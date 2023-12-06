package cs3500.reversi.provider.strategies;

import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy that tries to return a move that's on one of the sides.
 */
public class PlaySide implements ReversiStrategy {
  @Override
  public int[] chooseMove(ReversiModel model) {
    int[] bestMove = new int[]{-1, -1};
    int score = -1;
    for (int row = 0; row < model.getSize(); row++) {
      for (int col = 0; col < model.getRow(row).length; col++) {
        if (model.canPlay(row, col, model.getTurn()) && isSide(row, col, model)) {
          ReversiModel newModel = model.copyGame();
          newModel.makePlay(row, col);
          int newScore = newModel.getScore(model.getTurn());
          if (newScore > score) {
            score = newScore;
            bestMove[0] = row;
            bestMove[1] = col;
          }
        }
      }
    }
    return bestMove;
  }

  private boolean isSide(int row, int col, ReversiModel model) {
    return row == 0
            || row == model.getSize() - 1
            || col == 0
            || col == model.getRow(row).length - 1;
  }
}