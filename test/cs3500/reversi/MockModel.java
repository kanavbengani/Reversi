package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.PieceColor;

/**
 * Represents a mock model that appends to the given log and returns based
 * on the given parameters in the constructor.
 */
public class MockModel implements IModel {
  private final StringBuilder log;
  private final Map<AxialPosn, Integer> posnCaptures;
  private final int numRings;
  private final List<AxialPosn> listOfPosn = new ArrayList<>();
  private PieceColor currentPieceColor;
  
  /**
   * Constructs a MockModel with the given parameters.
   *
   * @param log          StringBuilder to store method call logs
   * @param posnCaptures map representing the captures at each axial position
   * @param numRings     number of rings in the game
   */
  public MockModel(StringBuilder log,
                   Map<AxialPosn, Integer> posnCaptures,
                   int numRings) {
    this.log = log;
    this.posnCaptures = posnCaptures;
    this.numRings = numRings;
    this.currentPieceColor = PieceColor.BLACK;
    this.initializePosn();
  }

  private void initializePosn() {
    int start = 0;
    int end = this.numRings;

    for (int r = -this.numRings; r <= this.numRings; r++) {
      for (int q = start; q <= end; q++) {
        listOfPosn.add(new AxialPosn(q, r));
      }

      if (start == -this.numRings) {
        end--;
      } else {
        start--;
      }
    }
  }

  @Override
  public void playMove(PieceColor pc, AxialPosn ap)
          throws IllegalStateException, IllegalArgumentException {
    this.log.append(pc).append(" wants to play ").append(ap).append("\n");
    if (pc.equals(this.currentPieceColor)) {
      this.currentPieceColor = this.currentPieceColor.equals(PieceColor.BLACK)
          ? PieceColor.WHITE : PieceColor.BLACK;
    }
    else {
      throw new IllegalStateException("Not this player's turn.");
    }
  }

  @Override
  public void pass(PieceColor pc) throws IllegalStateException {
    this.log.append(pc).append(" wants to pass.\n");
    if (pc.equals(this.currentPieceColor)) {
      this.currentPieceColor = this.currentPieceColor.equals(PieceColor.BLACK)
          ? PieceColor.WHITE : PieceColor.BLACK;
    }
    else {
      throw new IllegalStateException("Not this player's turn.");
    }
  }

  @Override
  public IROModel getReadOnlyModel() {
    this.log.append("Calling getReadOnlyModel.\n");
    return this;
  }
  
  @Override
  public void startGame() {
    this.log.append("Calling startGame.\n");
  }
  
  @Override
  public IModel copy() {
    this.log.append("Calling copy.\n");
    return this;
  }

  @Override
  public boolean isMoveValid(PieceColor pieceColor, AxialPosn ap) {
    this.log.append("Calling isMoveValid to check if ").append(pieceColor)
            .append(" can play on ").append(ap).append(".\n");

    return this.posnCaptures.containsKey(ap);
  }

  @Override
  public boolean isGameOver() {
    this.log.append("Calling isGameOver.\n");
    return false;
  }

  @Override
  public List<AxialPosn> getAllCapturedPieces(PieceColor pieceColor, AxialPosn ap)
          throws IllegalStateException, IllegalArgumentException {
    this.log.append("Calling getAllCapturedPieces if ").append(pieceColor)
            .append(" plays on ").append(ap).append(".\n");

    List<AxialPosn> result = new ArrayList<>();
    int size = this.posnCaptures.getOrDefault(ap, 0);

    for (int i = 0; i < size; i++) {
      result.add(new AxialPosn(0, 0)); // Don't care about the actual locations
    }

    return result;
  }

  @Override
  public Optional<PieceColor> getWinner()
          throws IllegalStateException {
    this.log.append("Calling getWinner.\n");
    return Optional.empty();
  }

  @Override
  public Optional<PieceColor> getPieceAt(AxialPosn axialPosn)
          throws IllegalArgumentException {
    this.log.append("Calling getPieceAt on ").append(axialPosn).append(".\n");
    return Optional.empty();
  }

  @Override
  public PieceColor getTurnColor() {
    this.log.append("Calling getTurn.\n");
    return PieceColor.BLACK;
  }

  @Override
  public int getNumRings() {
    this.log.append("Calling getNumRings.\n");
    return this.numRings;
  }

  @Override
  public int getScore(PieceColor color) {
    this.log.append("Calling getScore for ").append(color).append(".\n");
    return 0;
  }
  
  @Override
  public boolean anyLegalMoves(PieceColor pieceColor) {
    this.log.append("Calling anyLegalMoves.\n");
    return false;
  }

  @Override
  public void addListener(ModelFeatures modelFeatures) {
    this.log.append("addListener called in MockModel.\n");
  }

  @Override
  public List<AxialPosn> getAllPosn() {
    this.log.append("getAllPosn is called.\n");

    return this.listOfPosn;
  }
}
