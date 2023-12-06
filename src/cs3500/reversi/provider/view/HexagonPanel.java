package cs3500.reversi.provider.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.provider.controller.GUIFeatures;
import cs3500.reversi.provider.controller.ReversiController;
import cs3500.reversi.provider.model.Color;
import cs3500.reversi.provider.model.ReadonlyReversiModel;

/**
 * A HexagonPanel will draw all the hexagons, allow users to click on them,
 * and play the game.
 */
public class HexagonPanel extends JPanel {
  private final ReadonlyReversiModel model;
  ReversiController listener;
  //  private final List<ViewFeatures> featuresListeners;

  //the size of the hexagon from top vertices to bottom
  public static int HEX_SIDE_LENGTH = 50;
  public static double HEX_SIDE_TO_SIDE_DIAMETER = HEX_SIDE_LENGTH * Math.sqrt(3);
  private Map<Point2D, Hexagon> coordinates;
  private Map<Hexagon, int[]> logicalCoordinates;
  private int[] selectedHexagon;

  /**
   * Constructs a HexagonPanel.
   *
   * @param model the model to be used
   */
  public HexagonPanel(ReadonlyReversiModel model) {
    super();
    this.model = model;
    this.coordinates = new HashMap<>();
    this.logicalCoordinates = new HashMap<>();
    MouseEventsListener listener = new MouseEventsListener();
    KeyEventsListener keyListener = new KeyEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    this.addKeyListener(keyListener);
  }

  public void subscribe(ReversiController listener) {
    this.listener = listener;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    drawGrid(g2d);
  }


  private void drawGrid(Graphics2D g2d) {
    Color[][] board = model.copyBoard();
    Rectangle bounds = this.getBounds();
    double minLength = Math.min(bounds.width, bounds.height);

    HEX_SIDE_LENGTH = (int) (minLength / model.getSize() / 2);
    HEX_SIDE_TO_SIDE_DIAMETER = HEX_SIDE_LENGTH * Math.sqrt(3);
    coordinates = new HashMap<>();
    logicalCoordinates = new HashMap<>();

    for (int row = 0; row < model.getNumRows(); row++) {
      Color[] currentRow = model.getRow(row);
      for (int col = 0; col < currentRow.length; col++) {
        int currentStartingCoord = updateStartingCoord(currentRow.length);
        double xPos = HEX_SIDE_TO_SIDE_DIAMETER / 2 + currentStartingCoord
                + col * (HEX_SIDE_TO_SIDE_DIAMETER);
        double yPos = HEX_SIDE_LENGTH + (row * HEX_SIDE_LENGTH * 3 / 2);

        Hexagon hexagon = new Hexagon(xPos, yPos, HEX_SIDE_LENGTH);
        coordinates.put(new Point2D.Double(xPos, yPos), hexagon);
        logicalCoordinates.put(hexagon, new int[]{row, col});

        float[] hsb = java.awt.Color.RGBtoHSB(34, 139, 34, null);
        g2d.setColor(java.awt.Color.getHSBColor(hsb[0], hsb[1], hsb[2]));

        if (board[row][col] == null) {
          int[] checkedHex = new int[]{row, col};

          paintYellow((int) xPos, (int) yPos, g2d, checkedHex, hexagon);

          hexagon.paintComponent(g2d);
        } else if (board[row][col] != null) { //if not an empty hexagon
          if (board[row][col].equals(Color.BLACK)) {
            paintBlack((int) xPos, (int) yPos, g2d, hexagon);

          } else if (board[row][col].equals(Color.WHITE)) {
            paintWhite((int) xPos, (int) yPos, g2d, hexagon);
          }
        }
      }
    }
  }

  private void paintYellow(int xPos, int yPos, Graphics2D g2d, int[] checkedHex, Hexagon hexagon) {
    if (checkOnSelectedHexagon(checkedHex)) {
      hexagon.paintComponent(g2d);
      g2d.setColor(java.awt.Color.YELLOW);
      g2d.fillOval((int) (xPos - HEX_SIDE_LENGTH / 2),
              (int) (yPos - HEX_SIDE_LENGTH / 2),
              HEX_SIDE_LENGTH, HEX_SIDE_LENGTH);
    }
  }

  private void paintBlack(int xPos, int yPos, Graphics2D g2d, Hexagon hexagon) {
    hexagon.paintComponent(g2d);
    g2d.setColor(java.awt.Color.RED);
    //            g2d.setColor(Color.BLACK);
    g2d.fillOval((int) (xPos - HEX_SIDE_LENGTH / 2),
            (int) (yPos - HEX_SIDE_LENGTH / 2),
            HEX_SIDE_LENGTH, HEX_SIDE_LENGTH);
  }

  private void paintWhite(int xPos, int yPos, Graphics2D g2d, Hexagon hexagon) {
    hexagon.paintComponent(g2d);
    g2d.setColor(java.awt.Color.WHITE);
    g2d.fillOval((int) (xPos - HEX_SIDE_LENGTH / 2),
            (int) (yPos - HEX_SIDE_LENGTH / 2),
            HEX_SIDE_LENGTH, HEX_SIDE_LENGTH);
  }

