package cs3500.reversi.view;

import java.awt.Point;
import java.util.Objects;

/**
 * This class represents a Cartesian position with x and y coordinates.
 */
public class CartesianPosn {
  public final double x;
  public final double y;

  /**
   * Constructs a CartesianPosn object with the given x and y coordinates.
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   */
  public CartesianPosn(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a CartesianPosn object with the given Point.
   * @param p the Point.
   */
  public CartesianPosn(Point p) {
    this.x = p.x;
    this.y = p.y;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o the reference object with which to compare.
   * @return true if this object is the same as the obj argument; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CartesianPosn that = (CartesianPosn) o;
    return x == that.x && y == that.y;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   * Returns a string representation of this `CartesianPosn` object in the format "(x, y)".
   *
   * @return A string representation of this `CartesianPosn` object.
   */
  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }
}