package cs3500.reversi.player;

import java.util.ArrayList;
import java.util.List;


/**
 * The HumanPlayer class represents a human player in the Reversi game.
 */
 public class HumanPlayer implements Player {
  private final List<PlayerFeatures> listeners;

  /**
   * Constructs a new HumanPlayer with an empty list of listeners.
   */
  public HumanPlayer() {
    this.listeners = new ArrayList<>();
  }
  
  @Override
  public void playAMove() {}
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.listeners.add(features);
  }
}
