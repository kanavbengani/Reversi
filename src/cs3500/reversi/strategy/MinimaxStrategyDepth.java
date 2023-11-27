package cs3500.reversi.strategy;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A Minimax strategy that defines a recursive Minimax strategy that
 * runs each computation upto a certain depth.
 */
public class MinimaxStrategyDepth implements ReversiStrategy {
  private PieceColor myColor;
  private PieceColor opponentColor;
  private final ReversiStrategy opponentStrategy;
  private final int initialDepth;

  /**
   * Constructs a new Minimax strategy with the passed in depth.
   *
   * @param opponent is the strategy of the opponent.
   * @param depth the amount of depth you would like to analyze.
   */
  public MinimaxStrategyDepth(ReversiStrategy opponent, int depth) {
    if (depth < 1) {
      throw new IllegalArgumentException("Depth has to be at least 1.");
    }
    this.opponentStrategy = Objects.requireNonNull(opponent);
    this.initialDepth = depth;
  }

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    // TODO: Add passing as a possible move.
    //       Keep in mind though during AI vs. AI,
    //       what if they keep passing because that is the optimal move?
    this.initializeColors(model);
    Map<AxialPosn, Integer> moves = this.doMinimax(model, initialDepth);
    return new ArrayList<>(moves.keySet());
  }

  // Initializes the colors for the player and
  // the opponent based on the current turn in the game.
  private void initializeColors(IROModel model) {
    this.myColor = model.getTurn();
    this.opponentColor = this.myColor.equals(PieceColor.BLACK)
            ? PieceColor.WHITE
            : PieceColor.BLACK;
  }

  // Does minimax with the given depth and model. Positive scores are good for the opponent
  // while negative scores are good for us.
  private Map<AxialPosn, Integer> doMinimax(IROModel model, int depth) {
    Map<AxialPosn, Integer> result;
    if (depth == 1) {
      result = this.doBaseCase(model);
    }
    else if ((initialDepth - depth) % 2 == 0) {
      // "My" perspective
      result = this.doMyPerspective(model, depth);
    }
    else {
      // Opponent's perspective
      result = this.doOpponentPerspective(model, depth);
    }
    return result;
  }

  // Executes the opponent's perspective, hence maximizing each of their moves.
  private Map<AxialPosn, Integer> doOpponentPerspective(IROModel model, int depth) {
    Map<AxialPosn, Integer> result = new HashMap<>();
    AxialPosn move;
    try {
      move = this.opponentStrategy.chooseMove(new ArrayList<>(), model).get(0);
    } catch (IndexOutOfBoundsException ib) {
      return new HashMap<>();
    }
    IModel copyModel = model.copy();
    copyModel.playMove(this.opponentColor, move);
    if (copyModel.isGameOver()) {
      if (copyModel.getWinner().isEmpty()) { // Stalemate
        result.put(move, 0);
      }
      else {
        if (copyModel.getWinner().get().equals(this.myColor)) { // We won.
          result.put(move, Integer.MIN_VALUE);
        } else { // Opponent won.
          result.put(move, Integer.MAX_VALUE);
        }
      }
    } else {
      Collection<Integer> c = this.sortAscending(
              this.doMinimax(copyModel, depth - 1), true).values();
      int maximizedOpponentMove;
      if (c.isEmpty()) {
        maximizedOpponentMove = Integer.MAX_VALUE / 2;
      } else {
        maximizedOpponentMove = Collections.max(c);
      }
      result.put(move, maximizedOpponentMove);
    }
    result = this.sortAscending(result, false);
    return result;
  }

  // Executes the 'my' (target) perspective, hence minimizing each of opponent's best moves.
  private Map<AxialPosn, Integer> doMyPerspective(IROModel model, int depth) {
    Map<AxialPosn, Integer> result = new HashMap<>();
    for (AxialPosn move : model.getAllPosn()) {
      if (model.isMoveValid(this.myColor, move)) {
        IModel copyModel = model.copy();
        copyModel.playMove(this.myColor, move);
        if (copyModel.isGameOver()) {
          if (copyModel.getWinner().isEmpty()) { // Stalemate
            result.put(move, 0);
          }
          else {
            if (copyModel.getWinner().get().equals(this.myColor)) { // We won.
              result.put(move, Integer.MIN_VALUE);
            } else { // Opponent won.
              result.put(move, Integer.MAX_VALUE);
            }
          }
        } else {
          Collection<Integer> c = this.sortAscending(
                  this.doMinimax(copyModel, depth - 1), true).values();
          int maximizedOpponentMove;
          if (c.isEmpty()) {
            maximizedOpponentMove = Integer.MIN_VALUE / 2;
          } else {
            maximizedOpponentMove = Collections.min(c);
          }
          result.put(move, maximizedOpponentMove);
        }
      }
    }
    result = this.sortAscending(result, true);
    return result;
  }
  
  // Executes the base case moves with depth = 1.
  private Map<AxialPosn, Integer> doBaseCase(IROModel model) {
    Map<AxialPosn, Integer> result = new HashMap<>();
    for (AxialPosn move : model.getAllPosn()) {
      if (model.isMoveValid(model.getTurn(), move)) {
        IModel copyModel = model.copy();
        copyModel.playMove(model.getTurn(), move);
        if (copyModel.isGameOver()) {
          if (copyModel.getWinner().isEmpty()) { // Stalemate
            result.put(move, 0);
          }
          else {
            if (copyModel.getWinner().get().equals(this.myColor)) { // We won.
              result.put(move, Integer.MIN_VALUE);
            } else { // Opponent won.
              result.put(move, Integer.MAX_VALUE);
            }
          }
        } else {
          int maximizedOpponentMove = this.baseCase(copyModel);
          result.put(move, maximizedOpponentMove);
        }
      }
    }
    if (model.getTurn().equals(this.myColor)) {
      result = this.sortAscending(result, true);
    } else {
      result = this.sortAscending(result, false);
    }
    return result;
  }

  // Executes last layer of the recursion tree, hence an output of an integer value
  // representing the difference in score. Positive (good for opponent), negative
  // (good for us).
  private Integer baseCase(IROModel model) {
    if (model.isGameOver()) {
      throw new IllegalStateException("Game is over, move cannot be chosen.");
    }
    List<AxialPosn> it;

    PieceColor turn = model.getTurn();

    if (turn.equals(this.opponentColor)) {
      // If end is opponent's turn, go through only moves picked in their strategy.
      it = this.opponentStrategy.chooseMove(new ArrayList<>(), model);
      if (it.isEmpty()) {
        return Integer.MIN_VALUE / 2;
      }
    }
    else {
      // If end is my turn, go through all possible moves.
      List<AxialPosn> validMoves = model.getAllPosn();
      validMoves.removeIf(move -> !model.isMoveValid(this.myColor, move));
      it = validMoves;
      if (it.isEmpty()) {
        return Integer.MAX_VALUE / 2;
      }
    }

    Map<AxialPosn, Integer> result = new HashMap<>();

    for (AxialPosn move : it) {
      IModel copyModel = model.copy();
      copyModel.playMove(turn, move);
      if (copyModel.isGameOver()) {
        if (copyModel.getWinner().isEmpty()) { // Stalemate
          result.put(move, 0);
        }
        else {
          if (copyModel.getWinner().get().equals(this.myColor)) { // We won.
            result.put(move, Integer.MIN_VALUE);
          } else { // Opponent won.
            result.put(move, Integer.MAX_VALUE);
          }
        }
      } else {
        result.put(move,
                copyModel.getScore(this.opponentColor) - copyModel.getScore(this.myColor));
      }
    }

    return turn.equals(this.opponentColor)
            ? Collections.max(result.values())
            : Collections.min(result.values());
  }

  // Sorts the passed in Map in the order of score
  // followed by the axial position (topmost leftmost).
  private Map<AxialPosn, Integer> sortAscending(Map<AxialPosn, Integer> result, boolean ascending) {
    List<Map.Entry<AxialPosn, Integer>> entryList = new ArrayList<>(result.entrySet());

    Comparator<Map.Entry<AxialPosn, Integer>> comparator;
    if (ascending) {
      comparator = Comparator
              .comparingInt((Map.Entry<AxialPosn, Integer> entry) -> entry.getValue())
              .thenComparingInt(entry -> entry.getKey().r)
              .thenComparingInt(entry -> entry.getKey().q);
    }
    else {
      comparator = Comparator
              .comparingInt((Map.Entry<AxialPosn, Integer> entry) -> entry.getValue())
              .reversed()
              .thenComparingInt(entry -> entry.getKey().r)
              .thenComparingInt(entry -> entry.getKey().q);
    }

    entryList.sort(comparator);

    return entryList.stream()
            .collect(LinkedHashMap::new,
              (map, entry) -> map.put(entry.getKey(), entry.getValue()),
              LinkedHashMap::putAll);
  }
}

