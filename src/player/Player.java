package player;

import model.IModel;

/**
 * The Player interface represents a player in a Reversi game. Implementing classes of this
 * interface are responsible for making moves on the game board.
 * A player must implement the playMove method, which defines how the player
 * chooses a move to play in the Reversi game.
 */
public interface Player {

  /**
   * Makes a move in the Reversi game based on the current state of the game model.
   * The player's strategy and decision-making process for selecting a move are
   * implemented in this method.
   *
   * @param model The game model (IROModel) representing the current state of the Reversi game.
   * @throws IllegalArgumentException If the selected move is invalid or the player cannot make a
   *         move.
   */
  void playMove(IModel model);
}
