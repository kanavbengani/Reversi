package cs3500.reversi.adapter;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.provider.controller.ReversiController;
import cs3500.reversi.provider.model.Color;
import cs3500.reversi.provider.model.ReversiModel;

import java.util.Optional;

public class ModelAdapter extends Model implements ReversiModel {
  public ModelAdapter(int numRings) {
    super(numRings);
  }
  
  @Override
  public int getSize() {
    // Unsure what this needs to be returning. Not specified in the Javadoc. Assuming it is number
    // of rings.
    return super.getNumRings() * 2 + 1;
  }
  
  @Override
  public boolean possibleMoveExists(Color playerType) throws IllegalArgumentException {
    if (this.colorToPieceColor(playerType).isEmpty()) {
      throw new IllegalArgumentException("Invalid player type.");
    }
    return super.anyLegalMoves(this.colorToPieceColor(playerType).get());
  }
  
  @Override
  public int getScore(Color player) throws IllegalArgumentException {
    if (this.colorToPieceColor(player).isEmpty()) {
      throw new IllegalArgumentException("No score for the given player.");
    }
    return super.getScore(this.colorToPieceColor(player).get());
  }
  
  @Override
  public boolean canPlay(int row, int col, Color player) {
    if (this.colorToPieceColor(player).isEmpty()) {
      throw new IllegalArgumentException("Invalid player.");
    }
    return super.isMoveValid(this.colorToPieceColor(player).get(), this.getAxialPosn(row, col));
  }
  
  @Override
  public void switchTurn() {
    super.pass(super.getTurnColor());
  }
  
  @Override
  public Color getTurn() {
    try {
      return this.pieceColorToColor(Optional.of(super.getTurnColor()));
    } catch (IllegalStateException is) {
      return null;
    }
  }
  
  private Optional<PieceColor> colorToPieceColor(Color c) {
    if (c == null) {
      return Optional.empty();
    }
    if (c.equals(Color.BLACK)) {
      return Optional.of(PieceColor.BLACK);
    }
    
    return Optional.of(PieceColor.WHITE);
  }
  
  private Color pieceColorToColor(Optional<PieceColor> pc) {
    if (pc.isEmpty()) {
      return null;
    }
    if (pc.get().equals(PieceColor.BLACK)) {
      return Color.BLACK;
    }
    
    return Color.WHITE;
  }
  
  @Override
  public int getNumRows() {
    return super.getNumRings() * 2 + 1;
  }
  
  @Override
  public Color[] getRow(int index) throws IllegalArgumentException {
    Color[] result = new Color[this.getNumCols(index)];
    for (int col = 0; col < this.getNumCols(index); col++) {
      Optional<PieceColor> pieceColor = super.getPieceAt(this.getAxialPosn(index, col));
      result[col] = this.pieceColorToColor(pieceColor);
    }
    return result;
  }
  
  private AxialPosn getAxialPosn(int row, int col) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Invalid row, col.");
    }
    
    int r = row - super.getNumRings();
    int q;
    
    if (r <= 0) {
      q = super.getNumRings() - this.getNumCols(row) + 1 + col;
      return new AxialPosn(q, r);
    }
    else if (r <= super.getNumRings()) {
      q = -super.getNumRings() + col;
      return new AxialPosn(q, r);
    }
    throw new IllegalArgumentException("Invalid row, col");
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
    // TODO
    throw new IllegalArgumentException("stub");
//    return super.copy();
  }
  
  @Override
  public void makePlay(int row, int col) throws IllegalArgumentException {
    super.playMove(super.getTurnColor(), this.getAxialPosn(row, col));
  }
  
  @Override
  public void passTurn() {
    super.pass(super.getTurnColor());
  }
  
  @Override
  public void subscribe(ReversiController controller) {
    // TODO:
    throw new IllegalArgumentException("stub");
//    super.addListener(controller);
  }
  
  @Override
  public void notifyMove() {
    // TODO:
    throw new IllegalArgumentException("stub");
  }
  
  @Override
  public void notifyTurnMade() {
    // TODO:
    throw new IllegalArgumentException("stub");
  }
}
