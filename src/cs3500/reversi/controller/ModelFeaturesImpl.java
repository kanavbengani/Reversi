package cs3500.reversi.controller;

import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.Player;
import cs3500.reversi.view.IView;

import java.util.Optional;

class ModelFeaturesImpl implements ModelFeatures {
  private Player player;
  private IView view;
  private PieceColor color;
  
  ModelFeaturesImpl(Player player, IView view, PieceColor color) {
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