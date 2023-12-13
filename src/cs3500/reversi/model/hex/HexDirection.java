package cs3500.reversi.model.hex;

import cs3500.reversi.model.Direction;
import cs3500.reversi.model.Posn;
import cs3500.reversi.model.square.SquarePosn;

/**
 * An enum of six predefined `HexPosn` directional offsets representing the six neighboring
 * positions in a hexagonal grid.
 */
enum HexDirection implements Direction {
  UPLEFT(0, -1), UPRIGHT(1, -1),
  RIGHT(1, 0), DOWNRIGHT(0, 1),
  DOWNLEFT(-1, 1), LEFT(-1, 0);

  final int deltaQ;
  final int deltaR;

  // Constructs a new HexDirection using the delta value of q- and r-coordinate.
  HexDirection(int deltaQ, int deltaR) {
    this.deltaQ = deltaQ;
    this.deltaR = deltaR;
  }
  
  @Override
  public Posn compute(HexPosn hexPosn) throws IllegalArgumentException {
    return new HexPosn(hexPosn.q + this.deltaQ, hexPosn.r + this.deltaR);
  }
  
  @Override
  public Posn compute(SquarePosn squarePosn) throws IllegalArgumentException {
    throw new IllegalArgumentException("Trying to add hexagonal direction to square position.");
  }
}