package cs3500.reversi.model;

import java.awt.Color;

/**
 * The `PieceColor` enum represents the possible colors of game pieces in Reversi.
 */
public enum PieceColor {
  /**
   * The `BLACK` color represents the black side of the game piece.
   */
  BLACK("X", Color.BLACK),

  /**
   * The `WHITE` color represents the white side of the game piece.
   */
  WHITE("O", Color.WHITE);

  /**
   * The `String` representation of the piece color.
   */
  public final String str;
  public final Color color;

  /**
   * Constructs a `PieceColor` with the specified `String` representation.
   *
   * @param str The `String` representation of the piece color.
   * @param color The `Color` representation of the piece color.
   */
  PieceColor(String str, Color color) {
    this.str = str;
    this.color = color;
  }
}
