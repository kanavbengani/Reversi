package cs3500.reversi.model;

/**
 * The `MockModelListener` class is an implementation of the `ModelFeatures` interface that
 * simulates a cs3500.reversi.player's moves by logging messages to a given log.
 */
public class MockModelListener implements ModelFeatures {
  private final StringBuilder log;

  /**
   * Constructs a `MockModelListener` with the specified `StringBuilder` for logging moves.
   *
   * @param log The `StringBuilder` used for logging cs3500.reversi.player moves.
   */
  public MockModelListener(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void itsTheMoveOf(PieceColor pieceColor) {
    this.log.append("it's ").append(pieceColor).append("'s move!\n");
  }
}
