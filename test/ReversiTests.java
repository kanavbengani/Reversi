import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import model.Direction;
import model.IModel;
import model.IROModel;
import model.Model;
import model.AxialPosn;
import model.PieceColor;
import player.MockPlayer;
import player.PlayerListener;
import view.TextualView;

/**
 * Represents a set of JUnit tests that test the functionality of the model.
 */
public class ReversiTests {
  private IModel model;
  private IROModel roModel;
  private int numRings = 2;

  @Before
  public void initTest() {
    this.model = new Model(this.numRings);
    this.roModel = this.model.getReadOnlyModel();
  }

  // IROModel Tests (Observation Methods)
  // isMoveValid
  @Test
  public void testROMIsMoveValidValidMoves() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new AxialPosn(-1, -1)));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));

    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.WHITE, new AxialPosn(1, 1)));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, 1));

    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new AxialPosn(2, -1)));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(2, -1));
  }

  @Test
  public void testROMIsMoveValidInvalidMoveSameLocation() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new AxialPosn(-1, -1)));
    this.model.isMoveValid(PieceColor.BLACK, new AxialPosn(-1, -1));

    Assert.assertFalse(this.roModel.isMoveValid(PieceColor.WHITE, new AxialPosn(-1, 1)));
  }

  @Test
  public void testROMIsMoveValidInvalidMoveSamePlayer() {
    Assert.assertTrue(this.roModel.isMoveValid(PieceColor.BLACK, new AxialPosn(-1, -1)));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));

    Assert.assertFalse(this.roModel.isMoveValid(PieceColor.BLACK, new AxialPosn(1, 1)));
  }

  // IsGameOver
  @Test
  public void testROMIsGameOverFalseWhenGameIsNotOver() {
    Assert.assertFalse(this.roModel.isGameOver());
  }

  @Test
  public void testROMIsGameOverTrueWhenGameIsOver() {
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, 1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.BLACK, new AxialPosn(2, -1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, -2));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, 2));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(PieceColor.WHITE, new AxialPosn(-2, 1));
    Assert.assertTrue(this.roModel.isGameOver());
  }

  @Test
  public void testROMIsGameOverTrueWhenStalemate() {
    this.numRings = 1;
    this.initTest();
    Assert.assertTrue(this.roModel.isGameOver());
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
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, 1));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(2, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, -2));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, 2));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(-2, 1));
    Assert.assertEquals(this.roModel.getWinner(), Optional.of(PieceColor.WHITE));
  }

  // AnyLegalMoves
  @Test
  public void testROMAnyLegalMovesStartOfGame() {
    Assert.assertTrue(this.roModel.anyLegalMoves());
  }

  @Test
  public void testROMAnyLegalMovesAfterGameOver() {
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, 1));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(2, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, -2));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, 2));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(-2, 1));
    Assert.assertFalse(this.roModel.anyLegalMoves());
  }

  // GetPieceAt
  @Test
  public void testROMGetPieceAtOutOfBounds() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> this.roModel.getPieceAt(new AxialPosn(-2, -2)));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> this.roModel.getPieceAt(new AxialPosn(2, 2)));
  }

  @Test
  public void testROMGetPieceReturnsEmptyOptional() {
    Assert.assertEquals(Optional.empty(),
            this.roModel.getPieceAt(new AxialPosn(0, 0)));
  }

  @Test
  public void testROMGetPieceReturnsOptionalOfAtPiece() {
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
            this.roModel.getPieceAt(new AxialPosn(0, -1)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
            this.roModel.getPieceAt(new AxialPosn(1, -1)));
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
            this.roModel.getPieceAt(new AxialPosn(1, 0)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
            this.roModel.getPieceAt(new AxialPosn(0, 1)));
    Assert.assertEquals(Optional.of(PieceColor.BLACK),
            this.roModel.getPieceAt(new AxialPosn(-1, 1)));
    Assert.assertEquals(Optional.of(PieceColor.WHITE),
            this.roModel.getPieceAt(new AxialPosn(-1, 0)));

  }

  // GetTurn
  @Test
  public void testROMGetTurnAfterSwitchingTurn() {
    Assert.assertEquals(PieceColor.BLACK, this.roModel.getTurn());
    this.model.pass(PieceColor.BLACK);
    Assert.assertEquals(PieceColor.WHITE, this.roModel.getTurn());
  }

  @Test
  public void testROMGetTurnAfterMovingPiece() {
    Assert.assertEquals(PieceColor.BLACK, this.roModel.getTurn());
    this.model.playMove(PieceColor.BLACK, new AxialPosn(1, 1));
    Assert.assertEquals(PieceColor.WHITE, this.roModel.getTurn());
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
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, 1));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(2, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, -2));
    Assert.assertEquals(this.roModel.getScore(PieceColor.BLACK), 5);
    Assert.assertEquals(this.roModel.getScore(PieceColor.WHITE), 5);
  }

  // IModel Tests (Operations Methods)
  // Constructor
  @Test
  public void testMConstructorInvalidRingsNumberTooLow() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new Model(0));
  }

  @Test
  public void testMConstructorWorks() {
    Assert.assertEquals(this.model.getNumRings(), this.numRings);
    Assert.assertEquals(this.model.getTurn(), PieceColor.BLACK);
  }

  // PlayMove
  @Test
  public void testMPlayMoveInvalidCoordinatesOutOfBounds() {
    Assert.assertThrows(IllegalArgumentException.class, () -> this.model.playMove(PieceColor.BLACK,
            new AxialPosn(-2, -2)));

    Assert.assertThrows(IllegalArgumentException.class, () -> this.model.playMove(PieceColor.BLACK,
            new AxialPosn(2, 2)));
  }

  @Test
  public void testMPlayMoveInvalidPlacingOnOccupiedCell() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
            new AxialPosn(0, -1)));
  }

  @Test
  public void testMPlayMoveInvalidIllegalCoordinates() {
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
            new AxialPosn(0, 0)));
  }

  @Test
  public void testMPlayMoveInvalidIllegalPlayer() {
    Assert.assertThrows(IllegalArgumentException.class, () -> this.model.playMove(PieceColor.WHITE,
            new AxialPosn(1, 1)));
  }

  @Test
  public void testMPlayMoveInvalidAfterGameOver() {
    this.numRings = 1;
    this.initTest();
    Assert.assertThrows(IllegalStateException.class, () -> this.model.playMove(PieceColor.BLACK,
            new AxialPosn(1, 1)));
  }

  @Test
  public void testMPlayMoveValidMoveCorrectlyUpdatesCapturedPiece() {
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));
    Assert.assertEquals(this.model.getPieceAt(new AxialPosn(-1, -1)),
            Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.model.getPieceAt(new AxialPosn(-1, 0)),
            Optional.of(PieceColor.BLACK));
  }

  @Test
  public void testMPlayMoveValidMoveCorrectlyUpdatesMultipleCapturedPieces() {
    // Initial moves
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(-2, 1));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(2, -1));
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, 1));
    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, 2));

    // Check to see if all pieces are switched in color
    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, -2));
    Assert.assertEquals(this.model.getPieceAt(new AxialPosn(1, -2)),
            Optional.of(PieceColor.WHITE));
    Assert.assertEquals(this.model.getPieceAt(new AxialPosn(0, -1)),
            Optional.of(PieceColor.WHITE));
    Assert.assertEquals(this.model.getPieceAt(new AxialPosn(-1, 0)),
            Optional.of(PieceColor.WHITE));
    Assert.assertEquals(this.model.getPieceAt(new AxialPosn(1, -1)),
            Optional.of(PieceColor.WHITE));
    Assert.assertEquals(this.model.getPieceAt(new AxialPosn(1, 0)),
            Optional.of(PieceColor.WHITE));

  }

  @Test
  public void testMPlayMoveSwitchesTurn() {
    Assert.assertEquals(this.model.getTurn(), PieceColor.BLACK);
    this.model.playMove(PieceColor.BLACK, new AxialPosn(1, 1));
    Assert.assertEquals(this.model.getTurn(), PieceColor.WHITE);
  }

  @Test
  public void testMPlayMoveCallsPlayerListener() {
    StringBuilder log = new StringBuilder();
    this.model.addListener(new MockPlayer(log));

    this.model.playMove(PieceColor.BLACK, new AxialPosn(-1, -1));
    Assert.assertEquals(log.toString(), "it's O's move!\n");

    this.model.playMove(PieceColor.WHITE, new AxialPosn(1, 1));
    Assert.assertEquals(log.toString(), "it's O's move!\nit's X's move!\n");
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
    Assert.assertEquals(this.model.getTurn(), PieceColor.BLACK);
    this.model.pass(PieceColor.BLACK);
    Assert.assertEquals(this.model.getTurn(), PieceColor.WHITE);
    this.model.pass(PieceColor.WHITE);
    Assert.assertEquals(this.model.getTurn(), PieceColor.BLACK);
  }

  @Test
  public void testMPassCallsPlayerListener() {
    StringBuilder log = new StringBuilder();
    this.model.addListener(new MockPlayer(log));

    this.model.pass(PieceColor.BLACK);
    Assert.assertEquals(log.toString(), "it's O's move!\n");

    this.model.pass(PieceColor.WHITE);
    Assert.assertEquals(log.toString(), "it's O's move!\nit's X's move!\n");
  }

  // GetReadOnlyModel
  @Test
  public void testMGetReadOnlyModel() {
    Assert.assertEquals(this.model.getReadOnlyModel(), this.roModel);
  }

  // AddListener
  @Test
  public void testMAddListener() {
    StringBuilder log = new StringBuilder();
    this.model.addListener(new MockPlayer(log));

    StringBuilder log2 = new StringBuilder();
    this.model.addListener(new MockPlayer(log2));

    this.model.pass(PieceColor.BLACK);
    Assert.assertEquals(log.toString(), "it's O's move!\n");
    Assert.assertEquals(log2.toString(), "it's O's move!\n");

    this.model.pass(PieceColor.WHITE);
    Assert.assertEquals(log.toString(), "it's O's move!\nit's X's move!\n");
    Assert.assertEquals(log2.toString(), "it's O's move!\nit's X's move!\n");
  }

  // AxialPosn Tests
  // Add
  @Test
  public void testAxialPosnAdd() {
    Assert.assertEquals(new AxialPosn(0, 0).add(Direction.LEFT), new AxialPosn(-1, 0));
    Assert.assertEquals(new AxialPosn(-1, 0).add(Direction.DOWNRIGHT),
            new AxialPosn(-1 , 1));
  }

  // Equals
  @Test
  public void testAxialPosnEquals() {
    Assert.assertNotEquals("(0, 0)", new AxialPosn(0, 0));
    Assert.assertNotEquals(new AxialPosn(0, 1), new AxialPosn(1, 0));
    Assert.assertEquals(new AxialPosn(0, 0), new AxialPosn(0, 0));
  }

  // toString
  @Test
  public void testAxialPosnToString() {
    Assert.assertEquals(new AxialPosn(0, 0).toString(), "(0, 0)");
    Assert.assertEquals(new AxialPosn(0, 1).toString(), "(0, 1)");
  }

  // MockPlayer
  @Test
  public void testMockPlayer() {
    StringBuilder log = new StringBuilder();
    PlayerListener m1 = new MockPlayer(log);
    this.model.addListener(new MockPlayer(log));

    m1.itsTheMoveOf(PieceColor.BLACK);
    Assert.assertEquals(log.toString(), "it's X's move!\n");

    m1.itsTheMoveOf(PieceColor.WHITE);
    Assert.assertEquals(log.toString(), "it's X's move!\nit's O's move!\n");
  }

  // TextualView
  @Test
  public void testTextualView() {
    TextualView view = new TextualView(this.roModel);

    Assert.assertEquals(view.toString(),
            "  _ _ _ \n"
            + " _ X O _ \n"
            + "_ O _ X _ \n"
            + " _ X O _ \n"
            + "  _ _ _");
  }
}