package cs3500.reversi.provider.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import cs3500.reversi.provider.controller.GUIFeatures;
import cs3500.reversi.provider.controller.ReversiController;
import cs3500.reversi.provider.controller.ReversiKeyAdapter;
import cs3500.reversi.provider.controller.ReversiMouseAdapter;
import cs3500.reversi.provider.model.ReadonlyReversiModel;

import static cs3500.reversi.provider.view.HexagonPanel.HEX_SIDE_LENGTH;
import static cs3500.reversi.provider.view.HexagonPanel.HEX_SIDE_TO_SIDE_DIAMETER;

/**
 * A view for Reversi which displays the game board and provide visual interface for users.
 */
public class BasicReversiFrame extends JFrame implements ReversiView {
  private final HexagonPanel panel;
  private final List<ReversiController> controllerList = new ArrayList<>();
  protected static final List<GUIFeatures> moveList = new ArrayList<>();
  
  /**
   * Constructs a BasicReversiFrame.
   *
   * @param model the model to be used
   */
  public BasicReversiFrame(ReadonlyReversiModel model) {
    super();
    
    this.setPreferredSize(new Dimension(calculateFrameWidth(model),
            calculateFrameHeight(model)));

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    //adding the panel to the frame
    this.panel = new HexagonPanel(model);
    this.panel.setBackground(Color.DARK_GRAY);

    this.add(panel);

    this.pack();

    this.repaint();
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void makeVisible(boolean b) {
    this.setVisible(b);
  }

  @Override
  public void addClickListener(ReversiController listener) {
    this.panel.addMouseListener(new ReversiMouseAdapter(listener));
  }
  
  @Override
  public void addKeyInputListener(ReversiController listener) {
    this.panel.addKeyListener(new ReversiKeyAdapter(listener));
  }
  
  @Override
  public void addFeature(GUIFeatures move) {
    moveList.add(move);
  }
  
  @Override
  public void setKey(KeyStroke key, String move) {
    this.panel.getInputMap().put(key, move);
  }

  @Override
  public void error() {
    JOptionPane.showMessageDialog(new JFrame(),
            "Illegal move for player.");
  }

  @Override
  public void subscribe(ReversiController reversiGUIController) {
    controllerList.add(reversiGUIController);
    this.panel.subscribe(reversiGUIController);
  }

  @Override
  public void gameOver(cs3500.reversi.provider.model.Color color, int score) {
    JOptionPane.showMessageDialog(new JFrame(),
            "Game over. Score for player " + color + ": " + score);
  }

  private int calculateFrameWidth(ReadonlyReversiModel model) {
    return (int) (HEX_SIDE_TO_SIDE_DIAMETER * model.getSize() + HEX_SIDE_TO_SIDE_DIAMETER * 1 / 6);
  }

  private int calculateFrameHeight(ReadonlyReversiModel model) {
    return HEX_SIDE_LENGTH * (model.getNumRows())
                    + HEX_SIDE_LENGTH * ((model.getNumRows() + 1) / 2)
                    + HEX_SIDE_LENGTH * 3 / 4;
  }
}