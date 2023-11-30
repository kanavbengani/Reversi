package cs3500.reversi;

import cs3500.reversi.controller.Controller;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;

import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;

import cs3500.reversi.strategy.AndStrategy;
import cs3500.reversi.strategy.AvoidEdgesStrategy;
import cs3500.reversi.strategy.CaptureMostStrategy;
import cs3500.reversi.strategy.GoCornerStrategy;
import cs3500.reversi.strategy.MinimaxStrategyDepth;

import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

/**
 * The ReversiFactory class is responsible for creating instances of the Reversi game,
 * including the model, players, controllers, and views. It supports different game types,
 * each represented by the GameType enum.
 */
public final class ReversiFactory {
  
  /**
   * Enum representing different game types supported by the ReversiFactory.
   */
  public enum GameType {
    HUMAN,
    CAPTURE_MOST,
    GO_CORNER,
    AVOID_EDGES,
    MINIMAX
  }
  
  /**
   * Creates and returns a new Reversi model with the specified parameters.
   *
   * @param numRings  The number of rings on the game board.
   * @param gt1       The game type for player 1.
   * @param gt1Depth  The depth parameter for the strategy of player 1.
   * @param gt2       The game type for player 2.
   * @param gt2Depth  The depth parameter for the strategy of player 2.
   * @return A new Reversi model configured with the specified parameters.
   */
  public static IModel makeModel(int numRings, ReversiFactory.GameType gt1, int gt1Depth,
                                 ReversiFactory.GameType gt2, int gt2Depth) {
    IModel model = new Model(numRings);
    IView viewBlack = new View(model, PieceColor.BLACK);
    IView viewWhite = new View(model, PieceColor.WHITE);
    
    Player player1 = ReversiFactory.getPlayer(gt1, gt1Depth, viewBlack, model);
    Player player2 = ReversiFactory.getPlayer(gt2, gt2Depth, viewWhite, model);
    
    new Controller(model, player1, viewBlack, PieceColor.BLACK);
    new Controller(model, player2, viewWhite, PieceColor.WHITE);
    
    return model;
  }
  
  /**
   * Helper method to create a player based on the specified game type, depth, view, and model.
   *
   * @param gt      The game type for the player.
   * @param gtDepth The depth parameter for the strategy of the player.
   * @param view    The view associated with the player.
   * @param model   The Reversi model associated with the player.
   * @return A new Player instance configured based on the specified parameters.
   */
  private static Player getPlayer(ReversiFactory.GameType gt, int gtDepth, IView view,
                                  IModel model) {
    Player player;
    
    if (gtDepth != 0 && !gt.equals(ReversiFactory.GameType.MINIMAX)) {
      System.err.println("Depth must not be outlined for non-minimax strategies.");
      System.exit(0);
      // The below statement will never be reached. This is only for type-checker.
      return null;
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
          System.err.println("Depth of Minimax must exist and be at least 1.");
          System.exit(0);
          // The below statement will never be reached. This is only for type-checker.
          return null;
        }
        player = new AIPlayer(model, new MinimaxStrategyDepth(new AndStrategy(
            new GoCornerStrategy(), new AndStrategy(new AvoidEdgesStrategy(),
            new CaptureMostStrategy())), gtDepth));
        break;
      default:
        System.err.println("Invalid game type");
        System.exit(0);
        // The below statement will never be reached. This is only for type-checker.
        return null;
    }
    
    if (!gt.equals(GameType.HUMAN)) {
      view.disableInput();
    }
    
    return player;
  }
}