  private int updateStartingCoord(int difference) {
    return (int) (HEX_SIDE_LENGTH / 2 * Math.sqrt(3)) * (model.getSize() - difference);
  }

  static class Hexagon extends JPanel {
    private Path2D.Double hexagon;
    public Graphics2D graphics2D;

    public Hexagon(double xPos, double yPos, int size) {
      hexagon = new Path2D.Double();

      for (int i = 0; i < 6; i++) {
        double angle = Math.toRadians(60 * i) + Math.toRadians(90);
        double x = xPos + size * Math.cos(angle);
        double y = yPos + size * Math.sin(angle);

        if (i == 0) {
          hexagon.moveTo(x, y);
        } else {
          hexagon.lineTo(x, y);
        }
      }
      hexagon.closePath();
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      graphics2D = g2d;
      g2d.fill(hexagon);

      g2d.setColor(java.awt.Color.DARK_GRAY);
      g2d.draw(hexagon);
    }
  }

  private void setSelectedHexagon(int[] coordinate) {
    selectedHexagon = new int[]{coordinate[0], coordinate[1]};
  }

  private boolean checkOnSelectedHexagon(int[] coordinate) {
    if (!checkNoSelectedHexagon()) {
      return coordinate[0] == selectedHexagon[0]
              && coordinate[1] == selectedHexagon[1];
    }
    return false;
  }

  private class MouseEventsListener extends MouseInputAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
      mouseDragged(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      Point physicalP = e.getPoint();
      System.out.println("Physical coords: " + physicalP.getX() + ", " + physicalP.getY());

      // check if it's in proper bounds
      if (!inBoardBounds(physicalP)) {
        resetSelectedHexagon();
      }

      for (Point2D c : coordinates.keySet()) {
        if (withinBounds(physicalP, c)) {
          Hexagon currentHexagon = coordinates.get(c);
          int[] logicalCoords = logicalCoordinates.get(currentHexagon);

          boolean isSelectedHexagon = !checkNoSelectedHexagon();
          boolean onSelectedHexagon = checkOnSelectedHexagon(logicalCoords);

          // if there is a selected hexagon and the selected hexagon is the same as the
          // hexagon that was clicked, then deselect the hexagon
          if (isSelectedHexagon && onSelectedHexagon) {
            resetSelectedHexagon();
          }

          // if there is a selected hexagon and the selected hexagon is not the same as the
          // hexagon that was clicked, then deselect the hexagon and select the new hexagon
          else if (isSelectedHexagon && !onSelectedHexagon) {
            resetSelectedHexagon();
            setSelectedHexagon(logicalCoords);
          }
          // if there is no selected hexagon, then select the hexagon that was clicked
          else if (checkNoSelectedHexagon()) {
            setSelectedHexagon(logicalCoords);
          }
        }
      }
    }

    private boolean inBoardBounds(Point physicalP) {
      for (Point2D c : coordinates.keySet()) {
        if (withinBounds(physicalP, c)) {
          return true;
        }
      }
      return false;
    }

    private boolean withinBounds(Point testPoint, Point2D center) {
      double testX = testPoint.getX();
      double testY = testPoint.getY();
      double centerX = center.getX();
      double centerY = center.getY();
      double sideRadius = HEX_SIDE_TO_SIDE_DIAMETER / 2;
      double halfSideLength = HEX_SIDE_LENGTH / 2;

      double x1 = centerX - sideRadius;
      double x2 = centerX - halfSideLength;
      double x3 = centerX + halfSideLength;
      double x4 = centerX + sideRadius;

      double y1 = centerY - HEX_SIDE_LENGTH / 2;
      double y2 = centerY - halfSideLength;
      double y3 = centerY + halfSideLength;
      double y4 = centerY + HEX_SIDE_LENGTH / 2;

      return (testY >= y1 && testY <= y2 && testX >= x1 && testX <= x4)
              || (testY >= y2 && testY <= y3 && testX >= x2 && testX <= x3)
              || (testY >= y3 && testY <= y4 && testX >= x1 && testX <= x4);
    }
  }

  private class KeyEventsListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      int keyCode = e.getKeyCode();

      for (GUIFeatures feature : BasicReversiFrame.moveList) {
        if (keyCode == KeyEvent.VK_ENTER) {
          if (!checkNoSelectedHexagon()) {
            listener.makeMove(selectedHexagon[0], selectedHexagon[1]);
            resetSelectedHexagon();
          } else {
            System.out.println("select a hexagon first");
          }
        } else if (keyCode == KeyEvent.VK_P) {
          listener.pass();
          resetSelectedHexagon();
        }
      }
    }
  }

  private void resetSelectedHexagon() {
    selectedHexagon = new int[0];
  }

  private boolean checkNoSelectedHexagon() {
    int[] mtArray = new int[0];
    boolean b = Arrays.equals(selectedHexagon, mtArray);
    boolean c = selectedHexagon == null;
    return b || c;
  }
}