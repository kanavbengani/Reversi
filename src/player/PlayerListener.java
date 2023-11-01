package player;

import model.PieceColor;

/**
 * This interface defines an interface for all objects that wish to listen to events
 * related to a player's moves in a game triggered by the model.
 */
public interface PlayerListener {
  /**
   * This method is called when it's the player's turn to make a move.
   *
   * @param pieceColor The color of the player's game pieces, indicating which player's turn it is.
   */
  void itsTheMoveOf(PieceColor pieceColor);
}
