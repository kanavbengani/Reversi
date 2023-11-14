package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

public class MinimaxStrategy implements ReversiStrategy {
  private PieceColor myColor;
  private PieceColor opponentColor;

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    this.myColor = model.getTurn();
    this.opponentColor = this.myColor.equals(PieceColor.BLACK)
            ? PieceColor.WHITE
            : PieceColor.BLACK;
    // ??? Your Move -> Best Outcome For Opponent ???
    Map<AxialPosn, Integer> moves = this.doMinimax((IModel) model);
    return new ArrayList<>(moves.keySet());
  }

  private Map<AxialPosn, Integer> doMinimax(IModel myTurnModel) {
    if (myTurnModel.isGameOver()) {
      throw new IllegalStateException("Game is over, move cannot be chosen.");
    }

    Map<AxialPosn, Integer> result = new HashMap<>();

    for (AxialPosn move : myTurnModel.getAllPosn()) {
      // Iterating through only valid moves for my color.
      if (myTurnModel.isMoveValid(this.myColor, move)) {
        IModel copyModel = myTurnModel.copy();
        copyModel.playMove(this.myColor, move);
        if (copyModel.isGameOver()) {
          result.put(move, Integer.MIN_VALUE);
        }
        else {
          int maximizedOpponentMove = this.maximizeForOpponent(copyModel);
          result.put(move, maximizedOpponentMove);
        }
      }
    }

    result = this.sortAscending(result);

    return result;
  }

  private Integer maximizeForOpponent(IModel oppTurnModel) {
    if (oppTurnModel.isGameOver()) {
      throw new IllegalStateException("Game is over, move cannot be chosen.");
    }

    Map<AxialPosn, Integer> result = new HashMap<>();

    ReversiStrategy opponentStrategyGuess = new AndStrategy(new GoCornerStrategy(),
            new AndStrategy(new AvoidEdgesStrategy(), new CaptureMostStrategy()));

    List<AxialPosn> opponentMoves = opponentStrategyGuess.chooseMove(new ArrayList<>(),
            oppTurnModel.getReadOnlyModel());

    if (opponentMoves.isEmpty()) {
      return Integer.MIN_VALUE / 2;
    }

    for (AxialPosn move : opponentMoves) {
      IModel copyModel = oppTurnModel.copy();
      copyModel.playMove(this.opponentColor, move);
      if (copyModel.isGameOver()) {
        result.put(move, Integer.MAX_VALUE);
      }
      else {
        // adding score from the perspective of opponent (positive means good for opponent).
        result.put(move,
                copyModel.getScore(this.opponentColor) - copyModel.getScore(this.myColor));
      }
    }

    result = this.sortAscending(result);

    // returning value of best move for opponent
    return result.get(new ArrayList<>(result.keySet()).get(result.keySet().size() - 1));
  }

  // Sort the given map by values in ascending order and top-most, left-most. Returns a
  // LinkedHashMap of which the keyset() method will always return a sorted list of keys.
  private Map<AxialPosn, Integer> sortAscending(Map<AxialPosn, Integer> result) {
    List<Map.Entry<AxialPosn, Integer>> entryList = new ArrayList<>(result.entrySet());

    entryList.sort((o1, o2) -> {
      if (o1.getValue() < o2.getValue()) {
        return -1;
      } else if (o1.getValue() > o2.getValue()) {
        return 1;
      } else if (o1.getKey().r < o2.getKey().r) {
        return -1;
      } else if (o1.getKey().r > o2.getKey().r) {
        return 1;
      } else return Integer.compare(o1.getKey().q, o2.getKey().q);
    });

    // Create a LinkedHashMap to store the sorted entries
    LinkedHashMap<AxialPosn, Integer> sortedMap = new LinkedHashMap<>();

    // Populate the LinkedHashMap with sorted entries
    for (Map.Entry<AxialPosn, Integer> entry : entryList) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }

    return sortedMap;
  }
}
