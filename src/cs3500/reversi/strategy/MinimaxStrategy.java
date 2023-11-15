package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

/**
 * Represents a Reversi strategy that uses the Minimax algorithm with a composite opponent strategy.
 * It evaluates possible moves by considering the best response from the opponent,
 * minimizing the potential loss.
 */
public class MinimaxStrategy implements ReversiStrategy {
  private PieceColor myColor;
  private PieceColor opponentColor;
  private final ReversiStrategy opponentStrategy;

  public MinimaxStrategy(ReversiStrategy opponent) {
    this.opponentStrategy = Objects.requireNonNull(opponent);
  }

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    this.initializeColors(model);
    Map<AxialPosn, Integer> moves = doMinimax((IModel) model);
    return new ArrayList<>(moves.keySet());
  }

  // Initializes the colors for the player and the opponent based on the current turn in the game.
  private void initializeColors(IROModel model) {
    this.myColor = model.getTurn();
    this.opponentColor = this.myColor.equals(PieceColor.BLACK)
            ? PieceColor.WHITE
            : PieceColor.BLACK;
  }

  // Performs the Minimax algorithm to evaluate and choose the best move for the current player.
  private Map<AxialPosn, Integer> doMinimax(IModel myTurnModel) {
    if (myTurnModel.isGameOver()) {
      throw new IllegalStateException("Game is over, move cannot be chosen.");
    }

    Map<AxialPosn, Integer> result = new HashMap<>();

    for (AxialPosn move : myTurnModel.getAllPosn()) {
      if (myTurnModel.isMoveValid(this.myColor, move)) {
        IModel copyModel = myTurnModel.copy();
        copyModel.playMove(this.myColor, move);
        if (copyModel.isGameOver()) {
          result.put(move, Integer.MIN_VALUE);
        } else {
          int maximizedOpponentMove = maximizeForOpponent(copyModel);
          result.put(move, maximizedOpponentMove);
        }
      }
    }

    return sortAscending(result);
  }

  // Maximizes the opponent's move by considering different opponent move options based on the
  // best composite strategy we have implemented (GoCorner, AvoidEdges, and then CaptureMost).
  private Integer maximizeForOpponent(IModel oppTurnModel) {
    if (oppTurnModel.isGameOver()) {
      throw new IllegalStateException("Game is over, move cannot be chosen.");
    }

    Map<AxialPosn, Integer> result = new HashMap<>();

    List<AxialPosn> opponentMoves = this.opponentStrategy.chooseMove(new ArrayList<>(),
            oppTurnModel.getReadOnlyModel());

    if (opponentMoves.isEmpty()) {
      return Integer.MIN_VALUE / 2;
    }

    for (AxialPosn move : opponentMoves) {
      IModel copyModel = oppTurnModel.copy();
      copyModel.playMove(this.opponentColor, move);
      if (copyModel.isGameOver()) {
        result.put(move, Integer.MAX_VALUE);
      } else {
        result.put(move,
                copyModel.getScore(this.opponentColor) - copyModel.getScore(this.myColor));
      }
    }

    return Collections.max(result.values());
  }

  // Sorts by ascending value in the result map and then topmost, leftmost key.
  private Map<AxialPosn, Integer> sortAscending(Map<AxialPosn, Integer> result) {
    List<Map.Entry<AxialPosn, Integer>> entryList = new ArrayList<>(result.entrySet());

    entryList.sort(Comparator
            .comparingInt((Map.Entry<AxialPosn, Integer> entry) -> entry.getValue())
            .thenComparingInt(entry -> entry.getKey().r)
            .thenComparingInt(entry -> entry.getKey().q));

    return entryList.stream()
            .collect(LinkedHashMap::new,
                (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                LinkedHashMap::putAll);
  }
}
