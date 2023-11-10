package cs3500.reversi.view;
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
   * Add a feature listener to the Reversi view to allow interaction with the game.
   *
   * @param features An instance of {@link ViewFeatures} that is going to be a listener to all
   *                 events triggered by the view.
   */
  void addFeatureListener(ViewFeatures features);
}
