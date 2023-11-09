package cs3500.reversi.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.Reversi;
import cs3500.reversi.view.CartesianPosn;
import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.PlayerListener;

/**
 * A ReversiPanel will draw all the colors, allow users to play the game.
 */
public class ReversiPanel extends JPanel implements PlayerListener {
  private final IROModel model;
  private final int numRings;
  private final List<ViewFeatures> featuresListeners;
  private final PieceColor pieceColor;
  private final double hexagonRadius;
  private boolean mouseIsDown = false;

  private static final int PADDING = 50;
  private static final int HEIGHT = 800;
  private static final int WIDTH = 800;
  private CartesianPosn highlightedHex = null;

  public ReversiPanel(IROModel model, PieceColor pieceColor) {
    this.model = Objects.requireNonNull(model);
    this.numRings = this.model.getNumRings();
    this.pieceColor = pieceColor;
    this.featuresListeners = new ArrayList<>();
    this.hexagonRadius = this.computeHexagonRadius();

    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);

    this.setBackground(Color.DARK_GRAY);
  }

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
    g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
    g2d.scale(1, -1);

    this.drawBoard(g2d);

    if (this.highlightedHex != null) {
      this.makeHexagon(g2d, this.highlightedHex, Color.CYAN);
    }
  }

  private void drawBoard(Graphics2D g2d) {
    Color oldColor = g2d.getColor();

    int start = 0;
    int end = this.numRings;

    for (int r = -this.numRings; r <= this.numRings; r++) {
      for (int q = start; q <= end; q++) {
        AxialPosn ap = new AxialPosn(q, r);
        CartesianPosn p = this.findCartesianCenter(ap);

        this.makeHexagon(g2d, p, Color.LIGHT_GRAY);
        if (this.model.getPieceAt(new AxialPosn(q, r)).isPresent()) {
          this.makeCircle(g2d, p, (int) this.hexagonRadius / 2,
                  this.model.getPieceAt(ap).get().color);
        }
      }

      if (start == -this.numRings) {
        end--;
      } else {
        start--;
      }
    }

    g2d.setColor(oldColor);
  }

  private void makeCircle(Graphics2D g2d, CartesianPosn p, double r, Color c) {
    Color oldColor = g2d.getColor();
    g2d.setColor(c);

    g2d.fill(new Ellipse2D.Double(p.x - r, p.y - r, 2 * r, 2 * r));

    g2d.setColor(oldColor);
  }

  private void makeHexagon(Graphics2D g2d, CartesianPosn p, Color fillColor) {
    Color oldColor = g2d.getColor();
    g2d.setColor(fillColor);

    Path2D path  = new Path2D.Double();

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
    g2d.setStroke(new BasicStroke((float) this.hexagonRadius * 0.06f));
    g2d.draw(path);

    g2d.setColor(oldColor);
  }

  void addFeaturesListener(ViewFeatures features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null");
    }
    this.featuresListeners.add(features);
  }

  @Override
  public void itsTheMoveOf(PieceColor pieceColor) {
    if (this.pieceColor.equals(pieceColor)) {
      // PROMPT USER IT'S THEIR TURN
      this.repaint();
      throw new IllegalArgumentException("STUB");
    }
    else {
      // REMOVE THE PROMPTING OF USER
      this.repaint();
      throw new IllegalArgumentException("STUB");
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(ReversiPanel.WIDTH, ReversiPanel.HEIGHT);
  }

  CartesianPosn findCartesianCenter(AxialPosn axial) {
    double x = this.hexagonRadius * (Math.sqrt(3) * axial.q + Math.sqrt(3) / 2 * axial.r);
    double y = this.hexagonRadius * (3.0 / 2.0 * axial.r);

    // To account for float-point inaccuracy
    return new CartesianPosn(x, -y);
  }

  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      ReversiPanel.this.mouseIsDown = true;
      this.mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      ReversiPanel.this.mouseIsDown = false;
      Point physicalP = e.getPoint();
      AxialPosn axialPosn = this.convertToAxial(new CartesianPosn(physicalP.x, physicalP.y));
      try {
        if (ReversiPanel.this.model.getPieceAt(axialPosn).isEmpty()) {
          ReversiPanel.this.highlightedHex = ReversiPanel.this.findCartesianCenter(axialPosn);
        }
        else {
          ReversiPanel.this.highlightedHex = null;
        }
      } catch (IllegalArgumentException ia) {
        ReversiPanel.this.highlightedHex = null;
      }
      ReversiPanel.this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      Point physicalP = e.getPoint();
    }

    private AxialPosn convertToAxial(CartesianPosn physicalP) {
      double x = physicalP.x - (double) ReversiPanel.WIDTH / 2;
      double y = physicalP.y - (double) ReversiPanel.HEIGHT / 2;

      double q = (Math.sqrt(3)/3 * x  -  1./3 * y) / ReversiPanel.this.hexagonRadius;
      double r = (2./3 * y) / ReversiPanel.this.hexagonRadius;
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

  }
}
