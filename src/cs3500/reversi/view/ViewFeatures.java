package cs3500.reversi.view;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.PieceColor;

/**
 * The ViewFeatures interface defines methods to handle various events and interactions in the
 * context of the Reversi.
 */
public interface ViewFeatures {
  /**
   * Notifies the listener that a player has chosen to pass their turn.
   *
   * @param pieceColor The color of the player passing the turn.
   */
  void pass(PieceColor pieceColor);

  /**
   * Notifies the listener that a player has made a move on the game board.
   *
   * @param pieceColor The color of the player making the move.
   * @param axialPosn  The axial position on the game board where the move is made.
   */
  void move(PieceColor pieceColor, AxialPosn axialPosn);

  /**
   * Notifies the listener that a player has chosen to quit the game.
   */
  void quit();
}
