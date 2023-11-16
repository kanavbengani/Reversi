package cs3500.reversi.strategy;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

import java.util.*;

public class MinimaxStrategyDepth implements ReversiStrategy {
  private PieceColor myColor;
  private PieceColor opponentColor;
  private final ReversiStrategy opponentStrategy;

  private final int initialDepth;

  public MinimaxStrategyDepth(ReversiStrategy opponent, int depth) {
    if (depth < 1) {
      throw new IllegalArgumentException("depth has to be at least 1.");
    }
    this.opponentStrategy = Objects.requireNonNull(opponent);
    this.initialDepth = depth;
  }

  @Override
  public List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model) {
    this.initializeColors(model);
    Map<AxialPosn, Integer> moves = this.doMinimax((IModel) model, initialDepth);
    return new ArrayList<>(moves.keySet());
  }

  // Initializes the colors for the player and the opponent based on the current turn in the game.
  private void initializeColors(IROModel model) {
    this.myColor = model.getTurn();
    this.opponentColor = this.myColor.equals(PieceColor.BLACK)
            ? PieceColor.WHITE
            : PieceColor.BLACK;
  }

  private Map<AxialPosn, Integer> doMinimax(IModel model, int depth) {
    Map<AxialPosn, Integer> result = new HashMap<>();
    if (depth == 1) {
      for (AxialPosn move : model.getAllPosn()) {
        if (model.isMoveValid(model.getTurn(), move)) {
          IModel copyModel = model.copy();
          copyModel.playMove(model.getTurn(), move);
          if (copyModel.isGameOver()) {
            if (model.getTurn().equals(this.myColor)) {
              result.put(move, Integer.MIN_VALUE);
            }
            else {
              result.put(move, Integer.MAX_VALUE);
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
    }
    else if ((initialDepth - depth) % 2 == 0) {
      // my perspective
      for (AxialPosn move : model.getAllPosn()) {
        if (model.isMoveValid(this.myColor, move)) {
          IModel copyModel = model.copy();
          copyModel.playMove(this.myColor, move);
          if (copyModel.isGameOver()) {
            result.put(move, Integer.MIN_VALUE);
          } else {
            Collection<Integer> c = this.sortAscending(this.doMinimax(copyModel, depth - 1), true).values();
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
    }
    else {
      // opponent's perspective
      for (AxialPosn move : model.getAllPosn()) {
        if (model.isMoveValid(this.opponentColor, move)) {
          IModel copyModel = model.copy();
          copyModel.playMove(this.opponentColor, move);
          if (copyModel.isGameOver()) {
            result.put(move, Integer.MAX_VALUE);
          } else {
            Collection<Integer> c = this.sortAscending(this.doMinimax(copyModel, depth - 1), true).values();
            int maximizedOpponentMove;
            if (c.isEmpty()) {
              maximizedOpponentMove = Integer.MAX_VALUE / 2;
            } else {
              maximizedOpponentMove = Collections.max(c);
            }
            result.put(move, maximizedOpponentMove);
          }
        }
      }
      result = this.sortAscending(result, false);
    }
    System.out.println(depth + ": " + result);
    return result;
  }

  private Integer baseCase(IModel oppTurnModel) {
    if (oppTurnModel.isGameOver()) {
      throw new IllegalStateException("Game is over, move cannot be chosen.");
    }
    List<AxialPosn> it;

    PieceColor turn = oppTurnModel.getTurn();

    if (turn.equals(this.opponentColor)) {
      // If end is opponent's turn, go through only moves picked in their strategy.
      it = this.opponentStrategy.chooseMove(new ArrayList<>(),
              oppTurnModel.getReadOnlyModel());
      if (it.isEmpty()) {
        return Integer.MIN_VALUE / 2;
      }
    }
    else {
      // If end is my turn, go through all possible moves.
      List<AxialPosn> validMoves = oppTurnModel.getAllPosn();
      validMoves.removeIf(move -> !oppTurnModel.isMoveValid(this.myColor, move));
      it = validMoves;
      if (it.isEmpty()) {
        return Integer.MAX_VALUE / 2;
      }
    }

    Map<AxialPosn, Integer> result = new HashMap<>();

    for (AxialPosn move : it) {
      IModel copyModel = oppTurnModel.copy();
      copyModel.playMove(turn, move);
      if (copyModel.isGameOver()) {
        if (turn.equals(this.opponentColor)) {
          result.put(move, Integer.MAX_VALUE);
        }
        else {
          result.put(move, Integer.MIN_VALUE);
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

