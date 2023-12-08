package cs3500.reversi;

import cs3500.reversi.controller.Controller;
import cs3500.reversi.controller.ModelFeaturesImpl;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.model.square.SquareModel;
import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.strategy.*;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;
import cs3500.reversi.view.square.SquarePanel;

/**
 * A main runner for a program of Reversi.
 */
public final class TestSquare {
  private static final int NUM_RINGS = 5;
  
  /**
   * Runs the main program using the HexModel and the View.
   * @param args String command line arguments
   */
  public static void main(String[] args) {
    IModel model = new SquareModel(8);
    IView viewBlack = new View(new SquarePanel(model.getReadOnlyModel(), PieceColor.BLACK));
    IView viewWhite = new View(new SquarePanel(model.getReadOnlyModel(), PieceColor.WHITE));
    
    Player player1 = new HumanPlayer();
    Player player2 = new AIPlayer(model, new MinimaxStrategyDepth(new AndStrategy(
        new GoCornerStrategy(), new AndStrategy(new AvoidEdgesStrategy(),
        new CaptureMostStrategy())), 5));
    
    new Controller(model, player1, viewBlack, PieceColor.BLACK);
    new Controller(model, player2, viewWhite, PieceColor.WHITE);
    
    model.startGame();
  }
}