package cs3500.reversi.controller;

import cs3500.reversi.model.*;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.Player;
import cs3500.reversi.view.PlayerFeatures;

public class Controller implements IController, PlayerFeatures, ModelFeatures {
  private final IModel model;
  private final Player player;
  private final IView view;
  
  public Controller(IModel model, Player player, IView view) {
    this.model = model;
    this.player = player;
    this.view = view;
    
    this.model.addListener(this);
    this.player.addListener(this);
    this.view.addListener(this);
  }
  
  @Override
  public void pass(PieceColor pieceColor) {
    try {
      this.model.pass(pieceColor);
    } catch (IllegalStateException e) {
      this.view.promptMessage(e.getMessage());
    }
  }
  
  @Override
  public void move(PieceColor pieceColor, AxialPosn axialPosn) {
    try {
      this.model.playMove(pieceColor, axialPosn);
    } catch (IllegalStateException | IllegalArgumentException e) {
      this.view.promptMessage(e.getMessage());
    }
  }
  
  @Override
  public void itsTheMoveOf(PieceColor pieceColor) {
    this.player.itsYourMove(pieceColor);
    this.view.itsYourMove(pieceColor);
  }
}
