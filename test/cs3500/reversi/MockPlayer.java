package cs3500.reversi;

import cs3500.reversi.player.Player;
import cs3500.reversi.player.PlayerFeatures;

public class MockPlayer implements Player {
  private final StringBuilder log;
  
  public MockPlayer(StringBuilder log) {
    this.log = log;
  }
  
  @Override
  public void playAMove() {
    this.log.append("playAMove called\n");
  }
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.log.append("addListener called in MockPlayer.\n");
  }
}
