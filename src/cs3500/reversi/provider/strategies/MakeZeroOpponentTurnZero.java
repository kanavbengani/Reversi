package cs3500.reversi.provider.strategies;

import cs3500.reversi.provider.model.Color;
import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy that tries to return a move that leaves the opponent with no moves.
 */
public class MakeZeroOpponentTurnZero implements ReversiStrategy {
  @Override
  public int[] chooseMove(ReversiModel model) {
    int[] bestMove = new int[]{-1, -1};
    for (int row = 0; row < model.getSize(); row++) {
      for (int col = 0; col < model.getRow(row).length; col++) {
        if (model.canPlay(row, col, model.getTurn())) {
          ReversiModel newModel = model.copyGame();
          newModel.makePlay(row, col);
          int numPossibleMovesForPlayer = getNumMoves(newModel, newModel.getTurn());
          if (numPossibleMovesForPlayer == 0) {
            bestMove[0] = row;
            bestMove[1] = col;
          }
        }
      }
    }
    return bestMove;
  }

  /**
   * Gets the number of possible moves for the given player.
   *
   * @param model the model to check
   * @param turn  the player to check
   * @return the number of possible moves for the given player
   */
  public int getNumMoves(ReversiModel model, Color turn) {
    int numMoves = 0;
    for (int row = 0; row < model.getSize(); row++) {
      for (int col = 0; col < model.getRow(row).length; col++) {
        if (model.canPlay(row, col, turn)) {
          numMoves++;
        }
      }
    }
    return numMoves;
  }

}