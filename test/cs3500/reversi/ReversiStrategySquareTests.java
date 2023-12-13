package cs3500.reversi;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Posn;
import cs3500.reversi.model.square.SquareModel;
import cs3500.reversi.model.square.SquarePosn;
import cs3500.reversi.model.square.MockSquareModel;
import cs3500.reversi.strategy.AndStrategy;
import cs3500.reversi.strategy.AvoidEdgesStrategy;
import cs3500.reversi.strategy.CaptureMostStrategy;
import cs3500.reversi.strategy.GoCornerStrategy;
import cs3500.reversi.strategy.MinimaxStrategyDepth;
import cs3500.reversi.strategy.ReversiStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a set of JUnit tests that tests the functionality of the cs3500.reversi.strategy.
 */
public class ReversiStrategySquareTests {
  private StringBuilder log;
  private final Map<Posn, Integer> validPosn = new HashMap<>();
  private int numRings;
  private IModel mockModel;
  private IModel fullModel;
  private ReversiStrategy captureMostStrategy;
  private ReversiStrategy avoidEdgesStrategy;
  private ReversiStrategy goCornersStrategy;
  private ReversiStrategy minimaxStrategy;
  
  @Before
  public void initTest() {
    this.log = new StringBuilder();
    this.validPosn.put(new SquarePosn(1, -2), 5);
    this.validPosn.put(new SquarePosn(2, -1), 3);
    this.validPosn.put(new SquarePosn(1, 1), 1);
    this.numRings = 4;
    
    this.mockModel = new MockSquareModel(this.log, this.validPosn, this.numRings);
    this.fullModel = new SquareModel(this.numRings);
    this.captureMostStrategy = new CaptureMostStrategy();
    this.avoidEdgesStrategy = new AvoidEdgesStrategy();
    this.goCornersStrategy = new GoCornerStrategy();
    this.minimaxStrategy = new MinimaxStrategyDepth(new AndStrategy(
        new GoCornerStrategy(),
        new AndStrategy(new AvoidEdgesStrategy(), new CaptureMostStrategy())), 5);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();
  }
  
  // CaptureMostStrategy
  @Test
  public void testCaptureMostStrategyCallsCorrectMethodsOnMock() {
    this.captureMostStrategy.chooseMove(new ArrayList<>(), mockModel);
    
    for (Posn posn : this.mockModel.getAllPosn()) {
      Assert.assertTrue(this.log.toString().contains("Calling isMoveValid to check if BLACK"
          + " can play on " + posn.toString()));
    }
  }
  
  @Test
  public void testCaptureMostStrategyGetsCorrectMove() {
    this.validPosn.put(new SquarePosn(2, -1), 5);
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), mockModel).get(0),
        new SquarePosn(1, -2)); // The "best" move according to this strategy [index 0]
  }
  
  @Test
  public void testCaptureMostStrategyMultipleMovesSameNumCapturesSortCorrectly() {
    this.validPosn.put(new SquarePosn(2, -1), 5);
    this.validPosn.put(new SquarePosn(1, 1), 5);
    // Mock model assumes the given list (validPosn) are the valid moves and returns
    // true accordingly to the strategy.
    
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), mockModel),
        List.of(new SquarePosn(1, -2), new SquarePosn(2, -1),
            new SquarePosn(1, 1)));
  }
  
  @Test
  public void testCaptureMostStrategyNoCaptures() {
    this.validPosn.clear();
    Assert.assertEquals(this.captureMostStrategy.chooseMove(
        new ArrayList<>(), this.mockModel).size(),0);
  }
  
  @Test
  public void testCaptureMostStrategyTakesPossibleMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new SquarePosn(2, -1)),
        this.mockModel), List.of(new SquarePosn(2, -1)));
  }
  
  @Test
  public void testCaptureMostStrategyDisjointMovesAndValidMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new SquarePosn(3, -2)),
        this.mockModel).size(), 0);
  }
  
  @Test
  public void testAvoidEdgesStrategyCorrectlyAvoidsEdgeWithOptionsProvided() {
    this.validPosn.clear();
    this.validPosn.put(new SquarePosn(0, -2), 1);
    this.validPosn.put(new SquarePosn(0, 1), 1);
    this.validPosn.put(new SquarePosn(-1, 2), 1);
    
    Assert.assertEquals(
        this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new SquarePosn(0, 1))),
            this.mockModel), List.of(new SquarePosn(0, 1)));
  }
  
  @Test
  public void testAvoidEdgesStrategyFindsNoMovesWhenAllMovesAreEdgeAdjacentMovesPassedIn() {
    this.validPosn.clear();
    this.validPosn.put(new SquarePosn(0, -2), 1);
    this.validPosn.put(new SquarePosn(2, -2), 1);
    this.validPosn.put(new SquarePosn(-2, 2), 1);
    
    Assert.assertEquals(
        this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new SquarePosn(-2, 0))),
            this.mockModel),
        new ArrayList<>());
  }
  
  @Test
  public void testGoCornerStrategyReturnsEmptyWhenNoCornerMoves() {
    this.validPosn.clear();
    this.validPosn.put(new SquarePosn(2, 0), 1);
    this.validPosn.put(new SquarePosn(-1, -3), 1);
    this.validPosn.put(new SquarePosn(-2, -1), 2);
    
    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(),
        this.mockModel), new ArrayList<>());
  }
}
