package cs3500.reversi.controller;

import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.Player;
import cs3500.reversi.view.IView;

import java.util.Optional;

/**
 * A concrete implementation of the model features that listens to all model triggers and acts
 * based on that.
 */
public class ModelFeaturesImpl implements ModelFeatures {
  private final Player player;
  private final IView view;
  private final PieceColor color;
  
  /**
   * Constructs a new ModelFeaturesImpl.
   *
   * @param player The player in the game of Reversi.
   * @param view The view to be used by the player.
   * @param color The color assigned to the player.
   */
  public ModelFeaturesImpl(Player player, IView view, PieceColor color) {
    this.player = player;
    this.view = view;
    this.color = color;
  }
  
  @Override
  public void notifyTurn(PieceColor pieceColor) {
    this.view.itsTheTurnOf(pieceColor);
  }
  
  @Override
  public void playAMove(PieceColor pieceColor) {
    if (this.color.equals(pieceColor)) {
      this.player.playAMove();
    }
  }
  
  @Override
  public void itsGameOver(Optional<PieceColor> winner) {
    if (winner.isEmpty()) {
      this.view.promptMessage("STALEMATE!");
    }
    else {
      this.view.promptMessage(winner.get() + " WON!");
    }
  }
}