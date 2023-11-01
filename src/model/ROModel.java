package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The `ROModel` class represents a read-only game model that allows observations such as
 * determining if the game is over, retrieving the piece color of a particular cell, and checking
 * the availability of legal moves for the current player.
 * This class provides a read-only view of a game board, allowing clients to query various aspects
 * of the game state without modifying it. It includes methods for checking the validity of moves,
 * determining the game's end condition, finding the winner, and retrieving information about the
 * current game state.
 */
public class ROModel implements IROModel {
  protected final PieceColor pieceColor1;
  protected final PieceColor pieceColor2;
  protected PieceColor currentPieceColor;
  protected final Map<AxialPosn, Optional<PieceColor>> board;
  private final int numRings;

  // CLASS INVARIANT: The number of key-value pairs in `board` is equal to `3 * numRings *
  // (numRings + 1) + 1`.

  /**
   * Constructs a read-only model with the given number of rings.
   *
   * @param numRings   The number of rings on the game board.
   * @throws IllegalArgumentException if the number of rings is less than 2.
   */
  protected ROModel(int numRings) {
    if (numRings < 1) {
      throw new IllegalArgumentException("Number of rings must be at least 1.");
    }

    this.pieceColor1 = PieceColor.BLACK;
    this.pieceColor2 = PieceColor.WHITE;
    this.currentPieceColor = this.pieceColor1;
    this.numRings = numRings;
    this.board = new HashMap<>();

    this.initializeBoard();
  }

  @Override
  public boolean isMoveValid(PieceColor pieceColor, AxialPosn ap) {
    try {
      return !this.getAllCapturedPieces(pieceColor, ap).isEmpty();
    } catch (IllegalStateException | IllegalArgumentException ignored) {
      return false;
    }
  }

  @Override
  public boolean isGameOver() {
    return !(this.anyLegalMoves(this.pieceColor1) || this.anyLegalMoves(this.pieceColor2));
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
  public Optional<PieceColor> getPieceAt(AxialPosn ap) throws IllegalArgumentException {
    if (!this.board.containsKey(ap)) {
      throw new IllegalArgumentException("Invalid coordinate passed in");
    }

    return this.board.get(ap);
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
    return board.keySet().stream()
            .anyMatch(ap -> isMoveValid(color, ap));
  }

  // Validates if the given hexagonal position can place this piece assuming the given hexagonal
  // position is in-bounds.
  protected List<AxialPosn> getAllCapturedPieces(PieceColor pieceColor, AxialPosn ap)
          throws IllegalStateException, IllegalArgumentException {
    if (this.board.getOrDefault(ap, Optional.empty()).isPresent()) {
      throw new IllegalStateException("Chip cannot be placed in an already occupied cell.");
    }

    if (!pieceColor.equals(this.currentPieceColor)) {
      throw new IllegalArgumentException("Not the piece's turn.");
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
    for (Direction offset : Direction.values()) {
      AxialPosn tempAp = ap.add(offset);
      int counter = 0;
      List<AxialPosn> tempPoints = new ArrayList<>();
      while (this.board.getOrDefault(tempAp, Optional.empty()).isPresent()) {
        tempPoints.add(tempAp);
        if (this.board.get(tempAp).isEmpty()) {
          break;
        }

        PieceColor p = this.board.get(tempAp).get();

        if (p.equals(pieceColor)) {
          if (counter == 0) {
            tempAp = tempAp.add(offset);
            counter += 1;
            continue;
          }
          finalPoints.addAll(tempPoints);
          break;
        }

        tempAp = tempAp.add(offset);
        counter += 1;
      }
    }

    return finalPoints;
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
}
