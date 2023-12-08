package cs3500.reversi.strategy;

import cs3500.reversi.model.Posn;
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
  public List<Posn> chooseMove(List<Posn> possibleMoves, IROModel model) {
    this.initializeColors(model);
    Map<Posn, Integer> moves = this.doMinimax(model, initialDepth);
    return new ArrayList<>(moves.keySet());
  }

  // Initializes the colors for the player and
  // the opponent based on the current turn in the game.
  private void initializeColors(IROModel model) {
    this.myColor = model.getTurnColor();
    this.opponentColor = this.myColor.equals(PieceColor.BLACK)
            ? PieceColor.WHITE
            : PieceColor.BLACK;
  }

  // Does minimax with the given depth and model. Positive scores are good for the opponent
  // while negative scores are good for us.
  private Map<Posn, Integer> doMinimax(IROModel model, int depth) {
    Map<Posn, Integer> result;
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
  private Map<Posn, Integer> doOpponentPerspective(IROModel model, int depth) {
    Map<Posn, Integer> result = new HashMap<>();
    Posn move;
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
  private Map<Posn, Integer> doMyPerspective(IROModel model, int depth) {
    Map<Posn, Integer> result = new HashMap<>();
    for (Posn move : model.getAllPosn()) {
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
  private Map<Posn, Integer> doBaseCase(IROModel model) {
    Map<Posn, Integer> result = new HashMap<>();
    for (Posn move : model.getAllPosn()) {
      if (model.isMoveValid(model.getTurnColor(), move)) {
        IModel copyModel = model.copy();
        copyModel.playMove(model.getTurnColor(), move);
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
    if (model.getTurnColor().equals(this.myColor)) {
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
    List<Posn> it;
    PieceColor turn = model.getTurnColor();

    if (turn.equals(this.opponentColor)) {
      // If end is opponent's turn, go through only moves picked in their strategy.
      it = this.opponentStrategy.chooseMove(new ArrayList<>(), model);
      if (it.isEmpty()) {
        return Integer.MIN_VALUE / 2;
      }
    }
    else {
      // If end is my turn, go through all possible moves.
      List<Posn> validMoves = model.getAllPosn();
      validMoves.removeIf(move -> !model.isMoveValid(this.myColor, move));
      it = validMoves;
      if (it.isEmpty()) {
        return Integer.MAX_VALUE / 2;
      }
    }
    
    Map<Posn, Integer> result = computeResult(model, it);
    
    return turn.equals(this.opponentColor)
            ? Collections.max(result.values())
            : Collections.min(result.values());
  }
  
  // Computes the base case result hashmap by making a copy of the model and playing all the
  // moves passed in
  private Map<Posn, Integer> computeResult(IROModel model, List<Posn> it) {
    PieceColor turn = model.getTurnColor();
    Map<Posn, Integer> result = new HashMap<>();
    for (Posn move : it) {
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
    return result;
  }
  
  // Sorts the passed in Map in the order of score
  // followed by the axial position (topmost leftmost).
  private Map<Posn, Integer> sortAscending(Map<Posn, Integer> result, boolean ascending) {
    List<Map.Entry<Posn, Integer>> entryList = new ArrayList<>(result.entrySet());

    Comparator<Map.Entry<Posn, Integer>> comparator;
    if (ascending) {
      comparator = Comparator
              .comparingInt((Map.Entry<Posn, Integer> entry) -> entry.getValue())
              .thenComparingInt(entry -> entry.getKey().getSecondCoord())
              .thenComparingInt(entry -> entry.getKey().getFirstCoord());
    }
    else {
      comparator = Comparator
              .comparingInt((Map.Entry<Posn, Integer> entry) -> entry.getValue())
              .reversed()
              .thenComparingInt(entry -> entry.getKey().getSecondCoord())
              .thenComparingInt(entry -> entry.getKey().getFirstCoord());
    }

    entryList.sort(comparator);

    return entryList.stream()
            .collect(LinkedHashMap::new,
              (map, entry) -> map.put(entry.getKey(), entry.getValue()),
              LinkedHashMap::putAll);
  }
}

