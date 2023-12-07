package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * The `HexModel` class represents the game board and logic for a Reversi game. It extends
 * the `AbstractModel` class and provides methods specific to a hexagonal board.
 */
public class HexModel extends AbstractModel {
  /**
   * Constructs a new Reversi game board with a specified number of rings.
   *
   * @param numRings The number of rings for the game board. Must be at least 1.
   * @throws IllegalArgumentException If the number of rings is less than 1.
   */
  public HexModel(int numRings) {
    super(numRings);
  }

  // For copy
  protected HexModel(int numRings, PieceColor pieceColor1, PieceColor pieceColor2,
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
  public HexModel(Map<Posn, Optional<PieceColor>> board) {
    super(board);
  }

  // Initializes board based on number of rings initialized earlier in the constructor
  @Override
  protected void initializeBoard() {
    int start = 0;
    int end = super.numRings;

    for (int r = -super.numRings; r <= super.numRings; r++) {
      for (int q = start; q <= end; q++) {
        Posn hexPosn = new HexPosn(q, r);
        super.board.put(hexPosn, Optional.empty());
      }

      if (start == -super.numRings) {
        end--;
      } else {
        start--;
      }
    }

    this.initializePieces();
  }

  // Initializes the pieces on the board around the center (in an alternate matter).
  @Override
  protected void initializePieces() {
    Posn center = new HexPosn(0, 0);

    for (int i = 0; i < this.getDirections().length; i++) {
      super.board.put(center.add(this.getDirections()[i]), Optional.of(i % 2 == 0
              ? super.pieceColor1
              : super.pieceColor2));
    }
  }

  // Returns the number of rings for the board passed in.
  @Override
  protected int validateBoard(Map<Posn, Optional<PieceColor>> board)
      throws IllegalArgumentException {
    int posnCount = board.keySet().size();
    double numRingsDouble = (-3 + Math.sqrt(-3 + 12 * posnCount)) / 6;
    if (numRingsDouble % 1 != 0) {
      throw new IllegalArgumentException("Illegal board passed in.");
    }
    
    int numRingsInt = (int) numRingsDouble;
    
    int start = 0;
    int end = numRingsInt;
    
    for (int r = -numRingsInt; r <= numRingsInt; r++) {
      for (int q = start; q <= end; q++) {
        Posn hexPosn = new HexPosn(q, r);
        if (!board.containsKey(hexPosn)) {
          throw new IllegalArgumentException("Illegal board passed in.");
        }
      }
      
      if (start == -numRingsInt) {
        end--;
      } else {
        start--;
      }
    }
    
    return numRingsInt;
  }
  
  // Operational Methods
  @Override
  public List<Posn> getAllCorners() {
    // TODO: Write Tests.
    int n = super.getNumRings();
    
    return new ArrayList<>(List.of(
        new HexPosn(n, 0), new HexPosn(0, n),
        new HexPosn(n, -n), new HexPosn(0, -n),
        new HexPosn(-n, 0), new HexPosn(-n, n)));
  }
  
  @Override
  public Direction[] getDirections() {
    // TODO: Write Tests.
    return HexDirection.values();
  }
  
  @Override
  public IModel copy() {
    return new HexModel(super.numRings, super.pieceColor1, super.pieceColor2,
        super.currentPieceColor, super.passCount, new ArrayList<>(), new HashMap<>(super.board));
  }
}
