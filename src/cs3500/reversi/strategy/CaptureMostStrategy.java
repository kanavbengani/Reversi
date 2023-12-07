package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.Posn;
import cs3500.reversi.model.IROModel;

/**
 * Represents a Reversi strategy that chooses moves based on capturing the most opponent pieces.
 * It evaluates the number of opponent pieces captured for each move and selects moves that result
 * in the maximum number of captured pieces. In case of ties, it selects moves based on their
 * axial positions (topmost-leftmost).
 */
public class CaptureMostStrategy implements ReversiStrategy {
  @Override
  public List<Posn> chooseMove(List<Posn> possibleMoves, IROModel model) {
    int maxCaptured = 0;
    List<Posn> ties = new ArrayList<>();
    Iterable<Posn> it = possibleMoves.isEmpty() ? model.getAllPosn() : possibleMoves;

    for (Posn posn : it) {
      try {
        if (model.isMoveValid(model.getTurnColor(), posn)) {
          int captured = model.getAllCapturedPieces(model.getTurnColor(), posn).size();
          if (maxCaptured < captured) {
            maxCaptured = captured;
            ties = new ArrayList<>();
          }

          if (maxCaptured == captured) {
            ties.add(posn);
          }
        }
      } catch (IllegalStateException | IllegalArgumentException ignored) {
      }
    }

    ties.sort(Comparator.comparingInt(Posn::getFirst).thenComparingInt(Posn::getSecond));

    return ties;
  }
}