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

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.PieceColor;

/**
 * A ReversiPanel is a JPanel that draws the Reversi game board and allows users to interact with
 * the game. It implements ModelFeatures to receive updates from the game model and repaints
 * itself accordingly.
 */
class ReversiPanel extends JPanel implements ModelFeatures {
  private static final int PADDING = 10;
  private static final int HEIGHT = 800;
  private static final int WIDTH = 800;

  private final IROModel model;
  private final int numRings;
  private final List<ViewFeatures> featuresListeners = new ArrayList<>();
  private final PieceColor pieceColor;
  private final double hexagonRadius;
  private Optional<AxialPosn> highlightedHex = Optional.empty();

  /**
   * Constructs a ReversiPanel with the specified Reversi game model and player color.
   *
   * @param model      The Reversi game model.
   * @param pieceColor The color of the player using this panel.
   */
  public ReversiPanel(IROModel model, PieceColor pieceColor) {
    this.model = Objects.requireNonNull(model);
    this.numRings = this.model.getNumRings();
    this.pieceColor = pieceColor;
    this.hexagonRadius = this.computeHexagonRadius();

    // adds this as a listener to the model.
    this.model.addListener(this);

    // adds mouse and key listeners
    MouseAdapter mouse = new MouseEventsListener();
    this.addMouseListener(mouse);
    this.addMouseMotionListener(mouse);
    KeyListener keyboard = new ReversiPanel.KeyboardEventListener();
    this.addKeyListener(keyboard);
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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    // Invert coordinates so origin is in the middle and +y is upwards and +x is to the right.
    g2d.translate(WIDTH / 2, HEIGHT / 2);
    g2d.scale(1, -1);

    this.drawBoard(g2d);

    if (this.highlightedHex.isPresent()) {
      AxialPosn posn = this.highlightedHex.get();
      Color color;

      if (this.model.isMoveValid(this.pieceColor, posn)) {
        color = Color.GREEN;
        this.makeHexagon(g2d, this.transformLogicalToPhysical(posn), color);
        this.writeHowManyCaptured(g2d, this.transformLogicalToPhysical(posn),
                this.model.getAllCapturedPieces(this.pieceColor, this.highlightedHex.get()).size());
      } else {
        color = Color.RED;
        this.makeHexagon(g2d, this.transformLogicalToPhysical(posn), color);
      }
    }
  }

  // Draws the board with the given Graphics2D object.
  private void drawBoard(Graphics2D g2d) {
    Color oldColor = g2d.getColor();

    for (AxialPosn axialPosn : this.model.getAllPosn()) {
      CartesianPosn p = this.transformLogicalToPhysical(axialPosn);

      this.makeHexagon(g2d, p, Color.LIGHT_GRAY);
      if (this.model.getPieceAt(axialPosn).isPresent()) {
        this.makeCircle(g2d, p, this.hexagonRadius / 2,
                this.model.getPieceAt(axialPosn).get().color);
      }
    }

    g2d.setColor(oldColor);
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

  void addFeaturesListener(ViewFeatures features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null");
    }
    this.featuresListeners.add(features);
  }

  @Override
  public void itsTheMoveOf(PieceColor pieceColor) {
    this.repaint();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(ReversiPanel.WIDTH, ReversiPanel.HEIGHT);
  }

  // Converts a given physical point assuming the origin is in the middle of the screen into a
  // logical coordinate (in axial).
  private AxialPosn transformPhysicalToLogical(CartesianPosn physicalP) {
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

    return new AxialPosn((int) qRounded, (int) rRounded);
  }

  // Transforms logical axial coordinates to the cartesian coordinate of the center of the
  // hexagon in the view.
  private CartesianPosn transformLogicalToPhysical(AxialPosn axial) {
    double x = this.hexagonRadius * (Math.sqrt(3) * axial.q + Math.sqrt(3) / 2 * axial.r);
    double y = this.hexagonRadius * (3.0 / 2.0 * axial.r);

    return new CartesianPosn(x, -y);
  }

  // Represents the KeyboardEventListener that parses input from a keyboard stroke and performs
  // action to the view/System.out accordingly.
  class KeyboardEventListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_P) {
        System.out.println(ReversiPanel.this.pieceColor + " wants to pass.");
        for (ViewFeatures l : ReversiPanel.this.featuresListeners) {
          l.pass(ReversiPanel.this.pieceColor);
        }
      }
      if (e.getKeyCode() == KeyEvent.VK_ENTER && ReversiPanel.this.highlightedHex.isPresent()) {
        System.out.println(ReversiPanel.this.pieceColor + " wants to play a move at "
                + ReversiPanel.this.highlightedHex.get() + ".");
        for (ViewFeatures l : ReversiPanel.this.featuresListeners) {
          l.move(ReversiPanel.this.pieceColor, ReversiPanel.this.highlightedHex.get());
        }
      }
    }
  }

  // Represents the MouseEventsListener that parses input from a mouse click and performs action
  // to the view/System.out accordingly.
  class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseReleased(MouseEvent e) {
      Point physicalP = e.getPoint();
      physicalP.x -= ReversiPanel.WIDTH / 2;
      physicalP.y -= ReversiPanel.HEIGHT / 2;

      AxialPosn axialPosn =
              ReversiPanel.this.transformPhysicalToLogical(new CartesianPosn(physicalP));

      // Showing axial coordinate that has been clicked to System.out
      try {
        Optional<PieceColor> pieceColor = ReversiPanel.this.model.getPieceAt(axialPosn);
        System.out.println(ReversiPanel.this.pieceColor + " selected " + axialPosn + ".");

        // Highlight/De-highlight logic
        if (pieceColor.isEmpty()) {
          if (ReversiPanel.this.highlightedHex.isPresent()
                  && axialPosn.equals(ReversiPanel.this.highlightedHex.get())) {
            throw new IllegalArgumentException("Cell is already highlighted.");
          }
          ReversiPanel.this.highlightedHex =
                  Optional.of(axialPosn);
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
