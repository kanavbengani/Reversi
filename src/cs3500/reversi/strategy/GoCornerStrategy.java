package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;

/**
 * Represents a Reversi strategy that chooses moves based on moving towards the corners of the game
 * board. It evaluates the provided possible moves and selects those that are in the corners of
 * the game board.
 */
public class GoCornerStrategy implements ReversiStrategy {
  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    List<AxialPosn> corners = this.getCorners(model);
    List<AxialPosn> moves = new ArrayList<>();

    Iterable<AxialPosn> it = possibleMoves.isEmpty() ? corners : possibleMoves;

    for (AxialPosn move : it) {
      if (model.isMoveValid(model.getTurn(), move) && corners.contains(move)) {
        moves.add(move);
      }
    }

    // sorting moves list top most and then left most.
    moves.sort(Comparator.comparingInt((AxialPosn ap) -> ap.r).thenComparingInt(ap -> ap.q));

    return moves;
  }

  // Gets the corners of the board.
  private List<AxialPosn> getCorners(IROModel model) {
    int n = model.getNumRings();

    return new ArrayList<>(List.of(
            new AxialPosn(n, 0), new AxialPosn(0, n),
            new AxialPosn(n, -n), new AxialPosn(0, -n),
            new AxialPosn(-n, 0), new AxialPosn(-n, n)
    ));
  }
}
