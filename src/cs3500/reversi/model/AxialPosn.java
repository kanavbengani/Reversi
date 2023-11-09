package cs3500.reversi.model;

import java.util.Objects;

/**
 * The `AxialPosn` class represents a position in a hexagonal grid using axial coordinates (q, r).
 */
public class AxialPosn {
  /**
   * The q-coordinate in axial coordinates, representing horizontal positioning.
   */
  public final int q;

  /**
   * The r-coordinate in axial coordinates, representing diagonal positioning from top-left to
   * bottom-right.
   */
  public final int r;


  /**
   * Constructs a new `AxialPosn` object with the specified axial coordinates (q, r).
   *
   * @param q The q-coordinate representing the horizontal axis.
   * @param r The r-coordinate representing the diagonal axis going from top-left to
   *          bottom-right (horizontal axis oriented 30 degrees clockwise)
   */
  public AxialPosn(int q, int r) {
    this.q = q;
    this.r = r;
  }
  

  /**
   * Adds the given `AxialPosn` to this `AxialPosn`, returning a new `AxialPosn` representing
   * the sum of the two positions.
   *
   * @param other The `AxialPosn` to add to this `AxialPosn`.
   * @return A new `AxialPosn` representing the sum of the two positions.
   */
  public AxialPosn add(Direction other) {
    return new AxialPosn(this.q + other.deltaQ, this.r + other.deltaR);
  }

  /**
   * Compares this `AxialPosn` object to another object to check if they are equal.
   *
   * @param o The object to compare to this `AxialPosn`.
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

    AxialPosn that = (AxialPosn) o;

    return this.q == that.q && this.r == that.r;
  }

  /**
   * Generates a hash code for this `AxialPosn` object, based on its axial coordinates.
   *
   * @return The hash code for this `AxialPosn` object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.q, this.r);
  }

  /**
   * Returns a string representation of this `AxialPosn` object in the format "(q, r)".
   *
   * @return A string representation of this `AxialPosn` object.
   */
  @Override
  public String toString() {
    return "(" + this.q + ", " + this.r + ")";
  }
}
