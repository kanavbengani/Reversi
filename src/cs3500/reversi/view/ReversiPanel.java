package cs3500.reversi.view;

import java.awt.BasicStroke;
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
import java.awt.geom.Path2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.model.*;
import cs3500.reversi.model.Posn;
import cs3500.reversi.player.PlayerFeatures;

/**
 * A ReversiPanel is a JPanel that draws the Reversi game board and allows users to interact with
 * the game. It implements ModelFeatures to receive updates from the game model and repaints
 * itself accordingly.
 */
class ReversiPanel extends JPanel {
  private static final int PADDING = 10;
  private static final int HEIGHT = 700;
  private static final int WIDTH = 700;
  private final IROModel model;
  private final int numRings;
  private final MouseAdapter mouse;
  private final KeyListener keyboard;
  private final List<PlayerFeatures> featuresListeners = new ArrayList<>();
  private final PieceColor pieceColor;
  private final double hexagonRadius;
  private Optional<Posn> highlightedHex = Optional.empty();
  private boolean isMyMove = false;

  /**
   * Constructs a ReversiPanel with the specified Reversi game model and player color.
   *
   * @param model      The Reversi game model.
   * @param pieceColor The color of the player using this panel.
   */
  ReversiPanel(IROModel model, PieceColor pieceColor) {
    this.model = Objects.requireNonNull(model);
    this.numRings = this.model.getNumRings();
    this.pieceColor = pieceColor;
    this.hexagonRadius = this.computeHexagonRadius();
    
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

  // Converts a given physical point assuming the origin is in the middle of the screen into a
  // logical coordinate (in axial).
  private double computeHexagonRadius() {
    double drawableWidth = ReversiPanel.WIDTH - 2 * ReversiPanel.PADDING;
    double drawableHeight = ReversiPanel.HEIGHT - 2 * ReversiPanel.PADDING;

    double horizontalMaxRadius =
            drawableWidth / (2 * Math.cos(Math.toRadians(30)) * (2 * this.numRings + 1));
    double verticalMaxRadius = drawableHeight / (2 * (this.numRings + 1));

    return Math.min(horizontalMaxRadius, verticalMaxRadius);
  }
  
  // Helps in testing the view mouse input triggers the correct events.
  MouseAdapter getMouseAdapter() {
    return this.mouse;
  }
  
  // Helps in testing the view keyboard input triggers the correct events.
  KeyListener getKeyListener() {
    return this.keyboard;
  }

  /**
   * Disables all mouse and keyboard input.
   */
  void disableInput() {
    this.removeMouseListener(this.mouse);
    this.removeMouseMotionListener(this.mouse);
    this.removeKeyListener(this.keyboard);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    // Invert coordinates so origin is in the middle and +y is upwards and +x is to the right.
    g2d.translate(WIDTH / 2, HEIGHT / 2);
    g2d.scale(1, -1);
    
    this.drawBoard(g2d);
    this.displayTurn(g2d);

    if (this.highlightedHex.isPresent()) {
      Posn posn = this.highlightedHex.get();
      Color color;
      int howManyCaptured;

      if (this.model.isMoveValid(this.pieceColor, posn)) {
        color = Color.GREEN;
        howManyCaptured = this.model.getAllCapturedPieces(this.pieceColor,
            this.highlightedHex.get()).size();
      } else {
        color = Color.RED;
        howManyCaptured = 0;
      }
      this.makeHexagon(g2d, this.transformLogicalToPhysical(posn), color);
      this.writeHowManyCaptured(g2d, this.transformLogicalToPhysical(posn),
          howManyCaptured);
    }
  }

  // Draws the board with the given Graphics2D object.
  private void drawBoard(Graphics2D g2d) {
    Color oldColor = g2d.getColor();

    for (Posn posn : this.model.getAllPosn()) {
      CartesianPosn p = this.transformLogicalToPhysical(posn);

      this.makeHexagon(g2d, p, Color.LIGHT_GRAY);
      if (this.model.getPieceAt(posn).isPresent()) {
        this.makeCircle(g2d, p, this.hexagonRadius / 2,
                this.model.getPieceAt(posn).get().color);
      }
    }

    g2d.setColor(oldColor);
  }

  // Displays the turn message with the given Graphics2D object.
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
    
    int textWidth = g2d.getFontMetrics().stringWidth(String.valueOf("It is your turn!")) / 2;
    
    g2d.drawString("It is your turn!", -textWidth, -ReversiPanel.HEIGHT / 2 + 40);
    
    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);
  }

  // Creates a circle with the given center (in cartesian coordinates) and radius with the given
  // Graphics2D object.
  private void makeCircle(Graphics2D g2d, CartesianPosn p, double r, Color c) {
    Color oldColor = g2d.getColor();
    g2d.setColor(c);

    g2d.fill(new Ellipse2D.Double(p.x - r, p.y - r, 2 * r, 2 * r));

    g2d.setColor(oldColor);
  }

  // Creates a hexagon with the given center (in cartesian coordinates) with the given
  // Graphics2D object.
  private void makeHexagon(Graphics2D g2d, CartesianPosn p, Color fillColor) {
    Color oldColor = g2d.getColor();
    g2d.setColor(fillColor);

    Path2D path = new Path2D.Double();

    double startX = p.x + hexagonRadius * Math.cos(Math.PI / 180 * 30);
    double startY = p.y + hexagonRadius * Math.sin(Math.PI / 180 * 30);

    path.moveTo(startX, startY);

    for (int i = 1; i < 7; i++) {
      int angle_deg = 60 * i + 30;
      double angle_rad = Math.PI / 180 * angle_deg;
      double xVal = (p.x + hexagonRadius * Math.cos(angle_rad));
      double yVal = (p.y + hexagonRadius * Math.sin(angle_rad));
      path.lineTo(xVal, yVal);
    }

    path.closePath();
    g2d.fill(path);
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke((float) this.hexagonRadius * 0.02f));
    g2d.draw(path);

    g2d.setColor(oldColor);
  }

  // Displays how many pieces will be captured if the player plays the given move.
  private void writeHowManyCaptured(Graphics2D g2d, CartesianPosn p, int numCaptures) {
    Color oldColor = g2d.getColor();
    AffineTransform oldTransform = g2d.getTransform();

    g2d.setColor(Color.BLACK);

    AffineTransform verticalFlip = AffineTransform.getScaleInstance(1, -1);
    verticalFlip.translate(0, -p.y * 2);
    g2d.transform(verticalFlip);

    int fontSize = 24;
    g2d.setFont(g2d.getFont().deriveFont((float) fontSize));

    // Calculate the center of the hexagon
    double hexagonCenterX = p.x;
    double hexagonCenterY = p.y;

    // Calculate the position to center the text within the hexagon
    int textWidth = g2d.getFontMetrics().stringWidth(String.valueOf(numCaptures));
    int textHeight = g2d.getFontMetrics().getHeight();
    int xText = (int) (hexagonCenterX - textWidth / 2);
    int yText = (int) (hexagonCenterY + textHeight / 4); // Adjust based on font metrics

    g2d.drawString(String.valueOf(numCaptures), xText, yText);

    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);
  }

  // Adds the passed in PlayerFeatures as a listener
  void addFeaturesListener(PlayerFeatures features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null");
    }
    this.featuresListeners.add(features);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(ReversiPanel.WIDTH, ReversiPanel.HEIGHT);
  }

  // Converts a given physical point assuming the origin is in the middle of the screen into a
  // logical coordinate (in axial).
  private Posn transformPhysicalToLogical(CartesianPosn physicalP) {
    double x = physicalP.x;
    double y = physicalP.y;

    double q = (Math.sqrt(3) / 3 * x - 1. / 3 * y) / this.hexagonRadius;
    double r = (2. / 3 * y) / this.hexagonRadius;
    double s = -q - r;

    double qRounded = Math.round(q);
    double rRounded = Math.round(r);
    double sRounded = Math.round(-q - r);

    double q_diff = Math.abs(qRounded - q);
    double r_diff = Math.abs(rRounded - r);
    double s_diff = Math.abs(sRounded - s);

    if (q_diff > r_diff && q_diff > s_diff) {
      qRounded = -rRounded - sRounded;
    } else if (r_diff > s_diff) {
      rRounded = -qRounded - sRounded;
    }

    return new HexPosn((int) qRounded, (int) rRounded);
  }

  // Transforms logical axial coordinates to the cartesian coordinate of the center of the
  // hexagon in the view.
  private CartesianPosn transformLogicalToPhysical(Posn axial) {
    double x = this.hexagonRadius * (Math.sqrt(3) * axial.getSecond() + Math.sqrt(3) / 2 * axial.getFirst());
    double y = this.hexagonRadius * (3.0 / 2.0 * axial.getFirst());

    return new CartesianPosn(x, -y);
  }

  /**
   * Notifies the player it is their turn by updating this panel.
   *
   * @param pieceColor the piece color of the player's turn
   */
  void itsTheTurnOf(PieceColor pieceColor) {
    this.isMyMove = this.pieceColor.equals(pieceColor);
    this.repaint();
  }
  
  // Represents the KeyboardEventListener that parses input from a keyboard stroke and performs
  // action to the view/System.out accordingly.
  private class KeyboardEventListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_P) {
        for (PlayerFeatures l : ReversiPanel.this.featuresListeners) {
          ReversiPanel.this.highlightedHex = Optional.empty();
          ReversiPanel.this.repaint();
          l.pass();
        }
        ReversiPanel.this.highlightedHex = Optional.empty();
      }
      if (e.getKeyCode() == KeyEvent.VK_ENTER && ReversiPanel.this.highlightedHex.isPresent()) {
        for (PlayerFeatures l : ReversiPanel.this.featuresListeners) {
          Posn tempHex = ReversiPanel.this.highlightedHex.get();
          ReversiPanel.this.highlightedHex = Optional.empty();
          ReversiPanel.this.repaint();
          l.move(tempHex);
        }
        ReversiPanel.this.highlightedHex = Optional.empty();
      }
    }
  }

  // Represents the MouseEventsListener that parses input from a mouse click and performs action
  // to the view/System.out accordingly.
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseReleased(MouseEvent e) {
      Point physicalP = e.getPoint();
      physicalP.x -= ReversiPanel.WIDTH / 2;
      physicalP.y -= ReversiPanel.HEIGHT / 2;

      Posn hexPosn =
              ReversiPanel.this.transformPhysicalToLogical(new CartesianPosn(physicalP));

      // Showing axial coordinate that has been clicked to System.out
      try {
        Optional<PieceColor> pieceColor = ReversiPanel.this.model.getPieceAt(hexPosn);

        // Highlight/De-highlight logic
        if (pieceColor.isEmpty()) {
          if (ReversiPanel.this.highlightedHex.isPresent()
                  && hexPosn.equals(ReversiPanel.this.highlightedHex.get())) {
            throw new IllegalArgumentException("Cell is already highlighted.");
          }
          ReversiPanel.this.highlightedHex =
                  Optional.of(hexPosn);
        } else {
          throw new IllegalArgumentException("There is already a chip there.");
        }
      } catch (IllegalArgumentException ia) {
        ReversiPanel.this.highlightedHex = Optional.empty();
      }
      ReversiPanel.this.repaint();
    }
  }
}
