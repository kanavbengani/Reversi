package cs3500.reversi.player;

import java.util.ArrayList;
import java.util.List;


public class HumanPlayer implements Player {
  private final List<PlayerFeatures> listeners;
  
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
