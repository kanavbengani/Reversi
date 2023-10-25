import java.util.Objects;

/**
 * The `HexPosn` class represents a position in a hexagonal grid using cube coordinates (q, r, s).
 */
public class HexPosn {
  /**
   * The q-coordinate in cube coordinates, representing horizontal positioning.
   */
  public final int q;

  /**
   * The r-coordinate in cube coordinates, representing diagonal positioning.
   */
  public final int r;

  /**
   * The s-coordinate in cube coordinates, representing the third axis.
   */
  public final int s;

  /**
   * Constructs a new `HexPosn` object with the specified cube coordinates (q, r, s).
   *
   * @param q The q-coordinate representing horizontal positioning.
   * @param r The r-coordinate representing diagonal positioning.
   * @param s The s-coordinate representing the third axis.
   */
  public HexPosn(int q, int r, int s) {
    this.q = q;
    this.r = r;
    this.s = s;
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

    return this.q == that.q && this.r == that.r && this.s == that.s;
  }

  /**
   * Generates a hash code for this `HexPosn` object, based on its cube coordinates.
   *
   * @return The hash code for this `HexPosn` object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.q, this.r, this.s);
  }
}
