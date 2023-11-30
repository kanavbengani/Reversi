package cs3500.reversi.player;

/**
 * The Player interface defines the scaffold for players in the Reversi game.
 */
public interface Player {
  /**
   * Plays a move based on the specific implementation of the player.
   */
  void playAMove();

  /**
   * Adds a listener to the player to receive notifications about moves or passes.
   *
   * @param playerFeatures The PlayerFeatures to be added as a listener.
   */
  void addListener(PlayerFeatures playerFeatures);
}
