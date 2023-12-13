package cs3500.reversi.controller;

import cs3500.reversi.model.hex.MockModel;
import cs3500.reversi.MockPlayer;
import cs3500.reversi.MockView;
import cs3500.reversi.model.hex.HexPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.Player;
import cs3500.reversi.view.IView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents a set of JUnit tests that NeedToDeleteTests the package-private functionality of the
 * cs3500.reversi.controller.
 */
public class PackagePrivateControllerTests {
  private StringBuilder mockModelLog;
  private StringBuilder mockPlayerLog;
  private StringBuilder mockViewLog;
  private ModelFeaturesImpl mfi;
  private PlayerFeaturesImpl pfi;
  
  @Before
  public void initTest() {
    int numRings = 3;
    this.mockPlayerLog = new StringBuilder();
    Player mockPlayer = new MockPlayer(this.mockPlayerLog);
    
    this.mockViewLog = new StringBuilder();
    IView mockView = new MockView(this.mockViewLog);
    
    this.mockModelLog = new StringBuilder();
    IModel mockModel = new MockModel(this.mockModelLog, new HashMap<>(), numRings);
    
    this.mfi = new ModelFeaturesImpl(mockPlayer, mockView, PieceColor.BLACK);
    this.pfi = new PlayerFeaturesImpl(mockModel, mockView, PieceColor.BLACK);
  }
  
  // ModelFeaturesImpl
  @Test
  public void testMFIPublishesForNotifyTurn() {
    this.mfi.notifyTurn(PieceColor.BLACK);
    Assert.assertEquals(this.mockViewLog.toString(), "itsTheTurnOf BLACK\n");
    Assert.assertEquals(this.mockPlayerLog.toString(), "");
  }
  
  @Test
  public void testMFIPublishesForPlayAMove() {
    this.mfi.playAMove(PieceColor.BLACK);
    Assert.assertEquals(this.mockViewLog.toString(), "");
    Assert.assertEquals(this.mockPlayerLog.toString(), "playAMove called\n");
  }
  
  @Test
  public void testMFIPublishesForItsGameOverStalemate() {
    this.mfi.itsGameOver(Optional.empty());
    Assert.assertEquals(this.mockViewLog.toString(),
        "promptMessage called with message: STALEMATE!\n");
    Assert.assertEquals(this.mockPlayerLog.toString(), "");
  }
  
  @Test
  public void testMFIPublishesForItsGameOverBlackWon() {
    this.mfi.itsGameOver(Optional.of(PieceColor.BLACK));
    Assert.assertEquals(this.mockViewLog.toString(),
        "promptMessage called with message: BLACK WON!\n");
    Assert.assertEquals(this.mockPlayerLog.toString(), "");
  }
  
  // PlayerFeaturesImpl
  @Test
  public void testPFIPublishesForPass() {
    this.pfi.pass();
   
    Assert.assertEquals(this.mockModelLog.toString(), "BLACK wants to pass.\n");
    Assert.assertEquals(this.mockViewLog.toString(), "refresh called\n");
  }
  
  @Test
  public void testPFIPublishesForPassInvalid() {
    this.pfi.pass();
    this.pfi.pass();
    
    Assert.assertEquals(this.mockModelLog.toString(), "BLACK wants to pass.\n"
        + "BLACK wants to pass.\n");
    Assert.assertEquals(this.mockViewLog.toString(), "refresh called\n"
        + "promptMessage called with message: Not this player's turn.\n");
  }
  
  @Test
  public void testPFIPublishesForMove() {
    this.pfi.move(new HexPosn(0, 0));
    
    Assert.assertEquals(this.mockModelLog.toString(), "BLACK wants to play (0, 0)\n");
    Assert.assertEquals(this.mockViewLog.toString(), "refresh called\n");
  }
  
  @Test
  public void testPFIPublishesForMoveInvalid() {
    this.pfi.move(new HexPosn(0, 0));
    this.pfi.move(new HexPosn(1, 1));
    
    Assert.assertEquals(this.mockModelLog.toString(), "BLACK wants to play (0, 0)\n"
        + "BLACK wants to play (1, 1)\n");
    Assert.assertEquals(this.mockViewLog.toString(), "refresh called\n"
        + "promptMessage called with message: Not this player's turn.\n");
  }
}
