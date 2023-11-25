package cs3500.reversi.player;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.strategy.ReversiStrategy;
import cs3500.reversi.view.PlayerFeatures;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer implements Player {
  
  private final ReversiStrategy strategy;
  private final PieceColor color;
  private final IModel model;
  private final List<PlayerFeatures> listeners = new ArrayList<>();
  
  public AIPlayer(IModel model, ReversiStrategy strategy, PieceColor color) {
    this.model = model;
    this.strategy = strategy;
    this.color = color;
  }
  
  @Override
  public void itsYourMove(PieceColor pieceColor) {
    if (!this.color.equals(pieceColor)) {
      return;
    }
    
    List<AxialPosn> moves = this.strategy.chooseMove(new ArrayList<>(), this.model.getReadOnlyModel());
    
    if (!moves.isEmpty()) {
      for (PlayerFeatures f : this.listeners) {
        f.move(this.color, moves.get(0));
      }
    } else {
      for (PlayerFeatures f : this.listeners) {
        f.pass(this.color);
      }
    }
  }
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.listeners.add(features);
  }
}
