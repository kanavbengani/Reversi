package cs3500.reversi.view;

/**
 * Represents an interface for a textual view to display the board of a Reversi game.
 * Implementing classes define the behavior to generate a textual representation of the game board.
 */
public interface ITextualView {

  /**
   * Generates a textual representation of the Reversi game board.
   *
   * @return A String representing the current state of the Reversi game board.
   */
  String toString();
}
