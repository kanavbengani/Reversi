package cs3500.reversi.model;

/**
 * The `IModel` interface extends the `IROModel` interface and defines additional methods for
 * modifying the Reversi game state.
 */
public interface IModel extends IROModel {
  /**
   * Allows a piece to make a move on the game board, updating the game state based on the provided
   * piece and hexagonal position.
   *
   * @param pieceColor The piece color for which the move will be played for.
   * @param posn The position where the cs3500.reversi.player wants to make their move.
   * @throws IllegalStateException if a wrong piece is being placed out of turn
   * @throws IllegalArgumentException if the passed in hexagonal position is out of bounds
   */
  void playMove(PieceColor pieceColor, Posn posn) throws IllegalStateException, IllegalArgumentException;

  /**
   * Switches the current turn to the next cs3500.reversi.player, allowing for the progression
   * of the game.
   */
  void pass(PieceColor pc) throws IllegalStateException;

  /**
   * Returns a read-only version of this cs3500.reversi.model that can safely be passed to the
   * cs3500.reversi.view.
   * @return A read-only version of this cs3500.reversi.model
   */
  IROModel getReadOnlyModel();
  
  /**
   * Starts the game of Reversi, allowing the set-up of the game to be separated from the play.
   * Also, it allows the model to ensure setup has be done accurately with two listeners
   * (signifying two players).
   */
  void startGame();
}
