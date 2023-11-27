package cs3500.reversi.model;

import java.util.Optional;

/**
 * This interface defines an interface for all objects that wish to listen to events
 * triggered by the cs3500.reversi.model.
 */
public interface ModelFeatures {
  /**
   * This method is called when it's the given color's turn to make a move.
   *
   * @param pieceColor The color of the piece whose turn it is to make a move.
   */
  void notifyTurn(PieceColor pieceColor);
  
  /**
   * This method is called when it is the move of the player passed in.
   *
   * @param pieceColor The color of the piece whose turn it is to play a move.
   */
  void playAMove(PieceColor pieceColor);
  
  /**
   * This method is called when the game is over.
   * @param winner the color of the piece who won. It will be empty if it is a stalemate.
   */
  void itsGameOver(Optional<PieceColor> winner);
}
