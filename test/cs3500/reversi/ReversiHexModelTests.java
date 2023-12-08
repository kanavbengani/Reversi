package cs3500.reversi;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.model.Posn;
import cs3500.reversi.model.hex.HexModel;
import cs3500.reversi.view.hex.HexTextualView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cs3500.reversi.model.hex.HexPosn;

/**
 * Represents a set of JUnit tests that tests the functionality of the cs3500.reversi.model.
 */
public class ReversiHexModelTests {
  private IModel model;
  private IROModel roModel;
  private int numRings = 2;
  
  private StringBuilder logBlack;
  private StringBuilder logWhite;

  @Before
  public void initTest() {
    this.model = new HexModel(this.numRings);
    this.roModel = this.model.getReadOnlyModel();
    this.logBlack = new StringBuilder();
    this.logWhite = new StringBuilder();
    this.model.addListener(new MockModelListener(this.logBlack));
    this.model.addListener(new MockModelListener(this.logWhite));
    this.model.startGame();
  }

  // IROModel Tests (Observation Methods)
  // isMoveValid
  @Test
  public void testROMIsMoveValidValidMoves() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new HexPosn(-1, -1)));
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));

    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.WHITE, new HexPosn(1, 1)));
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, 1));

    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new HexPosn(2, -1)));
    this.model.playMove(PieceColor.BLACK, new HexPosn(2, -1));
  }

  @Test
  public void testROMIsMoveValidInvalidMoveSameLocation() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new HexPosn(-1, -1)));
    this.model.isMoveValid(PieceColor.BLACK, new HexPosn(-1, -1));

    Assert.assertFalse(this.roModel.isMoveValid(PieceColor.WHITE, new HexPosn(-1, 1)));
  }

  @Test
  public void testROMIsMoveValidInvalidMoveSamePlayer() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new HexPosn(-1, -1)));
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));

    Assert.assertFalse(this.roModel.isMoveValid(PieceColor.BLACK, new HexPosn(1, 1)));
  }

  // IsGameOver
  @Test
  public void testROMIsGameOverFalseWhenGameIsNotOver() {
    Assert.assertFalse(this.roModel.isGameOver());
  }

  @Test
  public void testROMIsGameOverTrueWhenGameIsOver() {
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, 1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.BLACK, new HexPosn(2, -1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, -2));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, 2));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.WHITE, new HexPosn(-2, 1));
    Assert.assertTrue(this.roModel.isGameOver());
  }

  @Test
  public void testROMIsGameOverTrueWhenStalemate() {
    this.numRings = 1;
    this.initTest();
    Assert.assertTrue(this.roModel.isGameOver());
  }
  
  // GetAllCapturedPieces
  @Test
  public void testGetAllCapturedPiecesInvalidTurn() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.WHITE, new HexPosn(0, 0)));
  }
  
  @Test
  public void testGetAllCapturedPiecesInvalidMoveOutOfBounds() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.BLACK, new HexPosn(10, 10)));
  }
  
  @Test
  public void testGetAllCapturedPiecesInvalidMoveOnOccupiedCell() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.BLACK, new HexPosn(1, 0)));
  }
  
  @Test
  public void testGetAllCapturedPiecesInvalidMove() {
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.getAllCapturedPieces(PieceColor.BLACK, new HexPosn(0, 0)));
  }
  
  @Test
  public void testGetAllCapturedPiecesValidMove() {
    Assert.assertEquals(this.model.getAllCapturedPieces(PieceColor.BLACK,
        new HexPosn(1, -2)), new ArrayList<>(List.of(new HexPosn(1, -1))));
  }
  
  // GetAllPosn
  @Test
  public void testGetAllPosn() {
    this.model = new HexModel(1);
    Assert.assertEquals(this.model.getAllPosn(), new ArrayList<>(List.of(
        new HexPosn(0, -1), new HexPosn(1, 0), new HexPosn(0, 0),
        new HexPosn(-1, 0), new HexPosn(0, 1), new HexPosn(-1, 1),
        new HexPosn(1, -1))));
  }
  
  // Copy
  @Test
  public void testCopyReturnsANonMutableReference() {
    Assert.assertNotEquals(this.model, this.model.copy());
  }
  
  @Test
  public void testCopyMakesAValidCopy() {
    this.numRings = 1;
    this.initTest();
    Assert.assertEquals(this.model.getNumRings(), this.model.copy().getNumRings());
    Assert.assertEquals(this.model.getTurnColor(), this.model.copy().getTurnColor());
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(0, 0)),
        this.model.copy().getPieceAt(new HexPosn(0, 0)));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(1, 0)),
        this.model.copy().getPieceAt(new HexPosn(1, 0)));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(0, 1)),
        this.model.copy().getPieceAt(new HexPosn(0, 1)));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(-1, 1)),
        this.model.copy().getPieceAt(new HexPosn(-1, 1)));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(-1, 0)),
        this.model.copy().getPieceAt(new HexPosn(-1, 0)));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(0, -1)),
        this.model.copy().getPieceAt(new HexPosn(0, -1)));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(1, -1)),
        this.model.copy().getPieceAt(new HexPosn(1, -1)));
  }
  
  // GetWinner
  @Test
  public void testROMGetWinnerExceptionThrownWhenGameIsNotOver() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.getWinner());
  }

  @Test
  public void testROMGetWinnerStalemate() {
    this.numRings = 1;
    this.initTest();
    Assert.assertEquals(this.roModel.getWinner(), Optional.empty());
  }

  @Test
  public void testROMGetWinnerPiece2Wins() {
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, 1));
    this.model.playMove(PieceColor.BLACK, new HexPosn(2, -1));
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, -2));
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, 2));
    this.model.playMove(PieceColor.WHITE, new HexPosn(-2, 1));
    Assert.assertEquals(this.roModel.getWinner(), Optional.of(PieceColor.WHITE));
  }

  // AnyLegalMoves
  @Test
  public void testROMAnyLegalMovesStartOfGame() {
    Assert.assertTrue(this.roModel.anyLegalMoves(this.roModel.getTurnColor()));
  }

  @Test
  public void testROMAnyLegalMovesAfterGameOver() {
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, 1));
    this.model.playMove(PieceColor.BLACK, new HexPosn(2, -1));
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, -2));
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, 2));
    this.model.playMove(PieceColor.WHITE, new HexPosn(-2, 1));
    Assert.assertFalse(this.roModel.anyLegalMoves(this.roModel.getTurnColor()));
  }

  // GetPieceAt
  @Test
  public void testROMGetPieceAtOutOfBounds() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> this.roModel.getPieceAt(new HexPosn(-2, -2)));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> this.roModel.getPieceAt(new HexPosn(2, 2)));
  }

  @Test
  public void testROMGetPieceReturnsEmptyOptional() {
    Assert.assertEquals(Optional.empty(),
            this.roModel.getPieceAt(new HexPosn(0, 0)));
  }

  @Test
  public void testROMGetPieceReturnsOptionalOfAtPiece() {
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
            this.roModel.getPieceAt(new HexPosn(0, -1)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
            this.roModel.getPieceAt(new HexPosn(1, -1)));
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
            this.roModel.getPieceAt(new HexPosn(1, 0)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
            this.roModel.getPieceAt(new HexPosn(0, 1)));
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
            this.roModel.getPieceAt(new HexPosn(-1, 1)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
            this.roModel.getPieceAt(new HexPosn(-1, 0)));

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
    this.model.playMove(PieceColor.BLACK, new HexPosn(1, 1));
    Assert.assertEquals(PieceColor.WHITE, this.roModel.getTurnColor());
  }

  // GetRings
  @Test
  public void testROMGetRingsCorrectReturnValue() {
    Assert.assertEquals(this.numRings, this.roModel.getNumRings());
  }

  // GetScore
  @Test
  public void testROMGetScoreForEmptyBoard() {
    Assert.assertEquals(this.roModel.getScore(PieceColor.BLACK), 3);
    Assert.assertEquals(this.roModel.getScore(PieceColor.WHITE), 3);
  }

  @Test
  public void testROMGetScoreAfterPlayingSomeMoves() {
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, 1));
    this.model.playMove(PieceColor.BLACK, new HexPosn(2, -1));
    this.model.playMove(PieceColor.WHITE, new HexPosn(1, -2));
    Assert.assertEquals(this.roModel.getScore(PieceColor.BLACK), 5);
    Assert.assertEquals(this.roModel.getScore(PieceColor.WHITE), 5);
  }
  
  // AddListener
  @Test
  public void testMAddListener() {
    // Listeners are added correctly because start game passes after two are added.
    this.model = new HexModel(this.numRings);
    Assert.assertThrows(IllegalStateException.class, () -> this.model.startGame());
    this.model.addListener(new MockModelListener(new StringBuilder()));
    Assert.assertThrows(IllegalStateException.class, () -> this.model.startGame());
    this.model.addListener(new MockModelListener(new StringBuilder()));
    this.model.startGame();
  }

  // Constructor
  @Test
  public void testMConstructorInvalidRingsNumberTooLow() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new HexModel(0));
  }

  @Test
  public void testMConstructorWorks() {
    Assert.assertEquals(this.model.getNumRings(), this.numRings);
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.BLACK);
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
    this.model = new HexModel(this.numRings);
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
            new HexPosn(-2, -2)));

    Assert.assertThrows(IllegalArgumentException.class, () -> this.model.playMove(PieceColor.BLACK,
            new HexPosn(2, 2)));
  }

  @Test
  public void testMPlayMoveInvalidPlacingOnOccupiedCell() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
            new HexPosn(0, -1)));
  }

  @Test
  public void testMPlayMoveInvalidIllegalCoordinates() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
            new HexPosn(0, 0)));
  }

  @Test
  public void testMPlayMoveInvalidIllegalPlayer() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.WHITE,
            new HexPosn(1, 1)));
  }

  @Test
  public void testMPlayMoveInvalidAfterGameOver() {
    this.numRings = 1;
    this.initTest();
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
            new HexPosn(1, 1)));
  }

  @Test
  public void testMPlayMoveValidMoveCorrectlyUpdatesCapturedPiece() {
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(-1, -1)),
            Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(-1, 0)),
            Optional.of(PieceColor.BLACK));
  }

  @Test
  public void testMPlayMoveValidMoveCorrectlyUpdatesMultipleCapturedPieces() {
    // Initial moves
    int start = 0;
    int end = 2;
    Map<Posn, Optional<PieceColor>> board = new HashMap<>();
    for (int r = -2; r <= 2; r++) {
      for (int q = start; q <= end; q++) {
        HexPosn hp = new HexPosn(q, r);
        board.put(hp, Optional.empty());
      }

      if (start == -2) {
        end--;
      } else {
        start--;
      }
    }

    board.put(new HexPosn(-2, 1), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(-1, 0), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(0, -1), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(1, -1), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(1, 0), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(1, 1), Optional.of(PieceColor.BLACK));

    this.model = new HexModel(board);
    this.model.addListener(new MockModelListener(new StringBuilder()));
    this.model.addListener(new MockModelListener(new StringBuilder()));
    this.model.startGame();

    // Check to see if all pieces are switched in color
    this.model.playMove(PieceColor.BLACK, new HexPosn(1, -2));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(1, -2)),
            Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(0, -1)),
            Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(-1, 0)),
            Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(1, -1)),
            Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.model.getPieceAt(new HexPosn(1, 0)),
            Optional.of(PieceColor.BLACK));

  }

  @Test
  public void testMPlayMoveSwitchesTurn() {
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.BLACK);
    this.model.playMove(PieceColor.BLACK, new HexPosn(1, 1));
    Assert.assertEquals(this.model.getTurnColor(), PieceColor.WHITE);
  }

  @Test
  public void testMPlayMoveCallsPlayerListener() {
    this.model.playMove(PieceColor.BLACK, new HexPosn(-1, -1));
    Assert.assertEquals(this.logBlack.toString(), "It's BLACK's move!\n"
        + "BLACK needs to play a move!\n"
        + "It's WHITE's move!\n"
        + "WHITE needs to play a move!\n");

    this.model.playMove(PieceColor.WHITE, new HexPosn(1, 1));
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
    this.numRings = 1;
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

  // HexPosn Tests
  // Add
  @Test
  public void testAxialPosnAdd() {
    Assert.assertEquals(new HexPosn(0, 0).add(this.roModel.getDirections()[5]),
        new HexPosn(-1, 0));
    Assert.assertEquals(new HexPosn(-1, 0).add(this.roModel.getDirections()[3]),
            new HexPosn(-1 , 1));
  }

  // Equals
  @Test
  public void testAxialPosnEquals() {
    Assert.assertNotEquals("(0, 0)", new HexPosn(0, 0));
    Assert.assertNotEquals(new HexPosn(0, 1), new HexPosn(1, 0));
    Assert.assertEquals(new HexPosn(0, 0), new HexPosn(0, 0));
  }

  // toString
  @Test
  public void testAxialPosnToString() {
    Assert.assertEquals(new HexPosn(0, 0).toString(), "(0, 0)");
    Assert.assertEquals(new HexPosn(0, 1).toString(), "(0, 1)");
  }

  // HexTextualView
  @Test
  public void testTextualView() {
    HexTextualView view = new HexTextualView(this.roModel);

    Assert.assertEquals(view.toString(),
            "  _ _ _ \n"
            + " _ X O _ \n"
            + "_ O _ X _ \n"
            + " _ X O _ \n"
            + "  _ _ _");
  }
}