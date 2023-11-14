package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.Direction;
import cs3500.reversi.model.IROModel;

public class AvoidEdgesStrategy implements ReversiStrategy {

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    List<AxialPosn> edges = this.getEdges(model);
    List<AxialPosn> moves = new ArrayList<>();

    Iterable<AxialPosn> it = possibleMoves.isEmpty() ? model.getAllPosn() : possibleMoves;

    for (AxialPosn move : it) {
      if (model.isMoveValid(model.getTurn(), move) && !edges.contains(move)) {
        moves.add(move);
      }
    }

    moves.sort(Comparator.comparingInt((AxialPosn ap) -> ap.r).thenComparingInt(ap -> ap.q));

    return moves;
  }

  private List<AxialPosn> getEdges(IROModel model) {
    int n = model.getNumRings();
    List<AxialPosn> edges = new ArrayList<>();

    List<AxialPosn> corners = new ArrayList<>(List.of(
            new AxialPosn(n, 0), new AxialPosn(0, n),
            new AxialPosn(n, -n), new AxialPosn(0, -n),
            new AxialPosn(-n, 0), new AxialPosn(-n, n)
    ));

    for (AxialPosn c : corners) {
      for (Direction offset : Direction.values()) {
        try {
          model.getPieceAt(c.add(offset));
          edges.add(c.add(offset));
        } catch (IllegalArgumentException ignored) {
        }
      }
    }

    return edges;
  }
}
