package cs3500.reversi.model;

import cs3500.reversi.controller.Controller;
import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.strategy.*;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

public final class ReversiFactory {
  
  public enum GameType {
    HUMAN,
    CAPTURE_MOST,
    GO_CORNER,
    AVOID_EDGES,
    MINIMAX
  }
  
  public static IModel makeModel(int numRings, ReversiFactory.GameType gt1, int gt1Depth,
                                 ReversiFactory.GameType gt2, int gt2Depth) {
    IModel model = new Model(numRings);
    IView viewBlack = new View(model, PieceColor.BLACK);
    IView viewWhite = new View(model, PieceColor.WHITE);
    
    Player player1 = ReversiFactory.getPlayer(gt1, gt1Depth, viewBlack, model);
    Player player2  = ReversiFactory.getPlayer(gt2, gt2Depth, viewWhite, model);
    
    new Controller(model, player1, viewBlack, PieceColor.BLACK);
    new Controller(model, player2, viewWhite, PieceColor.WHITE);
    
    return model;
  }
  
  private static Player getPlayer(ReversiFactory.GameType gt, int gtDepth, IView view,
                                  IModel model) {
    Player player;
    
    if (gtDepth != 0 && !gt.equals(ReversiFactory.GameType.MINIMAX)) {
      throw new IllegalArgumentException("Depth must be 0 for non-minimax strategies.");
    }
    
    if (!gt.equals(ReversiFactory.GameType.HUMAN)) {
      view.display(false);
    }
    
    switch (gt) {
      case HUMAN:
        player = new HumanPlayer();
        break;
      case CAPTURE_MOST:
        player = new AIPlayer(model, new CaptureMostStrategy());
        break;
      case AVOID_EDGES:
        player = new AIPlayer(model, new AndStrategy(
            new AvoidEdgesStrategy(),
            new CaptureMostStrategy()
        ));
        break;
      case GO_CORNER:
        player = new AIPlayer(model, new AndStrategy(new GoCornerStrategy(), new AndStrategy(
                new AvoidEdgesStrategy(),
                new CaptureMostStrategy())));
        break;
      case MINIMAX:
        if (gtDepth <= 0) {
          throw new IllegalArgumentException("Depth of Minimax must be at least 1");
        }
        player = new AIPlayer(model, new MinimaxStrategyDepth(new AndStrategy(
            new GoCornerStrategy(), new AndStrategy(new AvoidEdgesStrategy(),
            new CaptureMostStrategy())), gtDepth));
        break;
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
    
    return player;
  }
}