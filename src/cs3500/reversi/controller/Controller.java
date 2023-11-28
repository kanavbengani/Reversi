package cs3500.reversi.controller;

import cs3500.reversi.model.*;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.Player;

public final class Controller {
  private final IModel model;
  private final Player player;
  private final IView view;
  private final PieceColor color;
  
  public Controller(IModel model, Player player, IView view, PieceColor color) {
    this.model = model;
    this.player = player;
    this.view = view;
    this.color = color;
    
    this.model.addListener(new ModelFeaturesImpl(this.player, this.view, this.color));
    this.player.addListener(new PlayerFeaturesImpl(this.model, this.view, this.color));
    this.view.addListener(new PlayerFeaturesImpl(this.model, this.view, this.color));
  }
}