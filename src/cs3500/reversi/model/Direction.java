package cs3500.reversi.model;

/**
 * An enum of six predefined `AxialPosn` directional offsets representing the six neighboring
 * positions in a hexagonal grid.
 */
public enum Direction {
  UPLEFT(0, -1), UPRIGHT(1, -1),
  RIGHT(1, 0), DOWNRIGHT(0, 1),
  DOWNLEFT(-1, 1), LEFT(-1, 0);

  public final int deltaQ;
  public final int deltaR;

  Direction(int deltaQ, int deltaR) {
    this.deltaQ = deltaQ;
    this.deltaR = deltaR;
  }
}