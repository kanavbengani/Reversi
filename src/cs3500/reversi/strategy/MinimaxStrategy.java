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
    this.opponentColor = this.myColor.equals(PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;
    Map<AxialPosn, Integer> moves = this.doMinimax((IModel) model);
    return new ArrayList<>(moves.keySet());
  }

  private Map<AxialPosn, Integer> doMinimax(IModel myTurnModel) {
    Map<AxialPosn, Integer> result = new HashMap<>();

    for (AxialPosn move : myTurnModel.getAllPosn()) {
      // Iterating through only valid moves for my color.
      if (myTurnModel.isMoveValid(this.myColor, move)) {
        IModel copyModel = this.copyModel(myTurnModel);
        copyModel.playMove(this.myColor, move);
        if (copyModel.isGameOver()) {
          result.put(move, Integer.MAX_VALUE);
        }
        else {
          result.put(move, this.maximizeForOpponent(copyModel));
        }
      }
    }

    result = this.sortAscending(result);

    return result;
  }

  private Integer maximizeForOpponent(IModel oppTurnModel) {
    Map<AxialPosn, Integer> result = new HashMap<>();

    ReversiStrategy opponentStrategyGuess = new AndStrategy(new GoCornerStrategy(),
            new AndStrategy(new AvoidEdgesStrategy(), new CaptureMostStrategy()));

    List<AxialPosn> opponentMoves = opponentStrategyGuess.chooseMove(new ArrayList<>(),
            oppTurnModel.getReadOnlyModel());

    for (AxialPosn move : opponentMoves) {
      IModel copyModel = this.copyModel(oppTurnModel);
      copyModel.playMove(this.myColor, move);
      if (copyModel.isGameOver()) {
        result.put(move, Integer.MAX_VALUE);
      }
      else {
        result.put(move,
                oppTurnModel.getScore(this.myColor) - oppTurnModel.getScore(this.opponentColor));
      }
    }

    result = this.sortAscending(result);

    return result.get(new ArrayList<>(result.keySet()).get(0));
  }

  // Sort the given map by values in ascending order and top-most, left-most. Returns a
  // LinkedHashMap of which the keyset() method will always return a sorted list of keys.
  private Map<AxialPosn, Integer> sortAscending(Map<AxialPosn, Integer> result) {
    List<Map.Entry<AxialPosn, Integer>> entryList = new ArrayList<>(result.entrySet());

    // TODO: Test this, it could be completely wrong.
    entryList.sort((o1, o2) -> {
      if (o1.getValue() < o2.getValue()) {
        return -1;
      } else if (o1.getValue() > o2.getValue()) {
        return 1;
      } else if (o1.getKey().r < o2.getKey().r) {
        return -1;
      } else if (o1.getKey().r > o2.getKey().r) {
        return 1;
      } else if (o1.getKey().q < o2.getKey().q) {
        return -1;
      } else if (o1.getKey().q > o2.getKey().q) {
        return 1;
      } else {
        return 0;
      }
    });


    // Create a LinkedHashMap to store the sorted entries
    LinkedHashMap<AxialPosn, Integer> sortedMap = new LinkedHashMap<>();

    // Populate the LinkedHashMap with sorted entries
    for (Map.Entry<AxialPosn, Integer> entry : entryList) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }

    return sortedMap;
  }

//  private Map<AxialPosn, Integer> doMinimax(IModel model, PieceColor pieceColor, int depth) {
//    if (depth == 1) {
//      Map<AxialPosn, Integer> result = new HashMap<>();
//
//      for (AxialPosn move : model.getAllPosn()) {
//        if (model.isMoveValid(pieceColor, move)) {
//          int score = pieceColor.equals(target) ?
//                  model.getAllCapturedPieces(pieceColor, move).size() :
//                  -model.getAllCapturedPieces(pieceColor, move).size();
//          IModel copyBoard = this.copyModel(model);
//          copyBoard.playMove(pieceColor, move);
//          if (copyBoard.isGameOver()) {
//            result.put(move, pieceColor.equals(target) ? Integer.MAX_VALUE : Integer.MIN_VALUE);
//          }
//          else {
//            result.put(move, score);
//          }
//        }
//      }
//
//      return result;
//    }
//
//    Map<AxialPosn, Integer> result = new HashMap<>();
//
//    for (AxialPosn move : model.getAllPosn()) {
//      if (model.isMoveValid(pieceColor, move)) {
//
//        int score = pieceColor.equals(target) ?
//                model.getAllCapturedPieces(pieceColor, move).size() :
//                -model.getAllCapturedPieces(pieceColor, move).size();
//
//        IModel copyBoard = this.copyModel(model);
//        copyBoard.playMove(pieceColor, move);
//        PieceColor nextColor = pieceColor.equals(PieceColor.BLACK) ? PieceColor.WHITE :
//                PieceColor.BLACK;
//        Map<AxialPosn, Integer> branch = this.doMinimax(copyBoard, nextColor, depth - 1);
//
//        int branchScore = Collections.max(branch.entrySet(), Map.Entry.comparingByValue()).getValue();
//
//        result.put(move, score + branchScore);
//
//        // 1. Clone the model
//        // 2. Play the move
//        // 3. Evaluate using helper
//        // 4. Put to result
//      }
//    }
//
//    return result;
//  }

  private IModel copyModel(IModel model) {
    return model;
  }
}
