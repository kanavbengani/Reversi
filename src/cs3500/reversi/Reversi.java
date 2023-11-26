package cs3500.reversi;

import cs3500.reversi.controller.Controller;
import cs3500.reversi.controller.IController;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.strategy.*;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

/**
 * A main runner for a program of Reversi.
 */
public final class Reversi {
  /**
   * Runs the main program using the Model and the View.
   * @param args String command line arguments
   */
  public static void main(String[] args) {
    IModel model = new Model(5);
    IView viewBlack = new View(model.getReadOnlyModel(), PieceColor.BLACK);
    IView viewWhite = new View(model.getReadOnlyModel(), PieceColor.WHITE);
//    Player player1 = new HumanPlayer();
    ReversiStrategy aiOpponent =
        new AndStrategy(
            new GoCornerStrategy(),
            new AndStrategy(
                new AvoidEdgesStrategy(),
                new CaptureMostStrategy()
            )
        );
    Player player1 = new AIPlayer(model, new MinimaxStrategyDepth(aiOpponent, 2), PieceColor.BLACK);
    
    // Player2 - Human
//    Player player2 = new HumanPlayer();
    
    // Player2 - AI
    ReversiStrategy aiOpponent2 =
        new AndStrategy(
        new GoCornerStrategy(),
        new AndStrategy(
            new AvoidEdgesStrategy(),
            new CaptureMostStrategy()
        )
    );
    Player player2 = new AIPlayer(model, new MinimaxStrategyDepth(aiOpponent, 2), PieceColor.WHITE);
//    viewWhite.display(false);
    
    IController controller1 = new Controller(model, player1, viewBlack);
    IController controller2 = new Controller(model, player2, viewWhite);
    model.startGame();
  }
}