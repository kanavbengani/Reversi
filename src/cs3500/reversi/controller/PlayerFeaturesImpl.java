package cs3500.reversi.controller;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.PlayerFeatures;

/**
 * A concrete implementation of the player features that listens to all player/view triggers and
 * acts based on that.
 */
public class PlayerFeaturesImpl implements PlayerFeatures {
  protected final IModel model;
  protected final IView view;
  protected final PieceColor color;
  
  /**
   * Constructs a new PlayerFeaturesImpl.
   *
   * @param model The model in the game of Reversi.
   * @param view The view to be used by the player.
   * @param color The color assigned to the player.
   */
  public PlayerFeaturesImpl(IModel model, IView view, PieceColor color) {
    this.model = model;
    this.view = view;
    this.color = color;
  }
  
  @Override
  public void pass() {
    try {
      this.model.pass(this.color);
      this.view.refresh();
    } catch (IllegalStateException e) {
      this.view.promptMessage(e.getMessage());
    }
  }
  
  @Override
  public void move(AxialPosn axialPosn) {
    try {
      this.model.playMove(this.color, axialPosn);
      this.view.refresh();
    } catch (IllegalStateException | IllegalArgumentException e) {
      this.view.promptMessage(e.getMessage());
    }
  }
}