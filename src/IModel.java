/**
 * The `IModel` interface extends the `IROModel` interface and defines additional methods for
 * modifying the Reversi game state.
 */
public interface IModel extends IROModel {
  /**
   * Allows a player to make a move on the game board, updating the game state based on the provided
   * player and hexagonal position.
   *
   * @param player The player making the move.
   * @param hp The hexagonal position where the player wants to make their move.
   * @throws IllegalStateException if it is not the player's turn
   * @throws IllegalArgumentException if the passed in hexagonal position is out of bounds
   */
  void playMove(Player player, Posn hp) throws IllegalStateException, IllegalArgumentException;

  /**
   * Switches the current turn to the next player, allowing for the progression of the game.
   */
  void switchTurn();

  /**
   * Returns a read-only version of this model that can safely be passed to the view.
   * @return A read-only version of this model
   */
  IROModel getReadOnlyModel();
}
