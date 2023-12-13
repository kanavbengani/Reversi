package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cs3500.reversi.model.Direction;
import cs3500.reversi.model.Posn;
import cs3500.reversi.model.IROModel;

/**
 * Represents a Reversi strategy that filters out moves near the edges of the game board.
 * It aims to avoid choosing moves that are adjacent to the board edges.
 */
public class AvoidEdgesStrategy implements ReversiStrategy {
  @Override
  public List<Posn> chooseMove(List<Posn> possibleMoves, IROModel model) {
    List<Posn> edges = this.getEdges(model);
    List<Posn> moves = new ArrayList<>();

    Iterable<Posn> it = possibleMoves.isEmpty() ? model.getAllPosn() : possibleMoves;

    for (Posn move : it) {
      if (model.isMoveValid(model.getTurnColor(), move) && !edges.contains(move)) {
        moves.add(move);
      }
    }

    moves.sort(Comparator.comparingInt(Posn::getSecondCoordinate)
        .thenComparingInt(Posn::getFirstCoordinate));

    return moves;
  }

  // Gets edges (the cells adjacent to the corners of the board).
  private List<Posn> getEdges(IROModel model) {
    List<Posn> edges = new ArrayList<>();
    
    for (Posn c : model.getAllCorners()) {
      for (Direction offset : model.getDirections()) {
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
