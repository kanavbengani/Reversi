package cs3500.reversi.provider.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy that chooses the best strategy from a list of strategies.
 */
public class ChooseBestStrategy implements ReversiStrategy {
  private final List<ReversiStrategy> strategyList;

  /**
   * Constructor for the strategy.
   *
   * @param strategies are all the strategies possible to choose
   */
  public ChooseBestStrategy(ReversiStrategy... strategies) {
    strategyList = new ArrayList<>();
    strategyList.addAll(Arrays.asList(strategies));
  }

  @Override
  public int[] chooseMove(ReversiModel model) {
    int[] move = strategyList.get(0).chooseMove(model);
    List<int[]> spotsAroundCorners = getSpotsAroundCorners(model);
    while (move[0] == -1 && move[1] == -1 && strategyList.size() > 1) {
      strategyList.remove(0);
      move = strategyList.get(0).chooseMove(model);
      if (spotsAroundCorners.contains(move)
              && !(strategyList.get(0) instanceof MakeZeroOpponentTurnZero
              || strategyList.get(0) instanceof MaximizeScore)) {
        move = new int[]{-1, 1};
      }
    }
    return move;
  }

  private List<int[]> getSpotsAroundCorners(ReversiModel model) {
    List<int[]> spots = new ArrayList<>();
    int middle = model.getSize() / 2;
    // top left corner
    spots.add(new int[]{0, 1});
    spots.add(new int[]{1, 0});
    spots.add(new int[]{1, 1});

    // top right corner
    spots.add(new int[]{0, model.getRow(0).length - 2});
    spots.add(new int[]{1, model.getRow(0).length - 1});
    spots.add(new int[]{1, model.getRow(0).length - 2});

    // middle left corner
    spots.add(new int[]{middle - 1, 0});
    spots.add(new int[]{middle, 1});
    spots.add(new int[]{middle + 1, 0});

    // middle right corner
    spots.add(new int[]{middle - 1, model.getRow(middle).length - 1});
    spots.add(new int[]{middle, model.getRow(middle).length - 2});
    spots.add(new int[]{middle + 1, model.getRow(middle).length - 1});

    // bottom left corner
    spots.add(new int[]{model.getSize() - 2, 0});
    spots.add(new int[]{model.getSize() - 1, 1});
    spots.add(new int[]{model.getSize() - 2, 1});

    // bottom right corner
    spots.add(new int[]{model.getSize() - 2, model.getRow(model.getSize() - 1).length - 1});
    spots.add(new int[]{model.getSize() - 1, model.getRow(model.getSize() - 1).length - 2});
    spots.add(new int[]{model.getSize() - 2, model.getRow(model.getSize() - 1).length - 2});
    return spots;
  }
}