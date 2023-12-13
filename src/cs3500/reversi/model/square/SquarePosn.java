package cs3500.reversi.model.square;

import cs3500.reversi.model.Direction;
import cs3500.reversi.model.Posn;

import java.util.Objects;

/**
 * The `SquarePosn` class represents a position in a square grid using axial coordinates (x, y).
 */
public class SquarePosn implements Posn {
  /**
   * The x-coordinate in axial coordinates, representing horizontal positioning.
   */
  public final int x;
  
  /**
   * The y-coordinate in axial coordinates, representing vertical positioning.
   */
  public final int y;
  
  /**
   * Constructs a new `SquarePosn` object with the specified axial coordinates (x, y).
   *
   * @param x The x-coordinate representing the horizontal axis.
   * @param y The y-coordinate representing the vertical axis.
   */
  public SquarePosn(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  @Override
  public Posn add(Direction other) {
    return other.compute(this);
  }
  
  @Override
  public int getSecondCoordinate() {
    return this.y;
  }
  
  @Override
  public int getFirstCoordinate() {
    return this.x;
  }
  
  /**
   * Compares this `SquarePosn` object to another object to check if they are equal.
   *
   * @param o The object to compare to this `SquarePosn`.
   * @return `true` if the objects are equal, `false` otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    
    SquarePosn that = (SquarePosn) o;
    
    return this.x == that.x && this.y == that.y;
  }
  
  /**
   * Generates a hash code for this `SquarePosn` object, based on its axial coordinates.
   *
   * @return The hash code for this `SquarePosn` object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }
  
  /**
   * Returns a string representation of this `SquarePosn` object in the format "(q, r)".
   *
   * @return A string representation of this `SquarePosn` object.
   */
  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }
}
