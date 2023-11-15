package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;

/**
 * Represents a Reversi strategy that chooses moves based on capturing the most opponent pieces.
 * It evaluates the number of opponent pieces captured for each move and selects moves that result
 * in the maximum number of captured pieces. In case of ties, it selects moves based on their
 * axial positions (topmost-leftmost).
 */
public class CaptureMostStrategy implements ReversiStrategy {
  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    int maxCaptured = 0;
    List<AxialPosn> ties = new ArrayList<>();
    Iterable<AxialPosn> it = possibleMoves.isEmpty() ? model.getAllPosn() : possibleMoves;

    for (AxialPosn ap : it) {
      try {
        if (model.isMoveValid(model.getTurn(), ap)) {
          int captured = model.getAllCapturedPieces(model.getTurn(), ap).size();
          if (maxCaptured < captured) {
            maxCaptured = captured;
            ties = new ArrayList<>();
          }

          if (maxCaptured == captured) {
            ties.add(ap);
          }
        }
      } catch (IllegalStateException | IllegalArgumentException ignored) {
      }
    }

    ties.sort(Comparator.comparingInt((AxialPosn ap) -> ap.r).thenComparingInt(ap -> ap.q));

    return ties;
  }
}