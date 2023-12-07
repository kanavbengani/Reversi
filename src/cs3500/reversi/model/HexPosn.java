package cs3500.reversi.model;

import java.util.Objects;

/**
 * The `HexPosn` class represents a position in a hexagonal grid using axial coordinates (q, r).
 */
public class HexPosn implements Posn {
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
   * Constructs a new `HexPosn` object with the specified axial coordinates (q, r).
   *
   * @param q The q-coordinate representing the horizontal axis.
   * @param r The r-coordinate representing the diagonal axis going from top-left to
   *          bottom-right (horizontal axis oriented 30 degrees clockwise)
   */
  public HexPosn(int q, int r) {
    this.q = q;
    this.r = r;
  }
  

  /**
   * Adds the given `HexPosn` to this `HexPosn`, returning a new `HexPosn` representing
   * the sum of the two positions.
   *
   * @param other The `HexPosn` to add to this `HexPosn`.
   * @return A new `HexPosn` representing the sum of the two positions.
   */
  @Override
  public Posn add(Direction other) {
    return other.compute(this);
  }
  
  @Override
  public int getFirst() {
    return this.r;
  }
  
  @Override
  public int getSecond() {
    return this.q;
  }
  
  /**
   * Compares this `HexPosn` object to another object to check if they are equal.
   *
   * @param o The object to compare to this `HexPosn`.
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

    HexPosn that = (HexPosn) o;

    return this.q == that.q && this.r == that.r;
  }

  /**
   * Generates a hash code for this `HexPosn` object, based on its axial coordinates.
   *
   * @return The hash code for this `HexPosn` object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.q, this.r);
  }

  /**
   * Returns a string representation of this `HexPosn` object in the format "(q, r)".
   *
   * @return A string representation of this `HexPosn` object.
   */
  @Override
  public String toString() {
    return "(" + this.q + ", " + this.r + ")";
  }
}
