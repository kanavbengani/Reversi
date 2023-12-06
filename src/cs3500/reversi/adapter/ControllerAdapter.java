package cs3500.reversi.adapter;

import cs3500.reversi.controller.ModelFeaturesImpl;
import cs3500.reversi.controller.PlayerFeaturesImpl;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.provider.controller.ReversiController;
import cs3500.reversi.provider.model.Color;
import cs3500.reversi.provider.model.ReversiModel;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.Player;


/**
 * The Controller class facilitates communication between the Reversi game's model, player, and
 * view. It sets up listeners to coordinate interactions during the game.
 */
public final class ControllerAdapter extends PlayerFeaturesImpl implements ReversiController {
  private final ModelFeatures mf;
  
  /**
   * Constructs a new instance of a controller for the game of Reversi.
   *
   * @param model The game model implementing IModel.
   * @param player The player participating in the game.
   * @param view The game view implementing IView.
   * @param color The player's PieceColor in the game.
   */
  public ControllerAdapter(IModel model, Player player, IView view, PieceColor color) {
    super(model, view, color);
    this.mf = new ModelFeaturesImpl(player, view, color);
    view.addListener(this);
    player.addListener(this);
    model.addListener(this.mf);
  }
  
  @Override
  public void makeMove(int row, int col) {
    super.move(Utils.convertRowColToAxial(row, col, super.model.getNumRings()));
  }
  
  @Override
  public void pass() {
    super.pass();
  }
  
  @Override
  public void notifyMove() {
    this.mf.playAMove(super.color);
  }
  
  @Override
  public void notifyTurnMade() {
    this.mf.notifyTurn(super.color);
  }
  
  @Override
  public void playGame(ReversiModel m) {
    // Stub, because startGame will be called by model.
    throw new UnsupportedOperationException("startGame will be called by the model.");
  }
  
  @Override
  public void handleCellClick(int row, int col) {
    super.view.refresh();
    // Stub, because providers said that this method is from previous iterations.
  }
  
  @Override
  public void handleKeyPress(int keyCode) {
    super.view.refresh();
    // Stub, because providers said that this method is from previous iterations
  }
  
  @Override
  public void assignColor(Color currentColor) {
    throw new UnsupportedOperationException("assignColor will be done in the constructor.");
  }
  
  @Override
  public void notifyGameOver() {
    this.mf.itsGameOver(this.model.getWinner());
  }
}