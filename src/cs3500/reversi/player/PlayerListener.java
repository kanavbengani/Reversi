package cs3500.reversi.player;

import cs3500.reversi.model.PieceColor;

/**
 * This interface defines an interface for all objects that wish to listen to events
 * related to a cs3500.reversi.player's moves in a game triggered by the cs3500.reversi.model.
 */
public interface PlayerListener {
  /**
   * This method is called when it's the cs3500.reversi.player's turn to make a move.
   *
   * @param pieceColor The color of the cs3500.reversi.player's game pieces, indicating which cs3500.reversi.player's turn it is.
   */
  void itsTheMoveOf(PieceColor pieceColor);
}
