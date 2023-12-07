package cs3500.reversi.strategy;

import java.util.List;

import cs3500.reversi.model.HexPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.Posn;

/**
 * Represents a strategy interface for choosing moves in a Reversi game.
 * Implementing classes provide specific strategies for selecting moves based on the current game
 * state.
 */
public interface ReversiStrategy {
  /**
   * Chooses a move based on the strategy implemented by the class.
   *
   * @param possibleMoves The initial set of possible moves.
   * @param model         The Reversi model representing the current state of the game.
   * @return A list of HexPosn representing the chosen move(s) based on the implemented strategy.
   */
  List<Posn> chooseMove(List<Posn> possibleMoves, IROModel model);
}
