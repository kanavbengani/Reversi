package cs3500.reversi.provider.strategies;

import cs3500.reversi.provider.model.ReversiModel;

/**
 * A Strategy interface for choosing where to play next for the given player.
 */
public interface ReversiStrategy {
  /**
   * Chooses what move to play next for the given player in the model.
   *
   * @param model the model to choose a move for
   * @return the row and column of the move to make
   */
  int[] chooseMove(ReversiModel model);
}