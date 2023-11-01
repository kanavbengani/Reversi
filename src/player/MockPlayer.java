package player;

import model.PieceColor;

/**
 * The `MockPlayer` class is an implementation of the `PlayerListener` interface that simulates
 * a player's moves by logging messages to a given log.
 */
public class MockPlayer implements PlayerListener {
  private final StringBuilder log;

  /**
   * Constructs a `MockPlayer` with the specified `StringBuilder` for logging moves.
   *
   * @param log The `StringBuilder` used for logging player moves.
   */
  public MockPlayer(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void itsTheMoveOf(PieceColor pieceColor) {
    this.log.append("it's ").append(pieceColor).append("'s move!\n");
  }
}
