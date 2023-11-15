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
    System.out.println("here");
    Map<AxialPosn, Integer> result = new HashMap<>();
    if (depth == 1) {
      System.out.println("HereBase");
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
        result = this.sortAscending(result);
      } else {
        result = this.sortAscending(result);
      }
    }
    else if ((initialDepth - depth) % 2 == 0) {
      for (AxialPosn move : model.getAllPosn()) {
        if (model.isMoveValid(this.myColor, move)) {
          IModel copyModel = model.copy();
          copyModel.playMove(this.myColor, move);
          if (copyModel.isGameOver()) {
            result.put(move, Integer.MIN_VALUE);
          } else {
            int maximizedOpponentMove =
                    Collections.min(this.sortAscending(this.doMinimax(copyModel, depth - 1)).values());
            result.put(move, maximizedOpponentMove);
          }
        }
      }
    }
    else {
      for (AxialPosn move : model.getAllPosn()) {
        if (model.isMoveValid(this.opponentColor, move)) {
          IModel copyModel = model.copy();
          copyModel.playMove(this.opponentColor, move);
          if (copyModel.isGameOver()) {
            result.put(move, Integer.MAX_VALUE);
          } else {
            // it is Collections.max because all given values are positive for opponent, negative for us.
            int minimizedMyMove =
                    Collections.max(this.sortAscending(this.doMinimax(copyModel, depth - 1)).values());
            result.put(move, minimizedMyMove);
          }
        }
      }
    }
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

