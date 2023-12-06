package cs3500.reversi.provider.model;

import cs3500.reversi.provider.controller.ModelFeatures;
import cs3500.reversi.provider.controller.ReversiController;

/**
 * Represents the model of a Reversi game. The model of the Reversi game should handle the core
 * functions of reversi, such as making a play, checking whether the game has ended, and getting
 * the score of a player.
 */
public interface ReversiModel extends ReadonlyReversiModel, ModelFeatures {

  /**
   * Makes a play at the given row and column, assigns board index of hexagonal board
   * a player once the play is made.
   *
   * @param row the row number
   * @param col the column number
   * @throws IllegalArgumentException if either the row or col does not exist, i.e. is a negative
   *                                  number, or it is larger than what it can possibly be.
   */
  void makePlay(int row, int col) throws IllegalArgumentException;

  /**
   * Passes the turn to the next player if the current player cannot make a move
   * or chooses not to make a move.
   */
  void passTurn();

  /**
   * Subscribes the given controller to the model.
   *
   * @param controller the controller to subscribe
   */
  void subscribe(ReversiController controller);
  
  /**
   * Starts the game.
   */
  void startGame();
}