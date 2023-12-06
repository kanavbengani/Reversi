package cs3500.reversi.provider.controller;

import cs3500.reversi.provider.model.Color;
import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a controller for the Reversi game.
 */
public interface ReversiController extends GUIFeatures, ModelFeatures {
  /**
   * Execute a single game of Reversi given a Reversi Model. When the game is over,
   * the playGame method ends.
   *
   * @param m a non-null Reversi Model
   */
  void playGame(ReversiModel m);

  /**
   * Handle an action in a single cell of the board by selecting or deselecting it.
   *
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  void handleCellClick(int row, int col);

  /**
   * Handle an action in a single cell of a board, such as make a move.
   *
   * @param keyCode the integer associated with the key pressed
   */
  void handleKeyPress(int keyCode);

  /**
   * Assigns a color to the player.
   *
   * @param currentColor the color to be assigned
   */
  void assignColor(Color currentColor);

  /**
   * Notifies the player that the game is over.
   */
  void notifyGameOver();
}
