package cs3500.reversi.adapter;

import cs3500.reversi.model.hex.HexPosn;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.provider.model.Color;

import java.util.Optional;

/**
 * Utility class containing methods for converting between different representations
 * and adapting data types between the Reversi game implementation and the provider's
 * model.
 */
class Utils {
  
  /**
   * Converts row and column coordinates to axial coordinates in a Reversi game board.
   *
   * @param row      The row index.
   * @param col      The column index.
   * @param numRings The number of rings in the Reversi game board.
   * @return An HexPosn representing the axial coordinates.
   * @throws IllegalArgumentException if the row or col is negative, or if the row and
   *                                  col combination is invalid.
   */
  static HexPosn convertRowColToAxial(int row, int col, int numRings) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Invalid row, col.");
    }
    
    int r = row - numRings;
    int q;
    
    int numCols = numRings * 2 + 1 - Math.abs(row - numRings);
    
    if (r <= 0) {
      q = numRings - numCols + 1 + col;
    } else if (r <= numRings) {
      q = -numRings + col;
    } else {
      throw new IllegalArgumentException("Invalid row, col");
    }
    return new HexPosn(q, r);
  }
  
  /**
   * Converts a Color enum from the provider's model to a corresponding PieceColor enum
   * in the Reversi game implementation.
   *
   * @param c The Color enum to be converted.
   * @return An Optional containing the corresponding PieceColor, or empty if the input
   *         Color is null.
   */
  static Optional<PieceColor> colorToPieceColor(Color c) {
    if (c == null) {
      return Optional.empty();
    }
    if (c.equals(Color.BLACK)) {
      return Optional.of(PieceColor.BLACK);
    }
    
    return Optional.of(PieceColor.WHITE);
  }
  
  /**
   * Converts an Optional PieceColor enum from the Reversi game implementation to a
   * corresponding Color enum in the provider's model.
   *
   * @param pc The Optional PieceColor to be converted.
   * @return The corresponding Color, or null if the input Optional is empty.
   */
  static Color pieceColorToColor(Optional<PieceColor> pc) {
    if (pc.isEmpty()) {
      return null;
    }
    if (pc.get().equals(PieceColor.BLACK)) {
      return Color.BLACK;
    }
    
    return Color.WHITE;
  }
}
