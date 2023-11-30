package cs3500.reversi.controller;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.Player;

/**
 * The Controller class facilitates communication between the Reversi game's model, player, and
 * view. It sets up listeners to coordinate interactions during the game.
 */
public final class Controller {
  /**
   * Constructs a new instance of a controller for the game of Reversi.
   *
   * @param model The game model implementing IModel.
   * @param player The player participating in the game.
   * @param view The game view implementing IView.
   * @param color The player's PieceColor in the game.
   */
  public Controller(IModel model, Player player, IView view, PieceColor color) {
    model.addListener(new ModelFeaturesImpl(player, view, color));
    player.addListener(new PlayerFeaturesImpl(model, view, color));
    view.addListener(new PlayerFeaturesImpl(model, view, color));
  }
}