package model;

import player.Player;

import java.util.Optional;

/**
 * The `ROModel` interface defines the read-only model for a Reversi game. This
 * interface specifies methods for querying the state and status of the game.
 */
public interface IROModel {
  /**
   * Returns whether the Reversi game is over. The game is considered over when no more legal moves
   * can be made by any player.
   *
   * @return `true` if the game is over, `false` otherwise.
   */
  boolean isGameOver();

  /**
   * Gets an optional representing the player who has won the Reversi game. If the game is not yet
   * over or if it's a draw, the `Optional` will be empty.
   *
   * @return An `Optional` containing the winning player if there is one, or an empty `Optional`
   * if the game is not over or if it's a draw.
   * @throws IllegalStateException if the game is not over.
   */
  Optional<Player> getWinner() throws IllegalStateException;

  /**
   * Gets an optional representing the player at a specific cell position on the Reversi board.
   *
   * @param hp The position of the cell on the Reversi board.
   * @return An `Optional` containing the player at the specified cell position, or an empty
   * `Optional` if the position is empty.
   * @throws IllegalArgumentException if the provided `Posn` is not a valid position on the
   * board.
   */
  Optional<Player> getPlayerAt(Posn hp) throws IllegalArgumentException;

  /**
   * Gets the player whose turn it currently is in the Reversi game.
   *
   * @return The player whose turn it is.
   */
  Player getTurn();

  /**
   * Gets the number of rings in the hexagonal grid. Each ring consists of
   * hexagons positioned around the central hexagon.
   *
   * @return The number of rings in the hexagonal grid.
   */
  int getRings();
}
