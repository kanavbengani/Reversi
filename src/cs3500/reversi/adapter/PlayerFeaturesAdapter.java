package cs3500.reversi.adapter;

import cs3500.reversi.controller.PlayerFeaturesImpl;
import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.provider.controller.GUIFeatures;
import cs3500.reversi.view.IView;

public class PlayerFeaturesAdapter extends PlayerFeaturesImpl implements GUIFeatures {
  public PlayerFeaturesAdapter(IModel model, IView view, PieceColor color) {
    super(model, view, color);
  }
  
  @Override
  public void makeMove(int row, int col) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Invalid row, col.");
    }
    
    int r = row - super.model.getNumRings();
    int q;
    
    int numCols = super.model.getNumRings() * 2 + 1 - Math.abs(row - super.model.getNumRings());
    
    if (r <= 0) {
      q = super.model.getNumRings() - numCols + 1 + col;
      super.move(new AxialPosn(q, r));
      return;
    }
    else if (r <= super.model.getNumRings()) {
      q = -super.model.getNumRings() + col;
      super.move(new AxialPosn(q, r));
      return;
    }
    throw new IllegalArgumentException("Invalid row, col");
  }
}
