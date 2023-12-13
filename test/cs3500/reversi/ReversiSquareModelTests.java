package cs3500.reversi;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

import cs3500.reversi.model.Posn;
import cs3500.reversi.model.square.SquareModel;
import cs3500.reversi.model.square.SquarePosn;
import cs3500.reversi.view.SquareTextualView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ReversiSquareModelTests {
  
  private IModel model;
  private IROModel roModel;
  private StringBuilder logBlack;
  private StringBuilder logWhite;
  private int sideLength = 4;
  
  @Before
  public void initTest() {
    this.model = new SquareModel(this.sideLength);
    this.roModel = this.model.getReadOnlyModel();
    this.logBlack = new StringBuilder();
    this.logWhite = new StringBuilder();
    this.model.addListener(new MockModelListener(this.logBlack));
    this.model.addListener(new MockModelListener(this.logWhite));
    this.model.startGame();
  }
  
  // Constructor
  
  
  // IROModel Tests (Observation Methods)
  // IsMoveValid
  @Test
  public void testROMIsMoveValidValidMoves() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new SquarePosn(2, 0)));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.WHITE, new SquarePosn(2, -1)));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(2, -1));
    
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new SquarePosn(-1, 1)));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(-1, 1));
  }
  
  @Test
  public void testROMIsMoveValidInvalidMoveSameLocation() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new SquarePosn(2, 0)));
    this.model.isMoveValid(PieceColor.BLACK, new SquarePosn(2, 0));
    
    Assert.assertFalse(this.roModel.isMoveValid(PieceColor.WHITE, new SquarePosn(2, 0)));
  }
  
  @Test
  public void testROMIsMoveValidInvalidMoveSamePlayer() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new SquarePosn(2, 0)));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    
    Assert.assertFalse(this.roModel.isMoveValid(PieceColor.BLACK, new SquarePosn(0, 2)));
  }
  
  // IsGameOver
  
  @Test
  public void testROMIsGameOverFalseWhenGameIsNotOver() {
    Assert.assertFalse(this.roModel.isGameOver());
  }
  
  @Test
  public void testSquareModelGameOverFullGamePlayed() {
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
    Assert.assertTrue(this.model.isGameOver());
  }
  
  @Test
  public void testSquareModelGameNotOver() {
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(2, -1));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(1, -1));
    Assert.assertFalse(this.model.isGameOver());
  }
  
  @Test
  public void testROMIsGameOverTrueWhenStalemate() {
    this.sideLength = 2;
    this.initTest();
    Assert.assertTrue(this.roModel.isGameOver());
  }
  
  // GetAllCapturedPieces
  @Test
  public void testGetAllCapturedPiecesInvalidTurn() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.WHITE, new SquarePosn(0, 0)));
  }
  
  @Test
  public void testGetAllCapturedPiecesInvalidMoveOutOfBounds() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.BLACK, new SquarePosn(10, 10)));
  }
  
  @Test
  public void testGetAllCapturedPiecesInvalidMoveOnOccupiedCell() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.BLACK, new SquarePosn(1, 0)));
  }
  
  @Test
  public void testGetAllCapturedPiecesInvalidMove() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.BLACK, new SquarePosn(0, 0)));
  }
  
  @Test
  public void testGetAllCapturedPiecesValidMove() {
    System.out.println(new SquareTextualView(this.roModel));
    Assert.assertEquals(this.model.getAllCapturedPieces(PieceColor.BLACK,
        new SquarePosn(2, 0)), new ArrayList<>(List.of(new SquarePosn(1, 0))));
  }
  
  
  // GetAllPosn
  @Test
  public void testGetAllPosn() {
    this.sideLength = 2;
    this.initTest();
    Assert.assertEquals(this.model.getAllPosn(), new ArrayList<>(List.of(
        new SquarePosn(1, 0), new SquarePosn(0, 0),
        new SquarePosn(1, 1), new SquarePosn(0, 1)
    )));
  }
  
  // GetAllCorners
  @Test
  public void testGetAllCorners() {
    Assert.assertEquals(this.model.getAllCorners(), new ArrayList<>(List.of(
        new SquarePosn(-1, -1), new SquarePosn(2, -1),
        new SquarePosn(-1, 2), new SquarePosn(2, 2)
    )));
  }
  
  // GetDirections -- Tested in PackagePrivate
  
  // Copy
  @Test
  public void testCopyReturnsANonMutableReference() {
    Assert.assertNotEquals(this.model, this.model.copy());
  }
  
  // GetWinner
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
    Assert.assertTrue(this.model.isGameOver());
    Assert.assertEquals(this.model.getWinner(), Optional.of(PieceColor.WHITE));
  }
  
  // AnyLegalMoves
  @Test
  public void testROMAnyLegalMovesStartOfGame() {
    Assert.assertTrue(this.roModel.anyLegalMoves(this.roModel.getTurnColor()));
  }
  
  @Test
  public void testROMAnyLegalMovesAfterGameOver() {
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
    Assert.assertFalse(this.roModel.anyLegalMoves(this.roModel.getTurnColor()));
  }
  
  // GetPieceAt
  @Test
  public void testROMGetPieceAtOutOfBounds() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> this.roModel.getPieceAt(new SquarePosn(-2, -2)));
    
    Assert.assertThrows(IllegalArgumentException.class,
        () -> this.roModel.getPieceAt(new SquarePosn(20, 12)));
  }
  
  @Test
  public void testROMGetPieceReturnsEmptyOptional() {
    Assert.assertEquals(Optional.empty(),
        this.roModel.getPieceAt(new SquarePosn(2, 0)));
  }
  
  @Test
  public void testROMGetPieceReturnsOptionalOfAtPiece() {
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
        this.roModel.getPieceAt(new SquarePosn(0, 0)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
        this.roModel.getPieceAt(new SquarePosn(1, 0)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
        this.roModel.getPieceAt(new SquarePosn(0, 1)));
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
        this.roModel.getPieceAt(new SquarePosn(1, 1)));
    Assert.assertEquals(Optional.empty(),
        this.roModel.getPieceAt(new SquarePosn(-1, 1)));
    Assert.assertEquals(Optional.empty(),
        this.roModel.getPieceAt(new SquarePosn(-1, 0)));
  }
  
  // GetTurn
  @Test
  public void testROMGetTurnAfterSwitchingTurn() {
    Assert.assertEquals(PieceColor.BLACK, this.roModel.getTurnColor());
    this.model.pass(PieceColor.BLACK);
    Assert.assertEquals(PieceColor.WHITE, this.roModel.getTurnColor());
  }
  
  @Test
  public void testROMGetTurnAfterMovingPiece() {
    Assert.assertEquals(PieceColor.BLACK, this.roModel.getTurnColor());
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    Assert.assertEquals(PieceColor.WHITE, this.roModel.getTurnColor());
  }
  
  
  // GetNumRings
  @Test
  public void testROMGetRingsCorrectReturnValue() {
    Assert.assertEquals(2, this.roModel.getNumRings());
  }
  
  // GetScore
  @Test
  public void testROMGetScoreForEmptyBoard() {
    Assert.assertEquals(this.roModel.getScore(PieceColor.BLACK), 2);
    Assert.assertEquals(this.roModel.getScore(PieceColor.WHITE), 2);
  }
  
  @Test
  public void testROMGetScoreAfterPlayingSomeMoves() {
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(2, -1));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(1, -1));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(2, 1));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 2));
    this.model.playMove(PieceColor.WHITE, new SquarePosn(0, 2));
    this.model.playMove(PieceColor.BLACK, new SquarePosn(1, 2));
    Assert.assertEquals(this.roModel.getScore(PieceColor.BLACK), 6);
    Assert.assertEquals(this.roModel.getScore(PieceColor.WHITE), 5);
  }
  
  
  // AddListener
  @Test
  public void testMAddListener() {
    // Listeners are added correctly because start game passes after two are added.
    this.model = new SquareModel(this.sideLength);
    Assert.assertThrows(IllegalStateException.class, () -> this.model.startGame());
    this.model.addListener(new MockModelListener(new StringBuilder()));
    Assert.assertThrows(IllegalStateException.class, () -> this.model.startGame());
    this.model.addListener(new MockModelListener(new StringBuilder()));
    this.model.startGame();
  }
  
  
  // IModel Tests (Operation Methods)
  // GetReadOnlyModel
  @Test
  public void testMGetReadOnlyModel() {
    Assert.assertEquals(this.model.getReadOnlyModel(), this.roModel);
  }
  
  // StartGame
  @Test
  public void testStartGameInvalid() {
    this.model = new SquareModel(this.sideLength);
    Assert.assertThrows(IllegalStateException.class, () -> this.model.startGame());
  }
  
  @Test
  public void testStartGameCorrectlyAssignsTurn() {
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.BLACK);
  }
  
  @Test
  public void testStartGameCorrectlyNotifiesListeners() {
    Assert.assertEquals(this.logBlack.toString(), "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n");
    Assert.assertEquals(this.logWhite.toString(), "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n");
  }
  
  
  // PlayMove
  @Test
  public void testMPlayMoveInvalidCoordinatesOutOfBounds() {
    Assert.assertThrows(IllegalArgumentException.class, () -> this.model.playMove(PieceColor.BLACK,
        new SquarePosn(-2, -2)));
    
    Assert.assertThrows(IllegalArgumentException.class, () -> this.model.playMove(PieceColor.BLACK,
        new SquarePosn(3, 3)));
  }
  
  @Test
  public void testMPlayMoveInvalidPlacingOnOccupiedCell() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
        new SquarePosn(0, 0)));
  }
  
  @Test
  public void testMPlayMoveInvalidIllegalCoordinates() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
        new SquarePosn(0, 0)));
  }
  
  @Test
  public void testMPlayMoveInvalidIllegalPlayer() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.WHITE,
        new SquarePosn(1, 1)));
  }
  
  @Test
  public void testMPlayMoveInvalidAfterGameOver() {
    this.sideLength = 2;
    this.initTest();
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
        new SquarePosn(1, 1)));
  }
  
  @Test
  public void testMPlayMoveValidMoveCorrectlyUpdatesCapturedPiece() {
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    Assert.assertEquals(this.model.getPieceAt(new SquarePosn(2, 0)),
        Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.model.getPieceAt(new SquarePosn(1, 0)),
        Optional.of(PieceColor.BLACK));
  }
  
  @Test
  public void testMPlayMoveValidMoveCorrectlyUpdatesMultipleCapturedPieces() {
    Map<Posn, Optional<PieceColor>> board = new HashMap<>();
    
    for (int x = -this.roModel.getNumRings() + 1; x <= this.roModel.getNumRings(); x++) {
      for (int y = -this.roModel.getNumRings() + 1; y <= this.roModel.getNumRings(); y++) {
        Posn squarePosn = new SquarePosn(x, y);
        board.put(squarePosn, Optional.empty());
      }
    }
    
    board.put(new SquarePosn(-1, -1), Optional.of(PieceColor.BLACK));
    board.put(new SquarePosn(2, -1), Optional.of(PieceColor.BLACK));
    board.put(new SquarePosn(2, 1), Optional.of(PieceColor.WHITE));
    board.put(new SquarePosn(2, 0), Optional.of(PieceColor.WHITE));
    board.put(new SquarePosn(1, 1), Optional.of(PieceColor.WHITE));
    board.put(new SquarePosn(0, 0), Optional.of(PieceColor.WHITE));
    
    IModel tempModel = new SquareModel(board);
    
    tempModel.addListener(new MockModelListener(this.logBlack));
    tempModel.addListener(new MockModelListener(this.logWhite));
    tempModel.startGame();
    
    // Check to see if all pieces are switched in color
    tempModel.playMove(PieceColor.BLACK, new SquarePosn(2, 2));
    Assert.assertEquals(tempModel.getPieceAt(new SquarePosn(2, 2)),
        Optional.of(PieceColor.BLACK));
    Assert.assertEquals(tempModel.getPieceAt(new SquarePosn(2, 1)),
        Optional.of(PieceColor.BLACK));
    Assert.assertEquals(tempModel.getPieceAt(new SquarePosn(2, 0)),
        Optional.of(PieceColor.BLACK));
    Assert.assertEquals(tempModel.getPieceAt(new SquarePosn(1, 1)),
        Optional.of(PieceColor.BLACK));
    Assert.assertEquals(tempModel.getPieceAt(new SquarePosn(0, 0)),
        Optional.of(PieceColor.BLACK));
  }
  
  @Test
  public void testMPlayMoveSwitchesTurn() {
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.BLACK);
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.WHITE);
  }
  
  @Test
  public void testMPlayMoveCallsPlayerListener() {
    this.model.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    Assert.assertEquals(this.logBlack.toString(), "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n"
        + "It's WHITE's move!\n"
        + "WHITE needs to play a move!\n");
    
    this.model.playMove(PieceColor.WHITE, new SquarePosn(2, -1));
    Assert.assertEquals(this.logBlack.toString(), "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n"
        + "It's WHITE's move!\n"
        + "WHITE needs to play a move!\n"
        + "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n");
  }
  
  // Pass
  @Test
  public void testMPassWhenGameIsOver() {
    this.sideLength = 2;
    this.initTest();
    Assert.assertThrows(IllegalStateException.class, () -> this.model.pass(PieceColor.BLACK));
  }
  
  @Test
  public void testMPassInvalidTurn() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.pass(PieceColor.WHITE));
  }
  
  @Test
  public void testMPassValid() {
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.BLACK);
    this.model.pass(PieceColor.BLACK);
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.WHITE);
    this.model.pass(PieceColor.WHITE);
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.BLACK);
  }
  
  @Test
  public void testMPassCallsModelListener() {
    this.model.pass(PieceColor.BLACK);
    Assert.assertEquals(this.logBlack.toString(), "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n"
        + "It's WHITE's move!\n"
        + "WHITE needs to play a move!\n");
    
    this.model.pass(PieceColor.WHITE);
    Assert.assertEquals(this.logWhite.toString(), "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n"
        + "It's WHITE's move!\n"
        + "WHITE needs to play a move!\n"
        + "It's game over! Stalemate!\n");
  }
  
  // SquarePosn Tests
  // Add
  @Test
  public void testSquarePosnAdd() {
    Assert.assertEquals(new SquarePosn(0, 0).add(this.roModel.getDirections()[5]),
        new SquarePosn(1, 1));
    Assert.assertEquals(new SquarePosn(-1, 0).add(this.roModel.getDirections()[3]),
        new SquarePosn(-2 , 0));
  }
  
  // Equals
  @Test
  public void testSquarePosnEquals() {
    Assert.assertNotEquals("(0, 0)", new SquarePosn(0, 0));
    Assert.assertNotEquals(new SquarePosn(0, 1), new SquarePosn(1, 0));
    Assert.assertEquals(new SquarePosn(0, 0), new SquarePosn(0, 0));
  }
  
  // toString
  @Test
  public void testSquarePosnToString() {
    Assert.assertEquals(new SquarePosn(0, 0).toString(), "(0, 0)");
    Assert.assertEquals(new SquarePosn(0, 1).toString(), "(0, 1)");
  }
}
