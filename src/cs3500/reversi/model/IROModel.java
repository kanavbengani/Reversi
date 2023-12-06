package cs3500.reversi.model;

import java.util.List;
import java.util.Optional;

/**
 * The `Model` interface defines the read-only cs3500.reversi.model for a Reversi game. This
 * interface specifies methods for querying the state and status of the game.
 */
public interface IROModel {
  /**
   * Checks if a move is valid for a specific piece color at a given position on the Reversi board.
   *
   * @param pieceColor The piece color attempting the move.
   * @param ap The position on the Reversi board where the move is to be made.
   * @return `true` if the move is valid, `false` otherwise.
   */
  boolean isMoveValid(PieceColor pieceColor, AxialPosn ap);

  /**
   * Returns whether the Reversi game is over. The game is considered over when no more legal moves
   * can be made by any piece color.
   *
   * @return `true` if the game is over, `false` otherwise.
   */
  boolean isGameOver();

  /**
   * Retrieves a list of all captured pieces for a specific piece color at a given position on the
   * Reversi board.
   *
   * @param pieceColor The piece color attempting the move.
   * @param ap The position on the Reversi board where the move is to be made.
   * @return List of captured pieces.
   * @throws IllegalStateException If the move is not valid.
   */
  List<AxialPosn> getAllCapturedPieces(PieceColor pieceColor, AxialPosn ap)
          throws IllegalStateException;

  /**
   * Gets an optional representing the piece color of the cs3500.reversi.player who has won the
   * Reversi game. If the game is not yet over or if it's a draw, the `Optional` will be empty.
   *
   * @return An `Optional` containing the winning color if there is one, or an empty `Optional`
   *         if it's a draw.
   * @throws IllegalStateException if the game is not over.
   */
  Optional<PieceColor> getWinner() throws IllegalStateException;

  /**
   * Gets an optional representing the piece color at a specific cell position on the Reversi board.
   *
   * @param axialPosn The position of the cell on the Reversi board.
   * @return An `Optional` containing the piece color at the specified cell position, or an empty
   *         `Optional` if the position is empty.
   * @throws IllegalArgumentException if the provided `AxialPosn` is not a valid position on the
   *         board.
   */
  Optional<PieceColor> getPieceAt(AxialPosn axialPosn) throws IllegalArgumentException;

  /**
   * Gets the piece color whose turn it currently is in the Reversi game.
   *
   * @return The piece color whose turn it is.
   * @throws IllegalStateException if the game has not been started.
   */
  PieceColor getTurnColor() throws IllegalStateException;

  /**
   * Gets the number of rings in the hexagonal grid. Each ring consists of
   * hexagons positioned around the central hexagon.
   *
   * @return The number of rings in the hexagonal grid.
   */
  int getNumRings();

  /**
   * Gets the score for a specific piece color.
   *
   * @param color The piece color for which you want to calculate the score.
   * @return The score for the specified piece color.
   */
  int getScore(PieceColor color);

  /**
   * Returns whether the current piece color has any legal moves.
   *
   * @return Whether the current piece color has any legal moves.
   */
  boolean anyLegalMoves(PieceColor pieceColor);

  /**
   * Adds the given features class to be added as a listener to when the cs3500.reversi.model
   * triggers an event.
   * @param modelFeatures is a listener class that will be added to the features list.
   */
  void addListener(ModelFeatures modelFeatures);

  /**
   * Returns a list of all positions on the board.
   *
   * @return a list of all positions on the board.
   */
  List<AxialPosn> getAllPosn();

  /**
   * Returns a copy of this model.
   *
   * @return returns a copy of the model.
   */
  IModel copy();
}
