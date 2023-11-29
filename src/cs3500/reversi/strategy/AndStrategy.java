package cs3500.reversi.strategy;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;

/**
 * Represents a composite Reversi strategy that combines two strategies using the logical AND
 * operation. It applies the first strategy to obtain a set of possible moves and then applies
 * the second strategy to further refine the selection based on the results of the first strategy.
 */
public class AndStrategy implements ReversiStrategy {
  private final ReversiStrategy strat1;
  private final ReversiStrategy strat2;

  /**
   * Constructs an AndStrategy with the specified two strategies.
   *
   * @param strat1 The first Reversi strategy to be applied.
   * @param strat2 The second Reversi strategy to be applied based on the results of the first
   *               strategy.
   */
  public AndStrategy(ReversiStrategy strat1, ReversiStrategy strat2) {
    this.strat1 = strat1;
    this.strat2 = strat2;
  }

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    List<AxialPosn> move1 = strat1.chooseMove(possibleMoves, model);

    return strat2.chooseMove(move1, model);
  }
}