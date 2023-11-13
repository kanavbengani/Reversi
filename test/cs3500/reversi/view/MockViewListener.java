package cs3500.reversi.view;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.PieceColor;

public class MockViewListener implements ViewFeatures {
  private final StringBuilder log;

  public MockViewListener(IView view, StringBuilder log) {
    this.log = log;
    view.addFeatureListener(this);
  }

  @Override
  public void pass(PieceColor pieceColor) {
    this.log.append(pieceColor).append(" wants to pass!");
    System.out.println(pieceColor + " wants to pass!");
  }

  @Override
  public void move(PieceColor pieceColor, AxialPosn ap) {
    this.log.append(pieceColor).append(" wants to place a chip on ").append(ap).append("!");
    System.out.println(pieceColor + " wants to place a chip on " + ap + "!");
  }

  @Override
  public void quit() {

  }
}
