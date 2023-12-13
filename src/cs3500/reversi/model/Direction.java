package cs3500.reversi.model;

import cs3500.reversi.model.hex.HexPosn;
import cs3500.reversi.model.square.SquarePosn;

/**
 * Represents a Direction in the game of Reversi -- both hexagonal and square.
 */
public interface Direction {
  /**
   * Computes the hexagonal position for the given position added to this direction.
   *
   * @param hexPosn represents the hexagonal position to add this direction to.
   * @return the final position after the direction has been added.
   * @throws IllegalArgumentException if this direction is not compatible with given position.
   */
  Posn compute(HexPosn hexPosn) throws IllegalArgumentException;
  
  /**
   * Computes the square position for the given position added to this direction.
   *
   * @param squarePosn represents the square position to add this direction to.
   * @return the final position after the direction has been added.
   * @throws IllegalArgumentException if this direction is not compatible with given position.
   */
  Posn compute(SquarePosn squarePosn) throws IllegalArgumentException;
}
