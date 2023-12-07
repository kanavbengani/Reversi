package cs3500.reversi;

import cs3500.reversi.model.HexPosn;
import cs3500.reversi.model.Posn;
import cs3500.reversi.player.PlayerFeatures;

/**
 * Represents a mock player listener that implements all the methods of a PlayerFeatures.
 * A log is maintained to track which methods of this class were called.
 */
public class MockPlayerListener implements PlayerFeatures {
  private final StringBuilder log;
  
  /**
   * Constructs a mock player listener to execute the purpose statement of this class.
   * @param log represents the string builder that will keep track of which methods were called.
   */
  public MockPlayerListener(StringBuilder log) {
    this.log = log;
  }
  
  @Override
  public void pass() {
    this.log.append("Pass was called\n");
  }
  
  @Override
  public void move(Posn posn) {
    String result = "Move was called with axial position " + posn + ".\n";
    this.log.append(result);
  }
}
