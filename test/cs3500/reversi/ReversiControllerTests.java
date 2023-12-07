package cs3500.reversi;

import cs3500.reversi.controller.Controller;
import cs3500.reversi.model.HexPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.HexModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.strategy.CaptureMostStrategy;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Represents a set of JUnit tests that tests the functionality of the cs3500.reversi.controller.
 */
public class ReversiControllerTests {
  private StringBuilder mockModelLog;
  private StringBuilder mockPlayerLogBlack;
  private StringBuilder mockPlayerLogWhite;
  private StringBuilder mockViewLogBlack;
  private StringBuilder mockViewLogWhite;
  private IModel model;
  private Player player;
  
  
  @Before
  public void initTest() {
    int numRings = 3;
    
    this.model = new HexModel(numRings);
    this.mockPlayerLogBlack = new StringBuilder();
    this.mockPlayerLogWhite = new StringBuilder();
    this.mockViewLogBlack = new StringBuilder();
    this.mockViewLogWhite = new StringBuilder();
    Player mockPlayerBlack = new MockPlayer(this.mockPlayerLogBlack);
    Player mockPlayerWhite = new MockPlayer(this.mockPlayerLogWhite);
    IView mockViewBlack = new MockView(this.mockViewLogBlack);
    IView mockViewWhite = new MockView(this.mockViewLogWhite);
    
    this.mockModelLog = new StringBuilder();
    IModel mockModel = new MockModel(this.mockModelLog, new HashMap<>(), numRings);
    IView view = new View(this.model.getReadOnlyModel(), PieceColor.BLACK);
    this.player = new AIPlayer(this.model.getReadOnlyModel(), new CaptureMostStrategy());
    
    
    // Mock model to tests player and view are correctly publishing to model through controller.
    new Controller(mockModel, this.player,
        view, PieceColor.BLACK);
    
    // Mock player to tests model is correctly publishing to player and view through controller.
    new Controller(this.model, mockPlayerBlack,
        mockViewBlack, PieceColor.BLACK);
    new Controller(this.model, mockPlayerWhite,
        mockViewWhite, PieceColor.WHITE);
  }
  
  // Controller
  @Test
  public void testControllerAllComponentsListenCorrectly() {
    Assert.assertEquals(this.mockModelLog.toString(), "addListener called in MockModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlack.toString(), "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogBlack.toString(), "addListener called in MockView.\n");
    Assert.assertEquals(this.mockPlayerLogWhite.toString(), "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhite.toString(), "addListener called in MockView.\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterStartGame() {
    this.model.startGame();
    
    Assert.assertEquals(this.mockModelLog.toString(),
        "addListener called in MockModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlack.toString(),
        "addListener called in MockPlayer.\n"
            + "playAMove called\n");
    Assert.assertEquals(this.mockViewLogBlack.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
    Assert.assertEquals(this.mockPlayerLogWhite.toString(),
        "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhite.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterPass() {
    this.model.startGame();
    this.model.pass(PieceColor.BLACK);
    
    Assert.assertEquals(this.mockModelLog.toString(),
        "addListener called in MockModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlack.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlack.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
    Assert.assertEquals(this.mockPlayerLogWhite.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogWhite.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterPlayAMove() {
    this.model.startGame();
    this.model.playMove(PieceColor.BLACK, new HexPosn(2, -1));
    
    Assert.assertEquals(this.mockModelLog.toString(),
        "addListener called in MockModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlack.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlack.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
    Assert.assertEquals(this.mockPlayerLogWhite.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogWhite.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterPlayerPlaysAMove() {
    this.model.startGame();
    this.player.playAMove();
    
    Assert.assertEquals(this.mockModelLog.toString(),
        "addListener called in MockModel.\n"
            + "BLACK wants to play (1, -2)\n");
    Assert.assertEquals(this.mockPlayerLogBlack.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlack.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
    Assert.assertEquals(this.mockPlayerLogWhite.toString(),
        "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhite.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
  }
}
