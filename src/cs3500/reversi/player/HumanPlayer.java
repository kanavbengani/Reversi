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
  public void playAMove() {
    // The behavior of this method is dependent on the view of this player to trigger an event.
    // Hence, it is practically "waiting" and does not return anything.
  }
  
  @Override
  public void addListener(PlayerFeatures features) {
    // Adds a listener to the listeners field, showing the Human player has the capability to emit
    // messages. However, it does not because the view of this player handles all of the triggering
    // of events from this player.
    this.listeners.add(features);
  }
}
