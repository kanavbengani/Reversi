package cs3500.reversi.strategy;

import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;

/**
 * Represents a composite Reversi strategy that combines two strategies using the logical AND
 * operation. It applies the first strategy to obtain a set of possible moves and then applies
 * the second strategy to further refine the selection based on the results of the first strategy.
 */
public class AndStrategy implements ReversiStrategy {
  private final ReversiStrategy strategy1;
  private final ReversiStrategy strategy2;

  /**
   * Constructs an AndStrategy with the specified two strategies.
   *
   * @param strategy1 The first Reversi strategy to be applied.
   * @param strategy2 The second Reversi strategy to be applied based on the results of the first
   *               strategy.
   */
  public AndStrategy(ReversiStrategy strategy1, ReversiStrategy strategy2) {
    this.strategy1 = strategy1;
    this.strategy2 = strategy2;
  }

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    List<AxialPosn> move1 = this.strategy1.chooseMove(possibleMoves, model);

    return this.strategy2.chooseMove(move1, model);
  }
}