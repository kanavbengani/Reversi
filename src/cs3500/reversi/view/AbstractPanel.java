package cs3500.reversi.view;

import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.model.Posn;
import cs3500.reversi.player.PlayerFeatures;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A AbstractPanel is a JPanel that draws the Reversi game board and allows users to interact with
 * the game. It implements ModelFeatures to receive updates from the game model and repaints
 * itself accordingly.
 */
abstract class AbstractPanel extends JPanel {
  protected static final int PADDING = 50;
  protected static final int HEIGHT = 700;
  protected static final int WIDTH = 700;
  protected final IROModel model;
  protected final int numRings;
  protected final MouseAdapter mouse;
  protected final KeyListener keyboard;
  protected final List<PlayerFeatures> featuresListeners = new ArrayList<>();
  protected final PieceColor pieceColor;
  protected final double cellRadius;
  protected Optional<Posn> highlightedCell = Optional.empty();
  protected boolean isMyMove = false;
  protected boolean hintsOn = false;
  
  /**
   * Constructs a AbstractPanel with the specified Reversi game model and player color.
   *
   * @param model      The Reversi game model.
   * @param pieceColor The color of the player using this panel.
   */
  protected AbstractPanel(IROModel model, PieceColor pieceColor) {
    this.model = Objects.requireNonNull(model);
    this.numRings = this.model.getNumRings();
    this.pieceColor = pieceColor;
    this.cellRadius = this.computeCellRadius();
    
    // Adds mouse and key listeners
    this.mouse = new MouseEventsListener();
    this.addMouseListener(this.mouse);
    this.addMouseMotionListener(this.mouse);
    this.keyboard = new KeyboardEventListener();
    this.addKeyListener(this.keyboard);
    this.setFocusable(true);
    this.requestFocus();
    
    this.setBackground(Color.DARK_GRAY);
  }
  
  // Helps in testing the view mouse input triggers the correct events.
  protected MouseAdapter getMouseAdapter() {
    return this.mouse;
  }
  
  // Helps in testing the view keyboard input triggers the correct events.
  protected KeyListener getKeyListener() {
    return this.keyboard;
  }
  
  /**
   * Disables all mouse and keyboard input.
   */
  protected void disableInput() {
    this.removeMouseListener(this.mouse);
    this.removeMouseMotionListener(this.mouse);
    this.removeKeyListener(this.keyboard);
  }
  
  // Computes the cell radius.
  protected abstract double computeCellRadius();
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    
    // Invert coordinates so origin is in the middle and +y is upwards and +x is to the right.
    g2d.translate(WIDTH / 2, HEIGHT / 2);
    g2d.scale(1, -1);
    
