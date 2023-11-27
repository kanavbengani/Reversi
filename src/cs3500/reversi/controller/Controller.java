package cs3500.reversi.controller;

import cs3500.reversi.model.*;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.Player;
import cs3500.reversi.view.PlayerFeatures;

import java.util.Optional;

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
    
    this.model.addListener(new ModelFeaturesImpl());
    this.player.addListener(new PlayerFeaturesImpl());
    this.view.addListener(new PlayerFeaturesImpl());
  }
  
  // For testing purposes ONLY.
  Controller(IModel model, Player player, IView view, PieceColor color,
             PlayerFeatures pf, ModelFeatures mf) {
    this.model = model;
    this.player = player;
    this.view = view;
    this.color = color;
    
    this.model.addListener(mf);
    this.player.addListener(pf);
    this.view.addListener(pf);
  }
  
  private class PlayerFeaturesImpl implements PlayerFeatures {
    @Override
    public void pass() {
      try {
        Controller.this.model.pass(Controller.this.color);
        Controller.this.view.refresh();
      } catch (IllegalStateException e) {
        Controller.this.view.promptMessage(e.getMessage());
      }
    }
    
    @Override
    public void move(AxialPosn axialPosn) {
      try {
        Controller.this.model.playMove(Controller.this.color, axialPosn);
        Controller.this.view.refresh();
      } catch (IllegalStateException | IllegalArgumentException e) {
        Controller.this.view.promptMessage(e.getMessage());
      }
    }
  }
  
  private class ModelFeaturesImpl implements ModelFeatures {
    @Override
    public void notifyTurn(PieceColor pieceColor) {
      Controller.this.view.itsYourTurn(pieceColor);
    }
    
    @Override
    public void playAMove(PieceColor pieceColor) {
      if (Controller.this.color.equals(pieceColor)) {
        Controller.this.player.playAMove();
      }
    }
    
    @Override
    public void itsGameOver(Optional<PieceColor> winner) {
      if (winner.isEmpty()) {
        Controller.this.view.promptMessage("STALEMATE!");
      }
      else {
        Controller.this.view.promptMessage(winner.get() + " WON!");
      }
    }
  }
}