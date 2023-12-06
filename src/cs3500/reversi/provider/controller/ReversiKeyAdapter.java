package cs3500.reversi.provider.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class to adapt key inputs from the user in the GUI to the controller.
 */
public class ReversiKeyAdapter extends KeyAdapter {
  private final ReversiController listener;
  
  /**
   * Constructor for ReversiKeyAdapter.
   * @param listener is the controller listener that lives in the GUi
   */
  public ReversiKeyAdapter(ReversiController listener) {
    this.listener = listener;
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    listener.handleKeyPress(keyCode);
  }
}

