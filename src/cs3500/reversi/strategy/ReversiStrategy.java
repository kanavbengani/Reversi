package cs3500.reversi.strategy;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;

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
   * @return A list of AxialPosn representing the chosen move(s) based on the implemented strategy.
   */
  List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model);
}
