package cs3500.reversi.provider.strategies;

import java.util.List;

import cs3500.reversi.provider.model.ReversiModel;


/**
 * Represents a strategy that tries to return a move that's three spots from a corner.
 */
public class PlayThreeFrom implements ReversiStrategy {
  @Override
  public int[] chooseMove(ReversiModel model) {
    int[] bestMove = new int[]{-1, -1};
    int bestScore = -1;
    List<int[]> spots = getSpotsThreeFromCorners(model);
    for (int[] spot : spots) {
      if (model.canPlay(spot[0], spot[1], model.getTurn())) {
        int newScore = model.getScore(model.getTurn());
        if (newScore > bestScore) {
          bestMove[0] = spot[0];
          bestMove[1] = spot[1];
          bestScore = newScore;
        }
      }
    }
    return bestMove;
  }

  private List<int[]> getSpotsThreeFromCorners(ReversiModel model) {
    List<int[]> spots = new java.util.ArrayList<>();
    int middle = model.getSize() / 2;
    // top left corner
    spots.add(new int[]{0, 2});
    spots.add(new int[]{1, 2});
    spots.add(new int[]{2, 2});
    spots.add(new int[]{2, 1});
    spots.add(new int[]{2, 0});
    // top right corner
    spots.add(new int[]{0, model.getRow(0).length - 3});
    spots.add(new int[]{1, model.getRow(0).length - 3});
    spots.add(new int[]{2, model.getRow(2).length - 3});
    spots.add(new int[]{2, model.getRow(2).length - 2});
    spots.add(new int[]{2, model.getRow(2).length - 1});
    // middle left corner
    spots.add(new int[]{middle - 2, 0});
    spots.add(new int[]{middle - 1, 1});
    spots.add(new int[]{middle, 2});
    spots.add(new int[]{middle + 1, 1});
    spots.add(new int[]{middle + 2, 0});
    // middle right corner
    spots.add(new int[]{middle - 2, model.getRow(middle - 2).length - 1});
    spots.add(new int[]{middle - 1, model.getRow(middle - 1).length - 2});
    spots.add(new int[]{middle, model.getRow(middle).length - 3});
    spots.add(new int[]{middle + 1, model.getRow(middle + 1).length - 2});
    spots.add(new int[]{middle + 2, model.getRow(middle + 2).length - 1});
    // bottom left corner
    spots.add(new int[]{model.getSize() - 3, 0});
    spots.add(new int[]{model.getSize() - 3, 1});
    spots.add(new int[]{model.getSize() - 3, 2});
    spots.add(new int[]{model.getSize() - 2, 2});
    spots.add(new int[]{model.getSize() - 1, 2});
    // bottom right corner
    spots.add(new int[]{model.getSize() - 3, model.getRow(model.getSize() - 3).length - 1});
    spots.add(new int[]{model.getSize() - 3, model.getRow(model.getSize() - 3).length - 2});
    spots.add(new int[]{model.getSize() - 3, model.getRow(model.getSize() - 3).length - 3});
    spots.add(new int[]{model.getSize() - 2, model.getRow(model.getSize() - 2).length - 3});
    spots.add(new int[]{model.getSize() - 1, model.getRow(model.getSize() - 1).length - 3});

    return spots;
  }
}