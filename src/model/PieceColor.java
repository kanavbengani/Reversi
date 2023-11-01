package model;

/**
 * The `PieceColor` enum represents the possible colors of game pieces in Reversi.
 */
public enum PieceColor {
  /**
   * The `BLACK` color represents the black side of the game piece.
   */
  BLACK("X"),

  /**
   * The `WHITE` color represents the white side of the game piece.
   */
  WHITE("O");

  /**
   * The `String` representation of the piece color.
   */
  public final String s;

  /**
   * Constructs a `PieceColor` with the specified `String` representation.
   *
   * @param s The `String` representation of the piece color.
   */
  PieceColor(String s) {
    this.s = s;
  }

  /**
   * Returns the `String` representation of the piece color.
   *
   * @return The `String` representation of the piece color.
   */
  @Override
  public String toString() {
    return this.s;
  }
}
