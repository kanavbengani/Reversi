package cs3500.reversi.strategy;

import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

public class AndStrategy implements ReversiStrategy {

  private final ReversiStrategy strat1;
  private final ReversiStrategy strat2;

  public AndStrategy(ReversiStrategy strat1, ReversiStrategy strat2) {
    this.strat1 = strat1;
    this.strat2 = strat2;
  }

  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    List<AxialPosn> move1 = strat1.chooseMove(possibleMoves, model);

    return strat2.chooseMove(move1, model);
  }
}