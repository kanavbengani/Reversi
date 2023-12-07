package cs3500.reversi;

import cs3500.reversi.model.*;
import cs3500.reversi.view.SquareTextualView;
import org.junit.Before;
import org.junit.Test;

public class ReversiSquareModelTests {
  
  private IModel model;
  private IROModel roModel;
  private StringBuilder logBlack;
  private StringBuilder logWhite;
  private final int numRings = 4;
  
  @Before
  public void initTest() {
    this.model = new SquareModel(this.numRings);
    this.roModel = this.model.getReadOnlyModel();
    this.logBlack = new StringBuilder();
    this.logWhite = new StringBuilder();
    this.model.addListener(new MockModelListener(this.logBlack));
    this.model.addListener(new MockModelListener(this.logWhite));
    this.model.startGame();
  }
  
  @Test
  public void testSquareModelPlayFullGame() {
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(2, -1));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(1, -1));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(2, 1));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 2));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(0, 2));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(1, 2));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(-1, 0));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(-1, 2));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(0, -1));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(-1, 1));
    System.out.println(new SquareTextualView(this.model.getReadOnlyModel()));
    System.out.println(this.model.getWinner());
  }
}
