package cs3500.reversi;

import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.PieceColor;

import java.util.Optional;

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
  public void notifyTurn(PieceColor pieceColor) {
    this.log.append("It's ").append(pieceColor).append("'s move!\n");
  }
  
  @Override
  public void playAMove(PieceColor pieceColor) {
    this.log.append(pieceColor).append(" needs to play a move!\n");
    
  }
  
  @Override
  public void itsGameOver(Optional<PieceColor> winner) {
    this.log.append("It's game over! ")
        .append(winner.map(pieceColor -> pieceColor + " won!").orElse("Stalemate!\n"));
  }
}
