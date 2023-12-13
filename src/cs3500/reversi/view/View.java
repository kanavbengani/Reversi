package cs3500.reversi.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.PlayerFeatures;

/**
 * The View class represents a Swing-based graphical user interface for the Reversi game.
 * It extends JFrame and implements the IView interface. The view consists of a HexPanel that
 * displays the game board and allows user interaction.
 */
public class View extends JFrame implements IView {
  private final AbstractPanel panel;
  
  /**
   * Constructs a View for the Reversi game with the specified panel.
   */
  public View(ViewType vt, IROModel model, PieceColor pieceColor) {
    super("Reversi");
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    switch (vt) {
      case SQUARE:
        this.panel = new SquarePanel(model, pieceColor);
        break;
      case HEX:
        this.panel = new HexPanel(model, pieceColor);
        break;
      default:
        throw new IllegalArgumentException("Invalid view type.");
    }
    
    this.add(this.panel);
    
    this.setResizable(false);
    
    this.pack();
    this.display(true);
  }
  
  @Override
  public void display(boolean b) {
    this.setVisible(b);
  }
  
  @Override
  public void disableInput() {
    this.panel.disableInput();
  }
  
  @Override
  public void refresh() {
    this.repaint();
  }
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.panel.addFeaturesListener(features);
  }
  
  @Override
  public void promptMessage(String message) {
    if (this.isVisible()) {
      JOptionPane.showMessageDialog(this.panel, message);
    }
  }
  
  @Override
  public void itsTheTurnOf(PieceColor pieceColor) {
    this.panel.itsTheTurnOf(pieceColor);
  }
}
