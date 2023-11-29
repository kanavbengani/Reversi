package cs3500.reversi;

import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.PlayerFeatures;


public class MockView implements IView {
  private final StringBuilder log;
  
  public MockView(StringBuilder log) {
    this.log = log;
  }
  
  @Override
  public void display(boolean b) {
    String result = "display called with " + b + ".\n";
    this.log.append(result);
  }
  
  @Override
  public void disableInput() {
    String result = "disableInput called.\n";
    this.log.append(result);
  }
  
  @Override
  public void refresh() {
    this.log.append("refresh called\n");
  }
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.log.append("addListener called in MockView.\n");
  }
  
  @Override
  public void promptMessage(String message) {
    String result = "promptMessage called with message: " + message + "\n";
    this.log.append(result);
  }
  
  @Override
  public void itsTheTurnOf(PieceColor pieceColor) {
    String result = "itsTheTurnOf " + pieceColor + "\n";
    this.log.append(result);
  }
}
