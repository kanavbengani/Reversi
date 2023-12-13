package cs3500.reversi.model.square;

import cs3500.reversi.model.AbstractModel;
import cs3500.reversi.model.Direction;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.model.Posn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The `SquareModel` class represents the game board and logic for a Reversi game. It extends
 * the `AbstractModel` class and provides methods specific to a square board.
 */
public class SquareModel extends AbstractModel {
  /**
   * Constructs a new Reversi game board with a specified number of rings.
   *
   * @param sideLength The number of rings for the game board. Must be at least 1.
   * @throws IllegalArgumentException If the number of rings is less than 1.
   */
  public SquareModel(int sideLength) {
    super(sideLength / 2);
    if (sideLength % 2 == 1) {
      throw new IllegalArgumentException("Side length must be even.");
    }
  }
  
  // For copy
  protected SquareModel(int numRings, PieceColor pieceColor1, PieceColor pieceColor2,
                        PieceColor currentPieceColor, int passCount, List<ModelFeatures> listeners,
                        Map<Posn, Optional<PieceColor>> board) {
    super(numRings, pieceColor1, pieceColor2, currentPieceColor, passCount, listeners, board);
  }
  
  /**
   * Constructs a new Reversi game board with a predefined board state.
   *
   * @param board The initial board state as a map of positions to optional piece colors.
   * @throws IllegalArgumentException If the provided board is invalid.
   */
  public SquareModel(Map<Posn, Optional<PieceColor>> board) {
    super(board);
  }
  
  @Override
  protected void initializeBoard() {
    for (int x = -super.getNumRings() + 1; x <= super.getNumRings(); x++) {
      for (int y = -super.getNumRings() + 1; y <= super.getNumRings(); y++) {
        Posn squarePosn = new SquarePosn(x, y);
        super.board.put(squarePosn, Optional.empty());
      }
    }
    
    this.initializePieces();
  }
  
  @Override
  protected void initializePieces() {
    super.board.put(new SquarePosn(0, 0), Optional.of(super.pieceColor1));
    super.board.put(new SquarePosn(1, 0), Optional.of(super.pieceColor2));
    super.board.put(new SquarePosn(1, 1), Optional.of(super.pieceColor1));
    super.board.put(new SquarePosn(0, 1), Optional.of(super.pieceColor2));
    
  }
  
  @Override
  protected int validateBoard(Map<Posn, Optional<PieceColor>> board)
      throws IllegalArgumentException {
    int posnCount = board.keySet().size();
    double numRingsDouble = Math.sqrt(posnCount) / 2;
    if (numRingsDouble % 1 != 0) {
      throw new IllegalArgumentException("Illegal board passed in.");
    }
    
    int numRingsInt = (int) numRingsDouble;
    
    for (int x = -numRingsInt + 1; x <= numRingsInt; x++) {
      for (int y = -numRingsInt + 1; y <= numRingsInt; y++) {
        Posn squarePosn = new SquarePosn(x, y);
        if (!board.containsKey(squarePosn)) {
          throw new IllegalArgumentException("Illegal board passed in.");
        }
      }
    }
    
    return numRingsInt;
  }
  
  @Override
  public List<Posn> getAllCorners() {
    int n = this.getNumRings();
    
    return new ArrayList<>(List.of(
        new SquarePosn(-n + 1, -n + 1), new SquarePosn(n, -n + 1),
        new SquarePosn(-n + 1, n), new SquarePosn(n, n)));
  }
  
  @Override
  public Direction[] getDirections() {
    return SquareDirection.values();
  }
  
  @Override
  public IModel copy() {
    return new SquareModel(this.numRings, this.pieceColor1, this.pieceColor2,
        this.currentPieceColor, this.passCount, new ArrayList<>(), new HashMap<>(this.board));
    
  }
}
