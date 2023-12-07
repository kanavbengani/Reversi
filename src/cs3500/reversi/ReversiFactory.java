package cs3500.reversi;

import cs3500.reversi.adapter.ControllerAdapter;
import cs3500.reversi.adapter.HexModelAdapter;
import cs3500.reversi.adapter.StrategyAdapter;
import cs3500.reversi.adapter.ViewAdapter;
import cs3500.reversi.controller.Controller;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;

import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;

import cs3500.reversi.provider.strategies.ChooseBestStrategy;
import cs3500.reversi.provider.strategies.MakeZeroOpponentTurnZero;
import cs3500.reversi.provider.strategies.MaximizeScore;
import cs3500.reversi.provider.strategies.PlayCorner;
import cs3500.reversi.provider.strategies.PlaySide;
import cs3500.reversi.provider.strategies.PlayThreeFrom;

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
    OUR_HUMAN,
    OUR_STRATEGY1,
    OUR_STRATEGY2,
    OUR_STRATEGY3,
    OUR_STRATEGY4,
    PROVIDER_HUMAN,
    PROVIDER_STRATEGY1,
    PROVIDER_STRATEGY2,
    PROVIDER_STRATEGY3
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
    // Using type HexModelAdapter to allow for passing between provider and our implementation.
    // This is because HexModelAdapter implements both IModel (Ours) and ReversiModel (Provider).
    HexModelAdapter model = new HexModelAdapter(numRings);
    IView viewBlack = new View(model, PieceColor.BLACK);
    IView viewWhite = ReversiFactory.getView2(gt2, model);
    
    Player player1 = ReversiFactory.getPlayer(gt1, gt1Depth, viewBlack, model);
    Player player2 = ReversiFactory.getPlayer2(gt2, gt2Depth, viewWhite, model);
    
    new Controller(model, player1, viewBlack, PieceColor.BLACK);
    ReversiFactory.initializeController2(gt2, model, player2, viewWhite);
    
    return model;
  }
  
  private static IView getView2(ReversiFactory.GameType gt, HexModelAdapter model) {
    switch (gt) {
      case PROVIDER_HUMAN:
      case PROVIDER_STRATEGY1:
      case PROVIDER_STRATEGY2:
      case PROVIDER_STRATEGY3:
        IView v =  new ViewAdapter(model);
        v.display(true);
        return v;
      default:
        return new View(model, PieceColor.WHITE); // Second player is always WHITE
    }
  }
  
  private static void initializeController2(ReversiFactory.GameType gt, IModel model,
                                            Player player2, IView viewWhite) {
    switch (gt) {
      case PROVIDER_HUMAN:
      case PROVIDER_STRATEGY1:
      case PROVIDER_STRATEGY2:
      case PROVIDER_STRATEGY3:
        new ControllerAdapter(model, player2, viewWhite, PieceColor.WHITE);
        break;
      default:
        new Controller(model, player2, viewWhite, PieceColor.WHITE);
    }
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
    if (gtDepth != 0 && !gt.equals(ReversiFactory.GameType.OUR_STRATEGY1)) {
      System.err.println("Depth must not be outlined for non-minimax strategies.");
      System.exit(0);
      // The below statement will never be reached. This is only for type-checker.
      return null;
    }
    
    if (!gt.equals(GameType.OUR_HUMAN)) {
      view.disableInput();
    }
    
    switch (gt) {
      case OUR_HUMAN:
        return new HumanPlayer();
      case OUR_STRATEGY1:
        if (gtDepth <= 0) {
          System.err.println("Depth of Minimax must exist and be at least 1.");
          System.exit(0);
          // The below statement will never be reached. This is only for type-checker.
          return null;
        }
        return new AIPlayer(model, new MinimaxStrategyDepth(new AndStrategy(
            new GoCornerStrategy(), new AndStrategy(new AvoidEdgesStrategy(),
            new CaptureMostStrategy())), gtDepth));
      case OUR_STRATEGY2:
        return new AIPlayer(model, new AndStrategy(
            new AvoidEdgesStrategy(),
            new CaptureMostStrategy()
        ));
      case OUR_STRATEGY3:
        return new AIPlayer(model, new AndStrategy(new GoCornerStrategy(), new AndStrategy(
            new AvoidEdgesStrategy(),
            new CaptureMostStrategy())));
      case OUR_STRATEGY4:
        return new AIPlayer(model, new CaptureMostStrategy());
      default:
        System.err.println("Invalid game type");
        System.exit(0);
        // The below statement will never be reached. This is only for type-checker.
        return null;
    }
  }
  
  /**
   * Helper method to create a player 2 based on the specified game type, depth, view, and model.
   *
   * @param gt      The game type for the player.
   * @param gtDepth The depth parameter for the strategy of the player.
   * @param view    The view associated with the player.
   * @param model   The Reversi model associated with the player.
   * @return A new Player instance configured based on the specified parameters.
   */
  private static Player getPlayer2(ReversiFactory.GameType gt, int gtDepth, IView view,
                                   IModel model) {
    if (gtDepth != 0 && !gt.equals(ReversiFactory.GameType.OUR_STRATEGY1)) {
      System.err.println("Depth must not be outlined for non-minimax strategies.");
      System.exit(0);
      // The below statement will never be reached. This is only for type-checker.
      return null;
    }
    
    switch (gt) {
      case PROVIDER_HUMAN:
        return new HumanPlayer();
      case PROVIDER_STRATEGY1:
        return new AIPlayer(model, new StrategyAdapter(
            new ChooseBestStrategy(new MaximizeScore())));
      case PROVIDER_STRATEGY2:
        return new AIPlayer(model, new StrategyAdapter(new ChooseBestStrategy(new PlayCorner(),
            new PlayThreeFrom(), new MaximizeScore())));
      case PROVIDER_STRATEGY3:
        return new AIPlayer(model, new StrategyAdapter(new MakeZeroOpponentTurnZero(),
            new PlayCorner(), new PlayThreeFrom(), new PlaySide(), new MaximizeScore()));
      default:
        return ReversiFactory.getPlayer(gt, gtDepth, view, model);
    }
  }
}
