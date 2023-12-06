package cs3500.reversi.provider.view;

import javax.swing.KeyStroke;

import cs3500.reversi.provider.controller.GUIFeatures;
import cs3500.reversi.provider.controller.ReversiController;
import cs3500.reversi.provider.model.Color;

/**
 * A view for Reversi which displays the game board and provide visual interface for users.
 */
public interface ReversiView {
  /**
   * Refreshes the view.
   */
  void refresh();

  /**
   * Makes the view visible.
   *
   * @param b true if the view should be visible, false otherwise
   */
  void makeVisible(boolean b);

  /**
   * Adds a click listener to the view.
   *
   * @param controller the controller to add
   */
  void addClickListener(ReversiController controller);

  /**
   * Adds a key input listener to the view.
   *
   * @param listener the listener to add
   */
  void addKeyInputListener(ReversiController listener);

  /**
   * Adds a move to the view.
   *
   * @param move the move to add
   */
  void addFeature(GUIFeatures move);

  /**
   * Sets a key to a move.
   *
   * @param key  the key to set
   * @param move the move to set
   */
  void setKey(KeyStroke key, String move);

  /**
   * Displays an error message.
   */
  void error();

  /**
   * Subscribes the view to the controller.
   *
   * @param reversiGUIController the controller to subscribe to
   */
  void subscribe(ReversiController reversiGUIController);

  /**
   * Displays the game over message.
   *
   * @param score the score of the game
   */
  void gameOver(Color color, int score);
}
