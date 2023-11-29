package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * The `Model` class represents the game board and logic for a Reversi game. It implements
 * the `IModel` interface and provides methods for making moves, checking game status, and more.
 */
public class Model implements IModel {
  private final PieceColor pieceColor1;
  private final PieceColor pieceColor2;
  private PieceColor currentPieceColor;
  private final Map<AxialPosn, Optional<PieceColor>> board;
  private final int numRings;
  private final List<ModelFeatures> listeners;
  private int passCount;

  // CLASS INVARIANT: The number of key-value pairs in `board` is equal to `3 * numRings *
  // (numRings + 1) + 1`.

  /**
   * Constructs a new Reversi game board with a specified number of rings.
   *
   * @param numRings The number of rings for the game board. Must be at least 1.
   * @throws IllegalArgumentException If the number of rings is less than 1.
   */
  public Model(int numRings) {
    this.pieceColor1 = PieceColor.BLACK;
    this.pieceColor2 = PieceColor.WHITE;
    this.currentPieceColor = null;
    this.passCount = 0;
    this.board = new HashMap<>();
    if (numRings < 1) {
      throw new IllegalArgumentException("Number of rings must be at least 1.");
    }
    this.numRings = numRings;
    this.listeners = new ArrayList<>();
    this.initializeBoard();
  }

  // For copy
  private Model(int numRings, PieceColor pieceColor1, PieceColor pieceColor2,
                PieceColor currentPieceColor, int passCount, List<ModelFeatures> listeners,
                Map<AxialPosn, Optional<PieceColor>> board) {
    this.pieceColor1 = pieceColor1;
    this.pieceColor2 = pieceColor2;
    this.currentPieceColor = currentPieceColor;
    this.passCount = passCount;
    this.board = board;
    this.numRings = numRings;
    this.listeners = listeners;
  }

  /**
   * Constructs a new Reversi game board with a predefined board state.
   *
   * @param board The initial board state as a map of positions to optional piece colors.
   * @throws IllegalArgumentException If the provided board is invalid.
   */
  public Model(Map<AxialPosn, Optional<PieceColor>> board) {
    this.pieceColor1 = PieceColor.BLACK;
    this.pieceColor2 = PieceColor.WHITE;
    this.currentPieceColor = null;
    this.passCount = 0;
    this.numRings = this.validateBoard(board);
    this.board = board;
    this.listeners = new ArrayList<>();
  }

  // Initializes board based on number of rings initialized earlier in the constructor
  private void initializeBoard() {
    int start = 0;
    int end = this.numRings;

    for (int r = -this.numRings; r <= this.numRings; r++) {
      for (int q = start; q <= end; q++) {
        AxialPosn ap = new AxialPosn(q, r);
        this.board.put(ap, Optional.empty());
      }

      if (start == -this.numRings) {
        end--;
      } else {
        start--;
      }
    }

    this.initializePieces();
  }

  // Initializes the pieces on the board around the center (in an alternate matter).
  private void initializePieces() {
    AxialPosn center = new AxialPosn(0, 0);

    for (int i = 0; i < Direction.values().length; i++) {
      this.board.put(center.add(Direction.values()[i]), Optional.of(i % 2 == 0
              ? PieceColor.BLACK
              : PieceColor.WHITE));
    }
  }

  // Returns the number of rings for the board passed in.
  private int validateBoard(Map<AxialPosn, Optional<PieceColor>> board) throws IllegalArgumentException {
    int posnCount = board.keySet().size();
    double numRings = (-3 + Math.sqrt(-3 + 12 * posnCount)) / 6;
    if (numRings % 1 != 0) {
      throw new IllegalArgumentException("Illegal board passed in.");
    }
    return (int) numRings;
  }
  
  @Override
  public void startGame() throws IllegalStateException {
    if (this.listeners.size() != 2) {
      throw new IllegalStateException("There has to be two players in this game.");
    }
    this.currentPieceColor = this.pieceColor1;
    this.notifyListeners();
  }

  // Observation Methods
  @Override
  public boolean isMoveValid(PieceColor pieceColor, AxialPosn ap) {
    try {
      return !this.getAllCapturedPieces(pieceColor, ap).isEmpty();
    } catch (IllegalStateException | IllegalArgumentException ignored) {
      return false;
    }
  }

  @Override
  public List<AxialPosn> getAllCapturedPieces(PieceColor pieceColor, AxialPosn ap)
          throws IllegalStateException, IllegalArgumentException {
    if (!pieceColor.equals(this.currentPieceColor)) {
      throw new IllegalStateException("Not the piece's turn.");
    }
    
    if (!this.board.containsKey(ap)) {
      throw new IllegalStateException("Move is not in bounds.");
    }
    
    if (this.board.get(ap).isPresent()) {
      throw new IllegalStateException("Chip cannot be placed in an already occupied cell.");
    }
    
    List<AxialPosn> finalPoints = this.validateAllDirections(pieceColor, ap);
    
    if (finalPoints.isEmpty()) {
      throw new IllegalStateException("Move is not valid.");
    }

    return finalPoints;
  }

