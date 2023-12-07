package cs3500.reversi;

import cs3500.reversi.model.HexModel;
import cs3500.reversi.model.HexPosn;
import cs3500.reversi.model.Posn;
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
  private StringBuilder logStrategy;
  private StringBuilder logPlayerListener;
  private List<Posn> validMoves = new ArrayList<>();
  
  @Before
  public void initTest() {
    this.logStrategy = new StringBuilder();
    Player aiPlayer = new AIPlayer(new HexModel(5).getReadOnlyModel(),
        new MockStrategy(this.logStrategy, this.validMoves));
    this.logPlayerListener = new StringBuilder();
    aiPlayer.addListener(new MockPlayerListener(this.logPlayerListener));
    aiPlayer.playAMove();
  }
  
  @Test
  public void testPlayerPassIsCalled() {
    Assert.assertEquals(this.logStrategy.toString(), "chooseMove called.\n");
    Assert.assertEquals(this.logPlayerListener.toString(), "Pass was called\n");
  }
  
  @Test
  public void testPlayerMoveIsCalled() {
    this.validMoves = new ArrayList<>(List.of(
        new HexPosn(0, 0),
        new HexPosn(1, 1)));
    this.initTest();
    
    Assert.assertEquals(this.logStrategy.toString(), "chooseMove called.\n");
    Assert.assertEquals(this.logPlayerListener.toString(),
        "Move was called with axial position (0, 0).\n");
  }
}
