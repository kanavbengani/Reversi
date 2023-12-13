package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.Posn;
import cs3500.reversi.model.IROModel;

/**
 * Represents a Reversi strategy that chooses moves based on moving towards the corners of the game
 * board. It evaluates the provided possible moves and selects those that are in the corners of
 * the game board.
 */
public class GoCornerStrategy implements ReversiStrategy {
  @Override
  public List<Posn> chooseMove(List<Posn> possibleMoves, IROModel model) {
    List<Posn> corners = model.getAllCorners();
    List<Posn> moves = new ArrayList<>();

    Iterable<Posn> it = possibleMoves.isEmpty() ? corners : possibleMoves;

    for (Posn move : it) {
      if (model.isMoveValid(model.getTurnColor(), move) && corners.contains(move)) {
        moves.add(move);
      }
    }

    // sorting moves list top most and then left most.
    moves.sort(Comparator.comparingInt(Posn::getSecondCoordinate).thenComparingInt(Posn::getFirstCoordinate));

    return moves;
  }
}
