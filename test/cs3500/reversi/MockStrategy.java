package cs3500.reversi;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.strategy.ReversiStrategy;

import java.util.ArrayList;
import java.util.List;

public class MockStrategy implements ReversiStrategy {
  private final StringBuilder log;
  private final List<AxialPosn> validMoves;
  
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
