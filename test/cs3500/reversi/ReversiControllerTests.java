package cs3500.reversi;

import cs3500.reversi.controller.Controller;
import cs3500.reversi.model.hex.HexPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.hex.HexModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.model.hex.MockHexModel;
import cs3500.reversi.model.square.SquareModel;
import cs3500.reversi.model.square.SquarePosn;
import cs3500.reversi.player.AIPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.strategy.CaptureMostStrategy;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;
import cs3500.reversi.view.ViewType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Represents a set of JUnit tests that tests the functionality of the cs3500.reversi.controller.
 */
public class ReversiControllerTests {
  private StringBuilder mockModelLogHex;
  private StringBuilder mockPlayerLogBlackHex;
  private StringBuilder mockPlayerLogWhiteHex;
  private StringBuilder mockViewLogBlackHex;
  private StringBuilder mockViewLogWhiteHex;
  private StringBuilder mockPlayerLogBlackSquare;
  private StringBuilder mockPlayerLogWhiteSquare;
  private StringBuilder mockViewLogBlackSquare;
  private StringBuilder mockViewLogWhiteSquare;
  private StringBuilder mockModelLogSquare;
  private Player playerSquare;
  private IModel hexModel;
  private IModel squareModel;
  private Player playerHex;
  
  @Before
  public void initTest() {
    int numRings = 3;
    
    this.hexModel = new HexModel(numRings);
    this.mockPlayerLogBlackHex = new StringBuilder();
    this.mockPlayerLogWhiteHex = new StringBuilder();
    this.mockViewLogBlackHex = new StringBuilder();
    this.mockViewLogWhiteHex = new StringBuilder();
    Player mockPlayerBlack = new MockPlayer(this.mockPlayerLogBlackHex);
    Player mockPlayerWhite = new MockPlayer(this.mockPlayerLogWhiteHex);
    IView mockViewBlack = new MockView(this.mockViewLogBlackHex);
    IView mockViewWhite = new MockView(this.mockViewLogWhiteHex);
    
    this.mockModelLogHex = new StringBuilder();
    IModel mockModelHex = new MockHexModel(this.mockModelLogHex, new HashMap<>(), numRings);
    IView viewHex = new View(ViewType.HEX, this.hexModel.getReadOnlyModel(), PieceColor.BLACK);
    this.playerHex = new AIPlayer(this.hexModel.getReadOnlyModel(), new CaptureMostStrategy());
    
    // Mock model to tests player and view are correctly publishing to model through controller.
    new Controller(mockModelHex, this.playerHex,
        viewHex, PieceColor.BLACK);
    
    // Mock player to tests model is correctly publishing to player and view through controller.
    new Controller(this.hexModel, mockPlayerBlack,
        mockViewBlack, PieceColor.BLACK);
    new Controller(this.hexModel, mockPlayerWhite,
        mockViewWhite, PieceColor.WHITE);
    
    // Square Specific
    this.squareModel = new SquareModel(numRings * 2);
    this.mockPlayerLogBlackSquare = new StringBuilder();
    this.mockPlayerLogWhiteSquare = new StringBuilder();
    this.mockViewLogBlackSquare = new StringBuilder();
    this.mockViewLogWhiteSquare = new StringBuilder();
    Player mockPlayerBlackSquare = new MockPlayer(this.mockPlayerLogBlackSquare);
    Player mockPlayerWhiteSquare = new MockPlayer(this.mockPlayerLogWhiteSquare);
    IView mockViewBlackSquare = new MockView(this.mockViewLogBlackSquare);
    IView mockViewWhiteSquare = new MockView(this.mockViewLogWhiteSquare);
    
    this.mockModelLogSquare = new StringBuilder();
    IModel mockModelSquare = new MockHexModel(this.mockModelLogSquare, new HashMap<>(), numRings);
    IView viewSquare = new View(ViewType.SQUARE, this.squareModel.getReadOnlyModel(), PieceColor.BLACK);
    this.playerSquare = new AIPlayer(this.squareModel.getReadOnlyModel(), new CaptureMostStrategy());
    
    // Mock model to tests player and view are correctly publishing to model through controller.
    new Controller(mockModelSquare, this.playerSquare,
        viewSquare, PieceColor.BLACK);
    
    // Mock player to tests model is correctly publishing to player and view through controller.
    new Controller(this.squareModel, mockPlayerBlackSquare,
        mockViewBlackSquare, PieceColor.BLACK);
    new Controller(this.squareModel, mockPlayerWhiteSquare,
        mockViewWhiteSquare, PieceColor.WHITE);
  }
  
