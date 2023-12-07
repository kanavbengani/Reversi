package cs3500.reversi.model;

public interface Direction {
  /**
   * Computes the hexagonal position for the given position added to this direction.
   *
   * @param hexPosn represents the hexagonal position to add this direction to.
   * @return the final hexagonal position after the direction has been added.
   * @throws IllegalArgumentException if this direction is not compatible with given position.
   */
  HexPosn compute(HexPosn hexPosn) throws IllegalArgumentException;
  
  /**
   * Computes the square position for the given position added to this direction.
   *
   * @param squarePosn represents the square position to add this direction to.
   * @return the final square position after the direction has been added.
   * @throws IllegalArgumentException if this direction is not compatible with given position.
   */
  SquarePosn compute(SquarePosn squarePosn) throws IllegalArgumentException;
}
