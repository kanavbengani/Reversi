package cs3500.reversi.provider.model;

/**
 * An interface that holds all the observational methods of Reversi, this was created to ensure
 * that whenever an instance of the Reversi model is delegated into a different class, such as
 * the view or the controller, that class will only be able to access the methods in Reversi that
 * only.
 */
public interface ReadonlyReversiModel {
  
  /**
   * Gets the size of the board.
   *
   * @return the size of the board
   */
  int getSize();

  /**
   * Checks if there is a possible move for the given player.
   *
   * @param playerType the player to check for
   * @return true if there is a possible move for the given player, false otherwise
   * @throws IllegalArgumentException if playerType is null.
   */
  boolean possibleMoveExists(Color playerType) throws IllegalArgumentException;

  /**
   * Calculates the score of the given Player by adding up all the pieces they currently
   * have ownership of in the board.
   *
   * @param player the player to get the score of
   * @return the score of the given player
   * @throws IllegalArgumentException if the player passed into the method is null.
   */
  int getScore(Color player) throws IllegalArgumentException;

  /**
   * Checks if the given coordinates can be played by the given player.
   *
   * @param row    the row of the piece to check
   * @param col    the column of the piece to check
   * @param player the player to check for
   * @return true if the given coordinates can be played by the given player, false otherwise
   */
  boolean canPlay(int row, int col, Color player);

  /**
   * Switches the turn to the next player.
   */
  void switchTurn();

  /**
   * Checks if the game is over, by seeing if the game is over, if the game has no
   * possible moves for either player, or if both players pass twice in a row.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Gets the player whose turn it is, mainly written for testing.
   *
   * @return the player whose turn it is
   */
  Color getTurn();

  /**
   * Gets the number of rows in the hexagonal board.
   *
   * @return the size of the first array in board
   */
  int getNumRows();

  /**
   * Gets the row at the given index.
   *
   * @param index the index of the row to get
   * @return the row at the given index
   * @throws IllegalArgumentException if either the given row does not exist, i.e. is a negative
   *                                  number, or it is larger than what it can possibly be.
   */
  Color[] getRow(int index) throws IllegalArgumentException;

  /**
   * Gets the number of columns at the given index.
   *
   * @param index the index of the column to get
   * @return the number of columns at the given index
   * @throws IllegalArgumentException if either the given col does not exist, i.e. is a negative
   *                                  number, or it is larger than what it can possibly be.
   */
  int getNumCols(int index) throws IllegalArgumentException;

  /**
   * Gets the piece at the given row and column.
   *
   * @param row the row of the piece to get
   * @param col the column of the piece to get
   * @return the piece at the given row and column
   * @throws IllegalArgumentException if the given row or column does not exist, i.e. is a
   *                                  negative number, or it is larger than what it can possibly
   *                                  be.
   */
  Color getPieceAt(int row, int col) throws IllegalArgumentException;

  /**
   * Gets the board of the game.
   *
   * @return the board of the game
   */
  Color[][] copyBoard();

  /**
   * Make deep copy of the game.
   *
   * @return a new game with exact same game states.
   */
  ReversiModel copyGame();
}