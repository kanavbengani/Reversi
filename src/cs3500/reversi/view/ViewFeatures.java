package cs3500.reversi.view;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.PieceColor;

public interface ViewFeatures {
  void pass(PieceColor pieceColor);
  void move(PieceColor pieceColor, AxialPosn ap);
  void quit();
}
