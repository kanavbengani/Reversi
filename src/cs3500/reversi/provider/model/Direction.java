package cs3500.reversi.provider.model;

/**
 * Represents all the directions that a current position within the hexagonal board
 * can look.
 */
public enum Direction {
  UPLEFT(-1),
  UPRIGHT(-1),
  LEFT(0, -1),
  RIGHT(0, 1),
  DOWNLEFT(1),
  DOWNRIGHT(1);

  private final int rowDifference;
  private int colDifference;

  Direction(int rowDifference) {
    this.rowDifference = rowDifference;
  }

  Direction(int rowDifference, int colDifference) {
    this.rowDifference = rowDifference;
    this.colDifference = colDifference;
  }

  /**
   * Gets the row difference of the direction.
   *
   * @return the row difference of the direction
   */
  public int getRowDifference() {
    return rowDifference;
  }

  /**
   * Gets the column difference of the direction.
   *
   * @return the column difference of the direction
   */
  public int getColDifference() {
    return colDifference;
  }

  /**
   * Sets the column difference of the direction.
   *
   * @param colDifference the column difference of the direction
   */
  public void setColDifference(int colDifference) {
    this.colDifference = colDifference;
  }
}