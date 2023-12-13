package cs3500.reversi.view;

import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.model.Posn;
import cs3500.reversi.model.square.SquarePosn;

import java.awt.*;
import java.awt.geom.Path2D;

// Represents a panel for a square Reversi board.
class SquarePanel extends AbstractPanel {
  /**
   * Constructs a AbstractPanel with the specified Reversi game model and player color.
   *
   * @param model      The Reversi game model.
   * @param pieceColor The color of the player using this panel.
   */
  SquarePanel(IROModel model, PieceColor pieceColor) {
    super(model, pieceColor);
  }
  
  @Override
  protected double computeCellRadius() {
    double drawableWidth = AbstractPanel.WIDTH - 2 * AbstractPanel.PADDING;
    double drawableHeight = AbstractPanel.HEIGHT - 2 * AbstractPanel.PADDING;
    
    double horizontalMaxRadius = drawableWidth / (2 * this.numRings);
    double verticalMaxRadius = drawableHeight / (2 * this.numRings);
    
    return Math.min(horizontalMaxRadius, verticalMaxRadius) / 2;
  }
  
  @Override
  protected void makeCell(Graphics2D g2d, Point p, Color fillColor) {
    Color oldColor = g2d.getColor();
    g2d.setColor(fillColor);
    
    Path2D path = new Path2D.Double();
    
    double startX = p.x + super.cellRadius;
    double startY = p.y - super.cellRadius;
    
    path.moveTo(startX, startY);
    path.lineTo(startX, startY + 2 * super.cellRadius);
    path.lineTo(startX - 2 * super.cellRadius, startY + 2 * super.cellRadius);
    path.lineTo(startX - 2 * super.cellRadius, startY);
    path.lineTo(startX, startY);
    
    path.closePath();
    g2d.fill(path);
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke((float) super.cellRadius * 0.02f));
    g2d.draw(path);
    
    g2d.setColor(oldColor);
  }
  
  @Override
  protected Posn transformPhysicalToLogical(Point physicalP) {
    double x = 1 + Math.floor((double) physicalP.x / super.cellRadius / 2);
    double y = 1 + Math.floor((double) physicalP.y / super.cellRadius / 2);
    
    return new SquarePosn((int) x, (int) y);
  }

  @Override
  protected Point transformLogicalToPhysical(Posn posn) {
    return new Point(posn.getFirstCoordinate() * (int) super.cellRadius * 2 - (int) super.cellRadius,
        -posn.getSecondCoordinate() * (int) super.cellRadius * 2 + (int) super.cellRadius);
  }
}
