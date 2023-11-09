package cs3500.reversi.view;
;

import javax.swing.JFrame;

import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

public class View extends JFrame implements IView {
  private final ReversiPanel panel;

  public View(IROModel model, PieceColor pieceColor) {
    super("Reversi: " + pieceColor.name() + " Player");

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new ReversiPanel(model, pieceColor);
    this.add(panel);

    this.pack();
  }

  @Override
  public void display(boolean b) {
    this.setVisible(b);
  }

  @Override
  public void addFeatureListener(ViewFeatures features) {
    this.panel.addFeaturesListener(features);
  }
}
