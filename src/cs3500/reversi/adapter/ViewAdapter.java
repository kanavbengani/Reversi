package cs3500.reversi.adapter;

import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.PlayerFeatures;
import cs3500.reversi.provider.controller.ReversiController;
import cs3500.reversi.provider.model.ReadonlyReversiModel;
import cs3500.reversi.provider.view.BasicReversiFrame;
import cs3500.reversi.view.IView;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Adapter class for integrating our providers' view with our view interface.
 * This class extends BasicReversiFrame, our providers' concrete implementation
 * and implements our IView.
 */
public class ViewAdapter extends BasicReversiFrame implements IView {
  
  /**
   * Constructs a BasicReversiFrame.
   *
   * @param model the model to be used
   */
  public ViewAdapter(ReadonlyReversiModel model) {
    super(model);
    super.setKey(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pass");
    super.setKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
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
    super.subscribe((ReversiController) playerFeatures);
    super.addClickListener((ReversiController) playerFeatures);
    super.addKeyInputListener((ReversiController) playerFeatures);
    super.addFeature((ReversiController) playerFeatures);
  }
  
  @Override
  public void promptMessage(String message) {
    this.refresh();
    JOptionPane.showMessageDialog(new JFrame(),
        message);
  }
  
  @Override
  public void itsTheTurnOf(PieceColor pieceColor) {
    this.refresh();
  }
}
