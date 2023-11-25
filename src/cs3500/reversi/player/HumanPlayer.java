package cs3500.reversi.player;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.PlayerFeatures;

import java.util.ArrayList;
import java.util.List;


public class HumanPlayer implements Player {
  private final IModel model;
  private final List<PlayerFeatures> listeners = new ArrayList<>();
  
  public HumanPlayer(IModel model) {
    this.model = model;
  }
  
  @Override
  public void itsYourMove(PieceColor pieceColor) {}
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.listeners.add(features);
  }
}
