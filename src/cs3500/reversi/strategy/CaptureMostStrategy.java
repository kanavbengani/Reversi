package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

public class CaptureMostStrategy implements ReversiStrategy {
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model,
                                    PieceColor color) {
    int maxCaptured = 0;
    List<AxialPosn> ties = new ArrayList<>();

    Iterable<AxialPosn> it = possibleMoves.isEmpty() ? model : possibleMoves;

    for (AxialPosn ap : it) {
      try {
        int captured = model.getAllCapturedPieces(color, ap).size();

        if (maxCaptured < captured) {
          maxCaptured = captured;
          ties = new ArrayList<>();
        }

        if (maxCaptured == captured) {
          ties.add(ap);
        }
      } catch (IllegalStateException | IllegalArgumentException ignored) {
      }
    }

    ties.sort(Comparator.comparingInt((AxialPosn ap) -> ap.r).thenComparingInt(ap -> ap.q));

    return ties;
  }
}
