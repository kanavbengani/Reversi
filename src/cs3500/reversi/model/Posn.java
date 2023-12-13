package cs3500.reversi.model;

/**
 * The {@code Posn} interface represents a position in a two-dimensional space.
 */
public interface Posn {
  
  /**
   * Adds the specified direction to the current position and returns a new position.
   *
   * @param other the direction to be added to the current position
   * @return a new {@code Posn} object representing the result of adding the direction
   *         to the current position
   */
  Posn add(Direction other);
  
  /**
   * Retrieves the first coordinate of the position.
   *
   * @return the first coordinate of the position
   */
  int getFirstCoordinate();
  
  /**
   * Retrieves the second coordinate of the position.
   *
   * @return the second coordinate of the position
   */
  int getSecondCoordinate();
}
