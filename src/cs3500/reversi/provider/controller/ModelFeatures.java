package cs3500.reversi.provider.controller;

/**
 * Represents the features that the model, controllers, and players can perform.
 */
public interface ModelFeatures {
  /**
   * Communicates between the model, controllers, and players that a move can been made.
   */
  void notifyMove();

  /**
   * Communicates between the model, controllers, and players that a turn has been made.
   */
  void notifyTurnMade();
}