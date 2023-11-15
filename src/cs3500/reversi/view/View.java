package cs3500.reversi.view;

import javax.swing.JFrame;

import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

/**
 * The View class represents a Swing-based graphical user interface for the Reversi game.
 * It extends JFrame and implements the IView interface. The view consists of a ReversiPanel that
 * displays the game board and allows user interaction.
 */
public class View extends JFrame implements IView {
  private final ReversiPanel panel;

  /**
   * Constructs a View for the Reversi game with the specified model and player color.
   *
   * @param model      The Reversi game model.
   * @param pieceColor The color of the player associated with this view.
   */
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
  public void refresh() {
    this.repaint();
  }

  @Override
  public void addFeatureListener(ViewFeatures features) {
    this.panel.addFeaturesListener(features);
  }
}
