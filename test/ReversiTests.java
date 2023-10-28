import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.util.Optional;

public class ReversiTests {

  private Player player1;
  private Player player2;
  private IModel model;
  private IROModel roModel;
  private int numRings = 2;

  @Before
  public void initTest() {
    this.player1 = new AIPlayer(Color.WHITE);
    this.player2 = new AIPlayer(Color.BLACK);
    model = new Model(this.player1, this.player2, this.numRings);
    roModel = model.getReadOnlyModel();
  }

  // IROModel Tests (Observation Methods)
  // Constructor
  @Test
  public void testROMConstructorInvalidRingsNumberTooLow() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new ROModel(this.player1, this.player2, 0);
    });
  }

  @Test
  public void testROMConstructorNullPlayerPassedIn() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new ROModel(null, new AIPlayer(Color.WHITE), this.numRings);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new ROModel(new AIPlayer(Color.BLACK), null, this.numRings);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new ROModel(null, null, this.numRings);
    });
  }

  @Test
  public void testROMConstructorWorks() {
    Assert.assertEquals(this.roModel.getRings(), this.numRings);
    Assert.assertEquals(this.roModel.getTurn(), this.player1);
  }

  // IsGameOver
  @Test
  public void testROMIsGameOverFalseWhenGameIsNotOver() {
    Assert.assertFalse(this.roModel.isGameOver());
  }

  @Test
  public void testROMIsGameOverTrueWhenGameIsOver() {
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(this.player1, new Posn(1, 1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(this.player2, new Posn(3, 3));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(this.player1, new Posn(4, 1));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(this.player2, new Posn(3, 0));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(this.player1, new Posn(1, 4));
    Assert.assertFalse(this.roModel.isGameOver());
    this.model.playMove(this.player2, new Posn(0, 3));
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
    Assert.assertThrows(IllegalStateException.class, () -> {
      this.model.getWinner();
    });
  }

  @Test
  public void testROMGetWinnerStalemate() {
    this.numRings = 1;
    this.initTest();
    Assert.assertEquals(this.roModel.getWinner(), Optional.empty());
  }

  @Test
  public void testROMGetWinnerPlayer2Wins() {
    this.model.playMove(this.player1, new Posn(1, 1));
    this.model.playMove(this.player2, new Posn(3, 3));
    this.model.playMove(this.player1, new Posn(4, 1));
    this.model.playMove(this.player2, new Posn(3, 0));
    this.model.playMove(this.player1, new Posn(1, 4));
    this.model.playMove(this.player2, new Posn(0, 3));
    Assert.assertEquals(this.roModel.getWinner(), Optional.of(this.player2));
  }

  // GetPlayerAt
  @Test
  public void testROMGetPlayerAtInvalidCoordinates() {
    // Not in the HashMap but on the top left of the 2D array representation
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      this.roModel.getPlayerAt(new Posn(0, 0));
    });

    // Not in the HashMap but on the bottom right of the 2D array representation
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      this.roModel.getPlayerAt(new Posn(5, 5));
    });

    // Not in the HashMap or the 2D array representation
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      this.roModel.getPlayerAt(new Posn(100, 100));
    });
  }

  @Test
  public void testROMGetPlayerReturnsEmptyOptional() {
    Assert.assertEquals(Optional.empty(), this.roModel.getPlayerAt(new Posn(3, 3)));
  }

  @Test
  public void testROMGetPlayerReturnsOptionalOfAtPlayer() {
    Assert.assertEquals(Optional.of(this.player1), this.roModel.getPlayerAt(new Posn(1, 3)));
    Assert.assertEquals(Optional.of(this.player2), this.roModel.getPlayerAt(new Posn(1, 2)));
  }

  // GetTurn
  @Test
  public void testROMGetTurnAfterSwitchingTurn() {
    Assert.assertEquals(this.player1, this.roModel.getTurn());
    this.model.switchTurn();
    Assert.assertEquals(this.player2, this.roModel.getTurn());
  }

  @Test
  public void testROMGetTurnAfterMovingPlayer() {
    Assert.assertEquals(this.player1, this.roModel.getTurn());
    this.model.playMove(this.player1, new Posn(1, 1));
    Assert.assertEquals(this.player2, this.roModel.getTurn());
  }

  // GetRings
  @Test
  public void testROMGetRingsCorrectReturnValue() {
    Assert.assertEquals(this.numRings, this.roModel.getRings());
  }

  // IModel Tests (Operations Methods)
  // Constructor
  @Test
  public void testMConstructorInvalidRingsNumberTooLow() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Model(this.player1, this.player2, 0);
    });
  }

  @Test
  public void testMConstructorNullPlayerPassedIn() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Model(null, new AIPlayer(Color.WHITE), this.numRings);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Model(new AIPlayer(Color.BLACK), null, this.numRings);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Model(null, null, this.numRings);
    });
  }

  @Test
  public void testMConstructorWorks() {
    Assert.assertEquals(this.model.getRings(), this.numRings);
    Assert.assertEquals(this.model.getTurn(), this.player1);
  }

  // PlayMove
  @Test
  public void testMPlayMoveInvalidCoordinatesOutOfBounds() {
    // Not in the HashMap but on the top left of the 2D array representation
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      this.model.playMove(this.player1, new Posn(0, 0));
    });

    // Not in the HashMap but on the bottom right of the 2D array representation
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      this.model.playMove(this.player1, new Posn(5, 5));
    });

    // Not in the HashMap or the 2D array representation
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      this.model.playMove(this.player1, new Posn(100, 100));
    });
  }

  @Test
  public void testMPlayMoveInvalidPlacingOnOccupiedCell() {
    Assert.assertThrows(IllegalStateException.class, () -> {
      this.model.playMove(this.player1, new Posn(1, 2));
    });
  }

  @Test
  public void testMPlayMoveEmptyButInvalidCoordinates() {
    Assert.assertThrows(IllegalStateException.class, () -> {
      this.model.playMove(this.player1, new Posn(2, 2));
    });
  }

  @Test
  public void testMPlayMoveValidMoveCorrectlyUpdatesBoard() {
    // Updates 1 piece in between
    Assert.assertEquals(this.model.getPlayerAt(new Posn(1, 1)), Optional.empty());
    this.model.playMove(this.player1, new Posn(1, 1));
    Assert.assertEquals(this.model.getPlayerAt(new Posn(1, 1)), Optional.of(this.player1));
    Assert.assertEquals(this.model.getPlayerAt(new Posn(1, 2)), Optional.of(this.player1));

    this.model.playMove(this.player2, new Posn(0, 3));
    this.model.playMove(this.player1, new Posn(4, 1));

    // Updates 2 pieces in between
    this.model.playMove(this.player2, new Posn(3, 0));
    Assert.assertEquals(this.model.getPlayerAt(new Posn(3, 0)), Optional.of(this.player2));
    Assert.assertEquals(this.model.getPlayerAt(new Posn(2, 1)), Optional.of(this.player2));
    Assert.assertEquals(this.model.getPlayerAt(new Posn(1, 2)), Optional.of(this.player2));
  }

  @Test
  public void testMPlayMoveSwitchesTurn() {
    Assert.assertEquals(this.model.getTurn(), this.player1);
    this.model.playMove(this.player1, new Posn(1, 1));
    Assert.assertEquals(this.model.getTurn(), this.player2);
  }

  // SwitchTurn
  @Test
  public void testMSwitchesTurn() {
    Assert.assertEquals(this.model.getTurn(), this.player1);
    this.model.switchTurn();
    Assert.assertEquals(this.model.getTurn(), this.player2);
    this.model.switchTurn();
    Assert.assertEquals(this.model.getTurn(), this.player1);
  }

  // GetReadOnlyModel
  @Test
  public void testMGetReadOnlyModel() {
    Assert.assertEquals(this.model.getReadOnlyModel(), this.roModel);
  }
}
