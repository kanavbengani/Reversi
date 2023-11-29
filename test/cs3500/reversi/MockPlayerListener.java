package cs3500.reversi;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.player.PlayerFeatures;

public class MockPlayerListener implements PlayerFeatures {
  private final StringBuilder log;
  
  public MockPlayerListener(StringBuilder log) {
    this.log = log;
  }
  
  @Override
  public void pass() {
    this.log.append("Pass was called\n");
  }
  
  @Override
  public void move(AxialPosn axialPosn) {
    String result = "Move was called with axial position " + axialPosn + ".\n";
    this.log.append(result);
  }
}
