package cs3500.reversi.model;

import java.util.*;

public abstract class AbstractModel implements IModel {
  protected final PieceColor pieceColor1;
  protected final PieceColor pieceColor2;
  protected PieceColor currentPieceColor;
  protected final Map<Posn, Optional<PieceColor>> board;
  protected final int numRings;
  protected List<ModelFeatures> listeners;
  protected int passCount;
  
  // CLASS INVARIANT: The number of key-value pairs in `board` is equal to `3 * numRings *
  // (numRings + 1) + 1`.
  
  /**
   * Constructs a new Reversi game board with a specified number of rings.
   *
   * @param numRings The number of rings for the game board. Must be at least 1.
   * @throws IllegalArgumentException If the number of rings is less than 1.
   */
  protected AbstractModel(int numRings) {
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
  protected AbstractModel(int numRings, PieceColor pieceColor1, PieceColor pieceColor2,
                     PieceColor currentPieceColor, int passCount, List<ModelFeatures> listeners,
                     Map<Posn, Optional<PieceColor>> board) {
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
  protected AbstractModel(Map<Posn, Optional<PieceColor>> board) {
    this.pieceColor1 = PieceColor.BLACK;
    this.pieceColor2 = PieceColor.WHITE;
    this.currentPieceColor = null;
    this.passCount = 0;
    this.numRings = this.validateBoard(board);
    this.board = board;
    this.listeners = new ArrayList<>();
  }
  
  // Initializes board based on number of rings initialized earlier in the constructor
  protected abstract void initializeBoard();
  
  // Initializes the pieces on the board around the center (in an alternate matter).
  protected abstract void initializePieces();
  
  // Returns the number of rings for the board passed in.
  protected abstract int validateBoard(Map<Posn, Optional<PieceColor>> board)
      throws IllegalArgumentException;
  
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
  public boolean isMoveValid(PieceColor pieceColor, Posn posn) {
    try {
      return !this.getAllCapturedPieces(pieceColor, posn).isEmpty();
    } catch (IllegalStateException | IllegalArgumentException ignored) {
      return false;
    }
  }
  
  @Override
  public List<Posn> getAllCapturedPieces(PieceColor pieceColor, Posn posn)
      throws IllegalStateException, IllegalArgumentException {
    if (!pieceColor.equals(this.currentPieceColor)) {
      throw new IllegalStateException("Not the piece's turn.");
    }
    
    if (!this.board.containsKey(posn)) {
      throw new IllegalStateException("Move is not in bounds.");
    }
    
    if (this.board.get(posn).isPresent()) {
      throw new IllegalStateException("Chip cannot be placed in an already occupied cell.");
    }
    
    List<Posn> finalPoints = this.validateAllDirections(pieceColor, posn);
    
    if (finalPoints.isEmpty()) {
      throw new IllegalStateException("Move is not valid.");
    }
    
    return finalPoints;
  }
  
  // Validating whether a move is possible in all directions
  private List<Posn> validateAllDirections(PieceColor pieceColor, Posn posn) {
    List<Posn> finalPoints = new ArrayList<>();
    
    if (this.board.getOrDefault(posn, Optional.empty()).isPresent()) {
      return new ArrayList<>();
    }
    
    for (Direction offset : this.getDirections()) {
      Posn tempPosn = posn.add(offset);
      
      if (this.board.getOrDefault(tempPosn, Optional.empty()).isEmpty()) {
        continue;
      }
      else if (this.board.get(tempPosn).get().equals(pieceColor)) {
        continue;
      }
      
      List<Posn> tempPoints = new ArrayList<>();
      tempPoints.add(tempPosn);
      
      tempPosn = tempPosn.add(offset);
      while (this.board.getOrDefault(tempPosn, Optional.empty()).isPresent()) {
        if (this.board.get(tempPosn).isEmpty()) {
          break;
        }
        PieceColor p = this.board.get(tempPosn).get();
        
        if (p.equals(pieceColor)) {
          finalPoints.addAll(tempPoints);
          break;
        }
        tempPoints.add(tempPosn);
        tempPosn = tempPosn.add(offset);
      }
    }
    
    return finalPoints;
  }
  
  @Override
  public boolean isGameOver() {
    return !(this.anyLegalMoves(this.pieceColor1) || this.anyLegalMoves(this.pieceColor2))
        || passCount == 2;
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
  public Optional<PieceColor> getPieceAt(Posn posn) throws IllegalArgumentException {
    if (!this.board.containsKey(posn)) {
      throw new IllegalArgumentException("Invalid coordinate passed in");
    }
    
    return this.board.get(posn);
  }
  
  @Override
  public PieceColor getTurnColor() throws IllegalStateException {
    if (this.currentPieceColor == null) {
      throw new IllegalStateException("Game has not started yet.");
    }
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
  
  // Returns whether there are any valid moves for the passed in color.
  @Override
  public boolean anyLegalMoves(PieceColor color) {
    return this.board.keySet().stream()
        .anyMatch(posn -> !this.validateAllDirections(color, posn).isEmpty());
  }
  
  @Override
  public void addListener(ModelFeatures modelFeatures) {
    this.listeners.add(modelFeatures);
  }
  
  // Operational Methods
  @Override
  public void playMove(PieceColor pieceColor, Posn posn)
      throws IllegalStateException, IllegalArgumentException {
    if (this.isGameOver()) {
      throw new IllegalStateException("Cannot be played when game is over.");
    }
    if (!this.board.containsKey(posn)) {
      throw new IllegalArgumentException("The passed-in position is out of bounds.");
    }
    
    List<Posn> points = this.getAllCapturedPieces(pieceColor, posn);
    
    this.board.put(posn, Optional.of(pieceColor));
    
    for (Posn tempPosn : points) {
      this.board.put(tempPosn, Optional.of(pieceColor));
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
  
  // Notifies all the listeners of this game
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
  public List<Posn> getAllPosn() {
    return new ArrayList<>(this.board.keySet());
  }
  
  @Override
  public abstract List<Posn> getAllCorners();
  
  @Override
  public abstract Direction[] getDirections();
  
  @Override
  public abstract IModel copy();
}
