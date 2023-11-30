package cs3500.reversi;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.strategy.ReversiStrategy;

import java.util.List;

/**
 * Represents a mock strategy that implements all the methods of a strategy.
 * A log is maintained to track which methods of this class were called.
 */
public class MockStrategy implements ReversiStrategy {
  private final StringBuilder log;
  private final List<AxialPosn> validMoves;
  
  /**
   * Constructs a mock strategy to execute the purpose statement of this class.
   * @param log represents the string builder that will keep track of which methods were called.
   */
  public MockStrategy(StringBuilder log, List<AxialPosn> validMoves) {
    this.log = log;
    this.validMoves = validMoves;
  }
  
  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    this.log.append("chooseMove called.\n");
    
    return this.validMoves;
  }
}
