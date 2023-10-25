import java.util.Optional;

/**
 * The `ROModel` interface defines the read-only model for a Reversi game. This
 * interface specifies methods for querying the state and status of the game.
 */
public interface ROModel {


  /**
   * Returns whether the Reversi game is over. The game is considered over when no more legal moves
   * can be made by any player.
   *
   * @return `true` if the game is over, `false` otherwise.
   * @throws IllegalStateException if the game is in an invalid state.
   */
  boolean isGameOver() throws IllegalStateException;

  /**
   * Gets an optional representing the player who has won the Reversi game. If the game is not yet
   * over or if it's a draw, the `Optional` will be empty.
   *
   * @return An `Optional` containing the winning player if there is one, or an empty `Optional`
   * if the game is not over or if it's a draw.
   * @throws IllegalStateException if the game is in an invalid state.
   */
  Optional<Player> getWinner() throws IllegalStateException;

  /**
   * Gets an optional representing the player at a specific cell position on the Reversi board.
   *
   * @param hp The position of the cell on the Reversi board.
   * @return An `Optional` containing the player at the specified cell position, or an empty
   * `Optional` if the position is empty.
   * @throws IllegalStateException if the game is in an invalid state.
   * @throws IllegalArgumentException if the provided `HexPosn` is not a valid position on the
   * board.
   */
  Optional<Player> getCell(HexPosn hp) throws IllegalStateException, IllegalArgumentException;

  /**
   * Gets the player whose turn it currently is in the Reversi game.
   *
   * @return The player whose turn it is.
   * @throws IllegalStateException if the game is in an invalid state.
   */
  Player getTurn() throws IllegalStateException;
}
