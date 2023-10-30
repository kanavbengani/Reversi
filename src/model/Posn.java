package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The `Posn` class represents a position in a hexagonal grid using cube coordinates (x, y).
 */
public class Posn {
  /**
   * The x-coordinate in cube coordinates, representing horizontal positioning.
   */
  public final int x;

  /**
   * The y-coordinate in cube coordinates, representing diagonal positioning.
   */
  public final int y;
  
  /**
   * A list of six predefined `Posn` offsets representing the six neighboring positions in a hexagonal grid.
   */
  public static final List<Posn> OFFSETS =
          new ArrayList<>(List.of(
                  new Posn(0, -1), new Posn(1, -1),
                  new Posn(1, 0), new Posn(0, 1),
                  new Posn(-1, 1), new Posn(-1, 0)
          ));


  /**
   * Constructs a new `Posn` object with the specified cube coordinates (x, y).
   *
   * @param x The x-coordinate representing horizontal positioning.
   * @param y The y-coordinate representing vertical positioning.
   */
  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }
  

  /**
   * Adds the given `Posn` to this `Posn`, returning a new `Posn` representing the sum of
   * the two positions.
   *
   * @param other The `Posn` to add to this `Posn`.
   * @return A new `Posn` representing the sum of the two positions.
   */
  public Posn add(Posn other) {
    return new Posn(this.x + other.x, this.y + other.y);
  }

  /**
   * Compares this `Posn` object to another object to check if they are equal.
   *
   * @param o The object to compare to this `Posn`.
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

    Posn that = (Posn) o;

    return this.x == that.x && this.y == that.y;
  }

  /**
   * Generates a hash code for this `Posn` object, based on its cube coordinates.
   *
   * @return The hash code for this `Posn` object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }

  /**
   * Returns a string representation of this `Posn` object in the format "(x, y)".
   *
   * @return A string representation of this `Posn` object.
   */
  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }
}
