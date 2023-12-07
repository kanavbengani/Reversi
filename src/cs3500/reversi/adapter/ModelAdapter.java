package cs3500.reversi.adapter;

import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.provider.controller.ReversiController;
import cs3500.reversi.provider.model.Color;
import cs3500.reversi.provider.model.ReversiModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * The ModelAdapter class serves as an adapter between our Model and our providers' ReversiModel
 * It allows the integration of the Model with external components that expect a ReversiModel.
 */
public class ModelAdapter extends Model implements ReversiModel {
  
  /**
   * Constructs a new ModelAdapter with the specified number of rings.
   *
   * @param numRings The number of rings for the Reversi board.
   */
  public ModelAdapter(int numRings) {
    super(numRings);
  }
  
  /**
   * Constructs a new ModelAdapter that is a copy of the given ModelAdapter.
   *
   * @param model The ModelAdapter to copy.
   */
  public ModelAdapter(ModelAdapter model) {
    super(model.numRings, model.pieceColor1, model.pieceColor2,
        model.currentPieceColor, model.passCount, new ArrayList<>(), new HashMap<>(model.board));
  }
  
  @Override
  public int getSize() {
    // Unsure what this needs to be returning. Not specified in the Javadoc. Assuming it is number
    // of rings.
    return super.getNumRings() * 2 + 1;
  }
  
  @Override
  public boolean possibleMoveExists(Color playerType) throws IllegalArgumentException {
    if (Utils.colorToPieceColor(playerType).isEmpty()) {
      throw new IllegalArgumentException("Invalid player type.");
    }
    return super.anyLegalMoves(Utils.colorToPieceColor(playerType).get());
  }
  
  @Override
  public int getScore(Color player) throws IllegalArgumentException {
    if (Utils.colorToPieceColor(player).isEmpty()) {
      throw new IllegalArgumentException("No score for the given player.");
    }
    return super.getScore(Utils.colorToPieceColor(player).get());
  }
  
  @Override
  public boolean canPlay(int row, int col, Color player) {
    if (Utils.colorToPieceColor(player).isEmpty()) {
      throw new IllegalArgumentException("Invalid player.");
    }
    return super.isMoveValid(Utils.colorToPieceColor(player).get(),
        Utils.convertRowColToAxial(row, col, super.getNumRings()));
  }
  
  @Override
  public void switchTurn() {
    super.pass(super.getTurnColor());
  }
  
  @Override
  public Color getTurn() {
    try {
      return Utils.pieceColorToColor(Optional.of(super.getTurnColor()));
    } catch (IllegalStateException is) {
      return null;
    }
  }
  
  @Override
  public int getNumRows() {
    return super.getNumRings() * 2 + 1;
  }
  
  @Override
  public Color[] getRow(int index) throws IllegalArgumentException {
    Color[] result = new Color[this.getNumCols(index)];
    for (int col = 0; col < this.getNumCols(index); col++) {
      Optional<PieceColor> pieceColor = super.getPieceAt(
          Utils.convertRowColToAxial(index, col, super.getNumRings()));
      result[col] = Utils.pieceColorToColor(pieceColor);
    }
    return result;
  }
  
  @Override
  public int getNumCols(int index) throws IllegalArgumentException {
    int distance = Math.abs(index - super.getNumRings());
    return super.getNumRings() * 2 + 1 - distance;
  }
  
  @Override
  public Color getPieceAt(int row, int col) throws IllegalArgumentException {
    return this.getRow(row)[col];
  }
  
  @Override
  public Color[][] copyBoard() {
    Color[][] result = new Color[this.getNumRows()][];
    for (int i = 0; i < this.getNumRows(); i++) {
      result[i] = this.getRow(i);
    }
    return result;
  }
  
  @Override
  public ReversiModel copyGame() {
    return new ModelAdapter(this);
  }
  
  @Override
  public void makePlay(int row, int col) throws IllegalArgumentException {
    super.playMove(super.getTurnColor(), Utils.convertRowColToAxial(row, col, super.getNumRings()));
  }
  
  @Override
  public void passTurn() {
    super.pass(super.getTurnColor());
  }
  
  @Override
  public void subscribe(ReversiController controller) {
    throw new IllegalArgumentException("stub");
  }
  
  @Override
  public void notifyMove() {
    throw new IllegalArgumentException("stub");
  }
  
  @Override
  public void notifyTurnMade() {
    throw new IllegalArgumentException("stub");
  }
}
