package cs3500.reversi;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.strategy.AndStrategy;
import cs3500.reversi.strategy.AvoidEdgesStrategy;
import cs3500.reversi.strategy.CaptureMostStrategy;
import cs3500.reversi.strategy.GoCornerStrategy;
import cs3500.reversi.strategy.MinimaxStrategy;
import cs3500.reversi.strategy.MinimaxStrategyDepth;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The StrategyRunner class demonstrates the usage of various strategies in a Reversi game.
 */
public class StrategyRunner {
  /**
   * The main method that executes the Reversi game with different strategies.
   * @param args Command line arguments (not used in this program).
   */
  public static void main(String[] args) {
    IModel model = new Model(5);
    IView view = new View(model.getReadOnlyModel(), PieceColor.BLACK);
    view.display(true);

    int count = 0;

    while (!model.isGameOver()) {
      count++;
      System.out.println("move " + count);
      List<AxialPosn> moves;
      moves = new MinimaxStrategyDepth(new AndStrategy(
              new GoCornerStrategy(),
              new AndStrategy(new AvoidEdgesStrategy(),
                      new CaptureMostStrategy())), 3).chooseMove(new ArrayList<>(), model);

      PieceColor turn = model.getTurn();

      if (!moves.isEmpty()) {
        model.playMove(turn, moves.get(0));
      } else {
        model.pass(turn);
      }

      view.refresh();
    }

    Optional<PieceColor> winner = model.getWinner();

    if (winner.isPresent()) {
      System.out.println("Game is over! " + winner.get() + " won the game!");
    } else {
      System.out.println("That was a TIE!");
    }
  }
}
