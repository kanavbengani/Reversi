package cs3500.reversi.provider.strategies;

import cs3500.reversi.provider.model.Color;
import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy that tries to return a move that maximizes the score.
 */
public class MaximizeScore implements ReversiStrategy {
  @Override
  public int[] chooseMove(ReversiModel model) {
    int[] bestMove = new int[]{-1, -1};
    int bestScore = -1;
    for (int row = 0; row < model.getSize(); row++) {
      for (int col = 0; col < model.getRow(row).length; col++) {
        if (model.canPlay(row, col, model.getTurn())) {
          ReversiModel newModel = model.copyGame();
          newModel.makePlay(row, col);
          // Have to get opposite turn because makePlay changed the turn to the opposite player
          int score = newModel.getScore(oppositeTurn(newModel.getTurn()));
          if (score > bestScore) {
            bestScore = score;
            bestMove[0] = row;
            bestMove[1] = col;
          }
        }
      }
    }
    return bestMove;
  }

  private Color oppositeTurn(Color turn) {
    return turn == Color.BLACK ? Color.WHITE : Color.BLACK;
  }
}