    this.drawBoard(g2d);
    this.displayTurn(g2d);
    this.displayPlayerColor(g2d);
    this.displayHints(g2d);
  }
  
  // Draws the board with the given Graphics2D object.
  private void drawBoard(Graphics2D g2d) {
    Color oldColor = g2d.getColor();
    
    for (Posn posn : this.model.getAllPosn()) {
      Point p = this.transformLogicalToPhysical(posn);
      
      this.makeCell(g2d, p, Color.LIGHT_GRAY);
      if (this.model.getPieceAt(posn).isPresent()) {
        this.makeCircle(g2d, p, this.cellRadius / 2,
            this.model.getPieceAt(posn).get().color);
      }
    }
    
    g2d.setColor(oldColor);
  }
  
  // Displays text message along y-axis with the given Graphics2D object.
  private void displayTurn(Graphics2D g2d) {
    if (!this.isMyMove || this.model.isGameOver()) {
      return;
    }
    Color oldColor = g2d.getColor();
    AffineTransform oldTransform = g2d.getTransform();
    
    g2d.setColor(Color.WHITE);
    
    int fontSize = 24;
    g2d.setFont(g2d.getFont().deriveFont((float) fontSize));
    
    AffineTransform verticalFlip = AffineTransform.getScaleInstance(1, -1);
    g2d.transform(verticalFlip);
    
    int textWidth = g2d.getFontMetrics().stringWidth("It is your turn!") / 2;
    
    g2d.drawString("It is your turn!", -textWidth, -310);
    
    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);
  }
  
  private void displayPlayerColor(Graphics2D g2d) {
    Color oldColor = g2d.getColor();
    AffineTransform oldTransform = g2d.getTransform();
    
    g2d.setColor(Color.WHITE);
    
    int fontSize = 24;
    g2d.setFont(g2d.getFont().deriveFont((float) fontSize));
    
    AffineTransform verticalFlip = AffineTransform.getScaleInstance(1, -1);
    g2d.transform(verticalFlip);
    
    int textWidth = g2d.getFontMetrics().stringWidth("Player: " + pieceColor.name()) / 2;
    
    g2d.drawString("Player: " + pieceColor.name(), -textWidth, AbstractPanel.HEIGHT / 2 - 20);
    
    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);
  }
  
  private void displayHints(Graphics2D g2d) {
    if (this.highlightedCell.isPresent()) {
      Posn posn = this.highlightedCell.get();
      Color color;
      int howManyCaptured;
      
      if (this.hintsOn) {
        if (this.model.isMoveValid(this.pieceColor, posn)) {
          color = Color.GREEN;
          howManyCaptured = this.model.getAllCapturedPieces(this.pieceColor,
              this.highlightedCell.get()).size();
        } else {
          color = Color.RED;
          howManyCaptured = 0;
        }
        this.makeCell(g2d, this.transformLogicalToPhysical(posn), color);
        this.writeHowManyCaptured(g2d, this.transformLogicalToPhysical(posn),
            howManyCaptured);
      }
      else {
        this.makeCell(g2d, this.transformLogicalToPhysical(posn), Color.CYAN);
      }
    }
  }
  
  // Creates a circle with the given center (in cartesian coordinates) and radius with the given
  // Graphics2D object.
  protected void makeCircle(Graphics2D g2d, Point p, double r, Color c) {
    Color oldColor = g2d.getColor();
    g2d.setColor(c);
    
    g2d.fill(new Ellipse2D.Double(p.x - r, p.y - r, 2 * r, 2 * r));
    
    g2d.setColor(oldColor);
  }
  
  // Creates a cell with the given center (in cartesian coordinates) with the given
  // Graphics2D object.
  protected abstract void makeCell(Graphics2D g2d, Point p, Color fillColor);
  
  // Displays how many pieces will be captured if the player plays the given move.
  protected void writeHowManyCaptured(Graphics2D g2d, Point p, int numCaptures) {
    Color oldColor = g2d.getColor();
    AffineTransform oldTransform = g2d.getTransform();
    
    g2d.setColor(Color.BLACK);
    
    AffineTransform verticalFlip = AffineTransform.getScaleInstance(1, -1);
    verticalFlip.translate(0, -p.y * 2);
    g2d.transform(verticalFlip);
    
    int fontSize = 24;
    g2d.setFont(g2d.getFont().deriveFont((float) fontSize));
    
    // Calculate the center of the cell
    double centerX = p.x;
    double centerY = p.y;
    
    // Calculate the position to center the text within the cell
    int textWidth = g2d.getFontMetrics().stringWidth(String.valueOf(numCaptures));
    int textHeight = g2d.getFontMetrics().getHeight();
    int xText = (int) (centerX - textWidth / 2);
    int yText = (int) (centerY + textHeight / 4); // Adjust based on font metrics
    
    g2d.drawString(String.valueOf(numCaptures), xText, yText);
    
    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);
  }
  
  // Adds the passed in PlayerFeatures as a listener
  protected void addFeaturesListener(PlayerFeatures features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null");
    }
    this.featuresListeners.add(features);
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(AbstractPanel.WIDTH, AbstractPanel.HEIGHT);
  }
  
  // Converts a given physical point assuming the origin is in the middle of the screen into a
  // logical coordinate (in axial).
  protected abstract Posn transformPhysicalToLogical(Point physicalP);
  
  // Transforms logical axial coordinates to the cartesian coordinate of the center of the
  // cell in the view.
  protected abstract Point transformLogicalToPhysical(Posn axial);
  
  /**
   * Notifies the player it is their turn by updating this panel.
   *
   * @param pieceColor the piece color of the player's turn
   */
  protected void itsTheTurnOf(PieceColor pieceColor) {
    this.isMyMove = this.pieceColor.equals(pieceColor);
    this.repaint();
  }
  
  // Represents the KeyboardEventListener that parses input from a keyboard stroke and performs
  // action to the view/System.out accordingly.
  protected class KeyboardEventListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_P) {
        for (PlayerFeatures l : AbstractPanel.this.featuresListeners) {
          AbstractPanel.this.highlightedCell = Optional.empty();
          AbstractPanel.this.repaint();
          l.pass();
        }
        AbstractPanel.this.highlightedCell = Optional.empty();
      }
      if (e.getKeyCode() == KeyEvent.VK_ENTER && AbstractPanel.this.highlightedCell.isPresent()) {
        for (PlayerFeatures l : AbstractPanel.this.featuresListeners) {
          Posn tempCell = AbstractPanel.this.highlightedCell.get();
          AbstractPanel.this.highlightedCell = Optional.empty();
          AbstractPanel.this.repaint();
          l.move(tempCell);
        }
        AbstractPanel.this.highlightedCell = Optional.empty();
      }
      if (e.getKeyCode() == KeyEvent.VK_H) {
        AbstractPanel.this.hintsOn = !AbstractPanel.this.hintsOn;
        AbstractPanel.this.repaint();
      }
    }
  }
  
  // Represents the MouseEventsListener that parses input from a mouse click and performs action
  // to the view/System.out accordingly.
  protected class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseReleased(MouseEvent e) {
      Point physicalP = e.getPoint();
      physicalP.x -= AbstractPanel.WIDTH / 2;
      physicalP.y -= AbstractPanel.HEIGHT / 2;
      
      Posn cellPosn =
          AbstractPanel.this.transformPhysicalToLogical(new Point(physicalP));
      
      // Showing axial coordinate that has been clicked to System.out
      try {
        Optional<PieceColor> pieceColor = AbstractPanel.this.model.getPieceAt(cellPosn);
        
        // Highlight/De-highlight logic
        if (pieceColor.isEmpty()) {
          if (AbstractPanel.this.highlightedCell.isPresent()
              && cellPosn.equals(AbstractPanel.this.highlightedCell.get())) {
            throw new IllegalArgumentException("Cell is already highlighted.");
          }
          AbstractPanel.this.highlightedCell =
              Optional.of(cellPosn);
        } else {
          throw new IllegalArgumentException("There is already a chip there.");
        }
      } catch (IllegalArgumentException ia) {
        AbstractPanel.this.highlightedCell = Optional.empty();
      }
      AbstractPanel.this.repaint();
    }
  }
}
