package cs3500.reversi;

import cs3500.reversi.model.hex.HexModel;
import cs3500.reversi.model.hex.HexPosn;
import cs3500.reversi.model.Posn;
import cs3500.reversi.model.square.SquareModel;
import cs3500.reversi.model.square.SquarePosn;
import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of JUnit tests that tests the functionality of the cs3500.reversi.player.
 */
public class ReversiPlayerTests {
  private StringBuilder logStrategyHex;
  private StringBuilder logPlayerListenerHex;
  private StringBuilder logStrategySquare;
  private StringBuilder logPlayerListenerSquare;
  private List<Posn> validMoves = new ArrayList<>();
  
  @Before
  public void initTest() {
    this.logStrategyHex = new StringBuilder();
    Player aiPlayerHex = new AIPlayer(new HexModel(5).getReadOnlyModel(),
        new MockStrategy(this.logStrategyHex, this.validMoves));
    this.logPlayerListenerHex = new StringBuilder();
    aiPlayerHex.addListener(new MockPlayerListener(this.logPlayerListenerHex));
    aiPlayerHex.playAMove();
    
    this.logStrategySquare = new StringBuilder();
    Player aiPlayerSquare = new AIPlayer(new SquareModel(6).getReadOnlyModel(),
        new MockStrategy(this.logStrategySquare, this.validMoves));
    this.logPlayerListenerSquare = new StringBuilder();
    aiPlayerSquare.addListener(new MockPlayerListener(this.logPlayerListenerSquare));
    aiPlayerSquare.playAMove();
  }
  
  // Hex
  @Test
  public void testPlayerPassIsCalled() {
    Assert.assertEquals(this.logStrategyHex.toString(), "chooseMove called.\n");
    Assert.assertEquals(this.logPlayerListenerHex.toString(), "Pass was called\n");
  }
  
  @Test
  public void testPlayerMoveIsCalled() {
    this.validMoves = new ArrayList<>(List.of(
        new HexPosn(0, 0),
        new HexPosn(1, 1)));
    this.initTest();
    
    Assert.assertEquals(this.logStrategyHex.toString(), "chooseMove called.\n");
    Assert.assertEquals(this.logPlayerListenerHex.toString(),
        "Move was called with position (0, 0).\n");
  }
  
  // Square
  @Test
  public void testSquarePlayerPassIsCalled() {
    Assert.assertEquals(this.logStrategySquare.toString(), "chooseMove called.\n");
    Assert.assertEquals(this.logPlayerListenerSquare.toString(), "Pass was called\n");
  }
  
  @Test
  public void testSquarePlayerMoveIsCalled() {
    this.validMoves = new ArrayList<>(List.of(
        new SquarePosn(2, 0),
        new SquarePosn(2, 1)));
    this.initTest();
    
    Assert.assertEquals(this.logStrategySquare.toString(), "chooseMove called.\n");
    Assert.assertEquals(this.logPlayerListenerSquare.toString(),
        "Move was called with position (2, 0).\n");
  }
}
