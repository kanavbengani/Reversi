package cs3500.reversi.provider.controller;

/**
 * An interface containing all possible reversi moves currently.
 */
public interface GUIFeatures {
  
  /**
   * Represents the move to make a play.
   * @param row is the row that the play will be made
   * @param col is the col that the play will be made
   */
  void makeMove(int row, int col);
  
  /**
   * Represents the ability to pass a turn.
   */
  void pass();
}
