import java.util.ArrayList;
import java.util.List;
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
   * A list of six predefined `HexPosn` offsets representing the six neighboring positions in a hexagonal grid.
   */
  public static final List<HexPosn> OFFSETS =
          new ArrayList<>(List.of(
                  new HexPosn(0, -1, 1), new HexPosn(1, -1, 0),
                  new HexPosn(1, 0, -1), new HexPosn(0, 1, -1),
                  new HexPosn(-1, 1, 0), new HexPosn(-1, 0, 1)
          ));


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
   * Constructs a new `HexPosn` object with two specified coordinates (q, r).
   * INVARIANT: q + r + s = 0
   *
   * @param q The q-coordinate representing horizontal positioning.
   * @param r The r-coordinate representing diagonal positioning.
   */
  public HexPosn(int q, int r) {
    this.q = q;
    this.r = r;
    this.s = -q - r;
  }

  /**
   * Adds the given `HexPosn` to this `HexPosn`, returning a new `HexPosn` representing the sum of
   * the two positions.
   *
   * @param other The `HexPosn` to add to this `HexPosn`.
   * @return A new `HexPosn` representing the sum of the two positions.
   */
  public HexPosn add(HexPosn other) {
    return new HexPosn(this.q + other.q, this.r + other.r, this.s + other.s);
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

  /**
   * Returns a string representation of this `HexPosn` object in the format "(q, r, s)".
   *
   * @return A string representation of this `HexPosn` object.
   */
  @Override
  public String toString() {
    return "(" + this.q + ", " + this.r + ", " + this.s + ")";
  }
}
