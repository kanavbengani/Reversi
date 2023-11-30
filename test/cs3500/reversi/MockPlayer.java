package cs3500.reversi;

import cs3500.reversi.player.Player;
import cs3500.reversi.player.PlayerFeatures;

/**
 * Represents a mock player that implements all the methods of a player. A log is maintained to
 * track which methods of this class were called.
 */
public class MockPlayer implements Player {
  private final StringBuilder log;
  
  /**
   * Constructs a mock player to execute the purpose statement of this class.
   * @param log represents the string builder that will keep track of which methods were called.
   */
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
