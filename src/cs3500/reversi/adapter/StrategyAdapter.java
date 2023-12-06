package cs3500.reversi.adapter;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.provider.strategies.ChooseBestStrategy;
import cs3500.reversi.provider.strategies.ReversiStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Adapter class for integrating our custom ReversiStrategy with external components
 * that expect a ReversiStrategy. Extends ChooseBestStrategy to bridge between our internal
 * implementation and the ReversiStrategy interface provided by external sources.
 */
public class StrategyAdapter extends ChooseBestStrategy
    implements cs3500.reversi.strategy.ReversiStrategy {
  
  /**
   * Constructor for the strategy.
   *
   * @param strategies are all the strategies possible to choose
   */
  public StrategyAdapter(ReversiStrategy... strategies) {
    super(strategies);
  }
  
  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    IModel m = model.copy();
    
    int[] move = super.chooseMove((ModelAdapter) model);
    
    if (Arrays.equals(move, new int[]{-1, -1})) {
      return new ArrayList<>();
    }
    
    return List.of(Utils.convertRowColToAxial(move[0], move[1], model.getNumRings()));
  }
}
