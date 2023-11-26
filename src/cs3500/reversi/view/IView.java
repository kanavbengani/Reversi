package cs3500.reversi.view;

import cs3500.reversi.model.PieceColor;

/**
 * The interface represents a view for the game of Reversi. It provides methods
 * for displaying the game state and adding feature listeners to interact with the view.
 */
public interface IView {
  /**
   * Display the current state of the Reversi game.
   *
   * @param b A boolean indicating whether the view should be shown or not.
   */
  void display(boolean b);

  /**
   * Refresh this view.
   */
  void refresh();

  /**
   * Add a feature listener to the Reversi view to allow interaction with the game.
   *
   * @param playerFeatures An instance of {@link PlayerFeatures} that is going to be a listener to all
   *                 events triggered by the view.
   */
  void addListener(PlayerFeatures playerFeatures);
  
  /**
   * Prompts the user with the given message.
   */
  void promptMessage(String message);
  
  /**
   * Tells the view whose turn it is.
   */
  void itsYourMove(PieceColor pieceColor);
}
