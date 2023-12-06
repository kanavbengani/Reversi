package cs3500.reversi.adapter;

import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.PlayerFeatures;
import cs3500.reversi.provider.model.ReadonlyReversiModel;
import cs3500.reversi.provider.view.BasicReversiFrame;
import cs3500.reversi.view.IView;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ViewAdapter extends BasicReversiFrame implements IView {
  
  /**
   * Constructs a BasicReversiFrame.
   *
   * @param model the model to be used
   */
  public ViewAdapter(ReadonlyReversiModel model) {
    super(model);
  }
  
  @Override
  public void display(boolean b) {
    super.makeVisible(b);
  }
  
  @Override
  public void disableInput() {
    for (MouseMotionListener mml : super.getMouseMotionListeners()) {
      super.removeMouseMotionListener(mml);
    }
    for (MouseListener ml : super.getMouseListeners()) {
      super.removeMouseListener(ml);
    }
    for (KeyListener kl : super.getKeyListeners()) {
      super.removeKeyListener(kl);
    }
  }
  
  @Override
  public void addListener(PlayerFeatures playerFeatures) {
//    throw new IllegalArgumentException("stub");
  }
  
  @Override
  public void promptMessage(String message) {
    JOptionPane.showMessageDialog(new JFrame(),
        message);
  }
  
  @Override
  public void itsTheTurnOf(PieceColor pieceColor) {
    // Stub, because provider view is not able to handle any sort of notifications about turn.
  }
}