  // Controller with Hex
  @Test
  public void testControllerAllComponentsListenCorrectly() {
    Assert.assertEquals(this.mockModelLogHex.toString(), "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackHex.toString(), "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogBlackHex.toString(), "addListener called in MockView.\n");
    Assert.assertEquals(this.mockPlayerLogWhiteHex.toString(), "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhiteHex.toString(), "addListener called in MockView.\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterStartGame() {
    this.hexModel.startGame();
    
    Assert.assertEquals(this.mockModelLogHex.toString(),
        "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackHex.toString(),
        "addListener called in MockPlayer.\n"
            + "playAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
    Assert.assertEquals(this.mockPlayerLogWhiteHex.toString(),
        "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhiteHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterPass() {
    this.hexModel.startGame();
    this.hexModel.pass(PieceColor.BLACK);
    
    Assert.assertEquals(this.mockModelLogHex.toString(),
        "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackHex.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
    Assert.assertEquals(this.mockPlayerLogWhiteHex.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogWhiteHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterPlayAMove() {
    this.hexModel.startGame();
    this.hexModel.playMove(PieceColor.BLACK, new HexPosn(2, -1));
    
    Assert.assertEquals(this.mockModelLogHex.toString(),
        "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackHex.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
    Assert.assertEquals(this.mockPlayerLogWhiteHex.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogWhiteHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
  }
  
  @Test
  public void testControllerAllComponentsListenCorrectlyAfterPlayerPlaysAMove() {
    this.hexModel.startGame();
    this.playerHex.playAMove();
    
    Assert.assertEquals(this.mockModelLogHex.toString(),
        "addListener called in MockHexModel.\n"
            + "BLACK wants to play (1, -2)\n");
    Assert.assertEquals(this.mockPlayerLogBlackHex.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
    Assert.assertEquals(this.mockPlayerLogWhiteHex.toString(),
        "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhiteHex.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
  }
  
  // Square
  // Controller with Hex
  @Test
  public void testSquareControllerAllComponentsListenCorrectly() {
    Assert.assertEquals(this.mockModelLogHex.toString(), "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackHex.toString(), "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogBlackHex.toString(), "addListener called in MockView.\n");
    Assert.assertEquals(this.mockPlayerLogWhiteHex.toString(), "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhiteHex.toString(), "addListener called in MockView.\n");
  }
  
  @Test
  public void testSquareControllerAllComponentsListenCorrectlyAfterStartGame() {
    this.squareModel.startGame();
    
    Assert.assertEquals(this.mockModelLogSquare.toString(),
        "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackSquare.toString(),
        "addListener called in MockPlayer.\n"
            + "playAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
    Assert.assertEquals(this.mockPlayerLogWhiteSquare.toString(),
        "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhiteSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
  }
  
  @Test
  public void testSquareControllerAllComponentsListenCorrectlyAfterPass() {
    this.squareModel.startGame();
    this.squareModel.pass(PieceColor.BLACK);
    
    Assert.assertEquals(this.mockModelLogSquare.toString(),
        "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackSquare.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
    Assert.assertEquals(this.mockPlayerLogWhiteSquare.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogWhiteSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
  }
  
  @Test
  public void testSquareControllerAllComponentsListenCorrectlyAfterPlayAMove() {
    this.squareModel.startGame();
    this.squareModel.playMove(PieceColor.BLACK, new SquarePosn(2, 0));
    
    Assert.assertEquals(this.mockModelLogSquare.toString(),
        "addListener called in MockHexModel.\n");
    Assert.assertEquals(this.mockPlayerLogBlackSquare.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
    Assert.assertEquals(this.mockPlayerLogWhiteSquare.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogWhiteSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\nitsTheTurnOf WHITE\n");
  }
  
  @Test
  public void testSquareControllerAllComponentsListenCorrectlyAfterPlayerPlaysAMove() {
    this.squareModel.startGame();
    this.playerSquare.playAMove();
    
    Assert.assertEquals(this.mockModelLogSquare.toString(),
        "addListener called in MockHexModel.\n"
            + "BLACK wants to play (1, -1)\n");
    Assert.assertEquals(this.mockPlayerLogBlackSquare.toString(),
        "addListener called in MockPlayer.\nplayAMove called\n");
    Assert.assertEquals(this.mockViewLogBlackSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
    Assert.assertEquals(this.mockPlayerLogWhiteSquare.toString(),
        "addListener called in MockPlayer.\n");
    Assert.assertEquals(this.mockViewLogWhiteSquare.toString(),
        "addListener called in MockView.\n"
            + "itsTheTurnOf BLACK\n");
  }
}
