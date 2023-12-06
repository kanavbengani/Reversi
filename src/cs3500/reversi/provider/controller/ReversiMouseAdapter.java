package cs3500.reversi.provider.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * A controller that handles mouse events for the Reversi game.
 */
public class ReversiMouseAdapter extends MouseAdapter {

  private final ReversiController controller;

  /**
   * Constructor for ReversiMouseAdapter.
   *
   * @param controller the controller listener that lives inside the GUI
   */
  public ReversiMouseAdapter(ReversiController controller) {
    this.controller = Objects.requireNonNull(controller);
  }

  /**
   * Handles a mouse click.
   *
   * @param e the mouse event
   */
  public void mouseClicked(MouseEvent e) {
    Point pt = e.getPoint();
    controller.handleCellClick(0, 0);
  }
}
