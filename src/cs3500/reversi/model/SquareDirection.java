package cs3500.reversi.model;

/**
 * An enum of six predefined `HexPosn` directional offsets representing the six neighboring
 * positions in a hexagonal grid.
 */
public enum SquareDirection implements Direction {
  UP(0, 1), DOWN(0, -1),
  RIGHT(1, 0), LEFT(-1, 0),
  UPLEFT(-1, 1), UPRIGHT(1, 1),
  DOWNLEFT(-1, -1), DOWNRIGHT(1, -1);
  
  final int deltaX;
  final int deltaY;
  
  // Constructs a new SquareDirection using the delta value of x- and y-coordinate.
  SquareDirection(int deltaX, int deltaY) {
    this.deltaX = deltaX;
    this.deltaY = deltaY;
  }
  
  @Override
  public HexPosn compute(HexPosn hexPosn) throws IllegalArgumentException {
    throw new IllegalArgumentException("Trying to add square direction to hexagonal position.");
  }
  
  @Override
  public SquarePosn compute(SquarePosn squarePosn) throws IllegalArgumentException {
    return new SquarePosn(squarePosn.x + this.deltaX, squarePosn.y + this.deltaY);
    
  }
}