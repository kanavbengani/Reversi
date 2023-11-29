package cs3500.reversi.player;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.strategy.ReversiStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The AIPlayer class represents an AI-controlled player in the Reversi game. It uses a specified
 * ReversiStrategy to make moves based on the given game model.
 */
 public final class AIPlayer implements Player {
  private final ReversiStrategy strategy;
  private final IROModel model;
  private final List<PlayerFeatures> listeners;

  /**
   * Constructs a new AIPlayer with the specified game model and ReversiStrategy.
   *
   * @param model The game model implementing IROModel.
   * @param strategy The ReversiStrategy used by the AIPlayer to make moves.
   */
  public AIPlayer(IROModel model, ReversiStrategy strategy) {
    this.model = model;
    this.strategy = strategy;
    this.listeners = new ArrayList<>();
  }
  
  @Override
  public void playAMove() {
    if (this.model.isGameOver()) {
      return;
    }
    
    List<AxialPosn> moves = this.strategy.chooseMove(new ArrayList<>(), this.model);
    
    if (!moves.isEmpty()) {
      for (PlayerFeatures f : this.listeners) {
        f.move(moves.get(0));
      }
    } else {
      for (PlayerFeatures f : this.listeners) {
        f.pass();
      }
    }
  }
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.listeners.add(features);
  }
}
