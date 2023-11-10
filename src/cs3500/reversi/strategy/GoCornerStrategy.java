package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

public class GoCornerStrategy implements ReversiStrategy {

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model,
                                    PieceColor color) {
    List<AxialPosn> corners = this.getCorners(model);
    List<AxialPosn> moves = new ArrayList<>();

    Iterable<AxialPosn> it = possibleMoves.isEmpty() ? model : possibleMoves;

    for (AxialPosn move : it) {
      if (model.isMoveValid(color, move) && corners.contains(move)) {
        moves.add(move);
      }
    }

    moves.sort(Comparator.comparingInt((AxialPosn ap) -> ap.r).thenComparingInt(ap -> ap.q));

    return moves;
  }

  private List<AxialPosn> getCorners(IROModel model) {
    int n = model.getNumRings();

    return new ArrayList<>(List.of(
            new AxialPosn(n, 0), new AxialPosn(0, n),
            new AxialPosn(n, -n), new AxialPosn(0, -n),
            new AxialPosn(-n, 0), new AxialPosn(-n, n)
    ));
  }
}
