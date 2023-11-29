package cs3500.reversi.controller;

import cs3500.reversi.model.*;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.Player;

public final class Controller {
  public Controller(IModel model, Player player, IView view, PieceColor color) {
    model.addListener(new ModelFeaturesImpl(player, view, color));
    player.addListener(new PlayerFeaturesImpl(model, view, color));
    view.addListener(new PlayerFeaturesImpl(model, view, color));
  }
}