  // Validating whether a move is possible in all directions
  private List<AxialPosn> validateAllDirections(PieceColor pieceColor, AxialPosn ap) {
    List<AxialPosn> finalPoints = new ArrayList<>();

    if (this.board.getOrDefault(ap, Optional.empty()).isPresent()) {
      return new ArrayList<>();
    }

    for (Direction offset : Direction.values()) {
      AxialPosn tempAp = ap.add(offset);

      if (this.board.getOrDefault(tempAp, Optional.empty()).isEmpty()) {
        continue;
      }
      else if (this.board.get(tempAp).get().equals(pieceColor)) {
        continue;
      }

      List<AxialPosn> tempPoints = new ArrayList<>();
      tempPoints.add(tempAp);

      tempAp = tempAp.add(offset);
      while (this.board.getOrDefault(tempAp, Optional.empty()).isPresent()) {
        if (this.board.get(tempAp).isEmpty()) {
          break;
        }
        PieceColor p = this.board.get(tempAp).get();

        if (p.equals(pieceColor)) {
          finalPoints.addAll(tempPoints);
          break;
        }
        tempPoints.add(tempAp);
        tempAp = tempAp.add(offset);
      }
    }

    return finalPoints;
  }

  @Override
  public boolean isGameOver() {
    return !(this.anyLegalMoves(this.pieceColor1) || this.anyLegalMoves(this.pieceColor2)) || passCount == 2;
  }

  @Override
  public Optional<PieceColor> getWinner() throws IllegalStateException {
    if (!isGameOver()) {
      throw new IllegalStateException("The game is not over.");
    }

    int piece1Count = this.getScore(this.pieceColor1);
    int piece2Count = this.getScore(this.pieceColor2);

    if (piece1Count > piece2Count) {
      return Optional.of(this.pieceColor1);
    } else if (piece2Count > piece1Count) {
      return Optional.of(this.pieceColor2);
    }

    return Optional.empty();
  }

  @Override
  public Optional<PieceColor> getPieceAt(AxialPosn axialPosn) throws IllegalArgumentException {
    if (!this.board.containsKey(axialPosn)) {
      throw new IllegalArgumentException("Invalid coordinate passed in");
    }

    return this.board.get(axialPosn);
  }

  @Override
  public PieceColor getTurn() {
    return this.currentPieceColor;
  }

  @Override
  public int getNumRings() {
    return this.numRings;
  }

  @Override
  public int getScore(PieceColor pieceColor) {
    return (int) this.board.values().stream()
            .filter(optional -> optional.equals(Optional.of(pieceColor)))
            .count();
  }

  @Override
  public boolean anyLegalMoves() {
    return this.anyLegalMoves(this.currentPieceColor);
  }

  // Returns whether there are any valid moves for the passed in color.
  private boolean anyLegalMoves(PieceColor color) {
    return this.board.keySet().stream()
            .anyMatch(ap -> !this.validateAllDirections(color, ap).isEmpty());
  }

  @Override
  public void addListener(ModelFeatures modelFeatures) {
    this.listeners.add(modelFeatures);
  }

  // Operational Methods
  @Override
  public void playMove(PieceColor pc, AxialPosn ap)
          throws IllegalStateException, IllegalArgumentException {
    if (this.isGameOver()) {
      throw new IllegalStateException("Cannot be played when game is over.");
    }
    if (!this.board.containsKey(ap)) {
      throw new IllegalArgumentException("The passed-in position is out of bounds.");
    }

    List<AxialPosn> points = this.getAllCapturedPieces(pc, ap);

    this.board.put(ap, Optional.of(pc));

    for (AxialPosn tempAp : points) {
      this.board.put(tempAp, Optional.of(pc));
    }
    
    this.passCount = 0;
    this.switchTurn();
  }

  @Override
  public void pass(PieceColor pc)
          throws IllegalStateException {
    if (this.isGameOver()) {
      throw new IllegalStateException("Cannot be played when game is over.");
    }

    if (!this.currentPieceColor.equals(pc)) {
      throw new IllegalStateException("Cannot pass when its not your turn.");
    }
    
    passCount += 1;
    
    this.switchTurn();
  }

  // Switches turn to a different color and triggers event saying it is the current piece's move.
  private void switchTurn() {
    // Switching current piece color.
    this.currentPieceColor =
            this.currentPieceColor.equals(pieceColor1)
                    ? pieceColor2
                    : pieceColor1;
    
    this.notifyListeners();
  }
  
  private void notifyListeners() {
    if (this.isGameOver()) {
      // Notifying that the game is over.
      for (ModelFeatures f : this.listeners) {
          f.itsGameOver(this.getWinner());
      }
    } else {
      // Notifying whose move it is.
      for (ModelFeatures f : this.listeners) {
        f.notifyTurn(this.currentPieceColor);
      }
      // Asking to play a move.
      for (ModelFeatures f : this.listeners) {
        f.playAMove(this.currentPieceColor);
      }
    }
  }
  
  @Override
  public IROModel getReadOnlyModel() {
    return this;
  }

  @Override
  public List<AxialPosn> getAllPosn() {
    return new ArrayList<>(this.board.keySet());
  }

  @Override
  public IModel copy() {
    return new Model(this.numRings, this.pieceColor1, this.pieceColor2,
            this.currentPieceColor, this.passCount, new ArrayList<>(), new HashMap<>(this.board));
  }
}
