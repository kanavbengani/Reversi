package cs3500.reversi.player;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.PieceColor;

/**
 * The PlayerFeatures interface defines methods to handle various events and interactions in the
 * context of the Reversi.
 */
public interface PlayerFeatures {
  /**
   * Notifies the listener that a player has chosen to pass their turn.
   */
  void pass();

  /**
   * Notifies the listener that a player has made a move on the game board.
   *
   * @param axialPosn  The axial position on the game board where the move is made.
   */
  void move(AxialPosn axialPosn);
}
