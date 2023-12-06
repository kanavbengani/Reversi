package cs3500.reversi.provider.strategies;

import cs3500.reversi.provider.model.ReversiModel;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a strategy that tries to return a move that's in one of the corners.
 */
public class PlayCorner implements ReversiStrategy {
  @Override
  public int[] chooseMove(ReversiModel model) {
    int[] bestMove = new int[]{-1, -1};
    int score = -1;
    List<int[]> corners = getCorners(model);
    for (int[] corner : corners) {
      if (model.canPlay(corner[0], corner[1], model.getTurn())) {
        ReversiModel newModel = model.copyGame();
        newModel.makePlay(corner[0], corner[1]);
        int newScore = newModel.getScore(model.getTurn());
        if (newScore > score) {
          score = newScore;
          bestMove[0] = corner[0];
          bestMove[1] = corner[1];
        }
      }
    }
    return bestMove;
  }

  private List<int[]> getCorners(ReversiModel model) {
    List<int[]> corners = new ArrayList<>();
    int middle = model.getSize() / 2;
    corners.add(new int[]{0, 0});
    corners.add(new int[]{0, model.getRow(0).length - 1});
    corners.add(new int[]{middle, 0});
    corners.add(new int[]{middle, model.getRow(middle).length - 1});
    corners.add(new int[]{model.getSize() - 1, 0});
    corners.add(new int[]{model.getSize() - 1, model.getRow(model.getSize() - 1).length - 1});
    return corners;
  }
}
