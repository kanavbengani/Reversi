package cs3500.reversi.strategy;

import java.util.List;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

public interface ReversiStrategy {
  List<AxialPosn> chooseMove(List<AxialPosn> possibleMoves, IROModel model);
}
