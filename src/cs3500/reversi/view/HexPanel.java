package cs3500.reversi.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import java.awt.geom.Path2D;

import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.model.Posn;
import cs3500.reversi.model.hex.HexPosn;

/**
 * A HexPanel is a JPanel that draws the Reversi game board and allows users to interact with
 * the game. It implements ModelFeatures to receive updates from the game model and repaints
 * itself accordingly.
 */
class HexPanel extends AbstractPanel {

  /**
   * Constructs a HexPanel with the specified Reversi game model and player color.
   *
   * @param model      The Reversi game model.
   * @param pieceColor The color of the player using this panel.
   */
  HexPanel(IROModel model, PieceColor pieceColor) {
    super(model, pieceColor);
  }
  
  @Override
  protected double computeCellRadius() {
    double drawableWidth = AbstractPanel.WIDTH - 2 * AbstractPanel.PADDING;
    double drawableHeight = AbstractPanel.HEIGHT - 2 * AbstractPanel.PADDING;
    
    double horizontalMaxRadius =
        drawableWidth / (2 * Math.cos(Math.toRadians(30)) * (2 * this.numRings + 1));
    double verticalMaxRadius = drawableHeight / (2 * (this.numRings + 1));
    
    return Math.min(horizontalMaxRadius, verticalMaxRadius);
  }
  
  @Override
  protected void makeCell(Graphics2D g2d, Point p, Color fillColor) {
    Color oldColor = g2d.getColor();
    g2d.setColor(fillColor);

    Path2D path = new Path2D.Double();

    double startX = p.x + super.cellRadius * Math.cos(Math.PI / 180 * 30);
    double startY = p.y + super.cellRadius * Math.sin(Math.PI / 180 * 30);

    path.moveTo(startX, startY);

    for (int i = 1; i < 7; i++) {
      int angle_deg = 60 * i + 30;
      double angle_rad = Math.PI / 180 * angle_deg;
      double xVal = (p.x + super.cellRadius * Math.cos(angle_rad));
      double yVal = (p.y + super.cellRadius * Math.sin(angle_rad));
      path.lineTo(xVal, yVal);
    }

    path.closePath();
    g2d.fill(path);
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke((float) super.cellRadius * 0.02f));
    g2d.draw(path);

    g2d.setColor(oldColor);
  }

  // Converts a given physical point assuming the origin is in the middle of the screen into a
  // logical coordinate (in axial).
  @Override
  protected Posn transformPhysicalToLogical(Point physicalP) {
    double x = physicalP.x;
    double y = physicalP.y;

    double q = (Math.sqrt(3) / 3 * x - 1. / 3 * y) / super.cellRadius;
    double r = (2. / 3 * y) / super.cellRadius;
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

  // Transforms logical posn coordinates to the cartesian coordinate of the center of the
  // hexagon in the view.
  @Override
  protected Point transformLogicalToPhysical(Posn posn) {
    double x = super.cellRadius
        * (Math.sqrt(3) * posn.getFirstCoordinate() + Math.sqrt(3) / 2
        * posn.getSecondCoordinate());
    double y = super.cellRadius * (3.0 / 2.0 * posn.getSecondCoordinate());

    return new Point((int) x, (int) -y);
  }
}
