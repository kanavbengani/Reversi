package cs3500.reversi;

import cs3500.reversi.model.*;
import cs3500.reversi.model.hex.HexModel;
import cs3500.reversi.model.hex.HexPosn;
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
import java.util.Optional;

/**
 * Represents a set of JUnit tests that tests the functionality of the cs3500.reversi.strategy.
 */
public class ReversiStrategyTests {
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
    this.validPosn.put(new HexPosn(1, -2), 5);
    this.validPosn.put(new HexPosn(2, -1), 3);
    this.validPosn.put(new HexPosn(1, 1), 1);
    this.numRings = 3;

    this.mockModel = new MockModel(this.log, this.validPosn, this.numRings);
    this.fullModel = new HexModel(this.numRings);
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
    this.validPosn.put(new HexPosn(2, -1), 5);
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), mockModel).get(0),
            new HexPosn(1, -2)); // The "best" move according to this strategy [index 0]
  }

  @Test
  public void testCaptureMostStrategyMultipleMovesSameNumCapturesSortCorrectly() {
    this.validPosn.put(new HexPosn(2, -1), 5);
    this.validPosn.put(new HexPosn(1, 1), 5);
    // Mock model assumes the given list (validPosn) are the valid moves and returns
    // true accordingly to the strategy.

    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), mockModel),
            List.of(new HexPosn(1, -2), new HexPosn(2, -1),
            new HexPosn(1, 1)));
  }

  @Test
  public void testCaptureMostStrategyNoCaptures() {
    this.validPosn.clear();
    Assert.assertEquals(this.captureMostStrategy.chooseMove(
            new ArrayList<>(), this.mockModel).size(),0);
  }

  @Test
  public void testCaptureMostStrategyTakesPossibleMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new HexPosn(2, -1)),
            this.mockModel), List.of(new HexPosn(2, -1)));
  }

  @Test
  public void testCaptureMostStrategyDisjointMovesAndValidMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new HexPosn(3, -2)),
            this.mockModel).size(), 0);
  }

  // AvoidEdgesStrategy
  @Test
  public void testAvoidEdgesStrategyCallsCorrectMethods() {
    this.avoidEdgesStrategy.chooseMove(new ArrayList<>(), this.mockModel);

    int n = this.numRings;
    List<HexPosn> edges = new ArrayList<>(List.of(
            new HexPosn(-1, -n + 1), new HexPosn(0, -n + 1), new HexPosn(1, -n),
            new HexPosn(n - 1, -n), new HexPosn(n - 1, -n + 1), new HexPosn(n, -n + 1),
            new HexPosn(n, -1), new HexPosn(n - 1, 0), new HexPosn(n - 1, 1),
            new HexPosn(1, n - 1), new HexPosn(0, n - 1), new HexPosn(-1, n),
            new HexPosn(-n + 1, n), new HexPosn(-n + 1, n - 1), new HexPosn(-n, n - 1),
            new HexPosn(-n, 1), new HexPosn(-n + 1, 0), new HexPosn(-n + 1, -1)
    ));

    for (HexPosn hp : edges) {
      Assert.assertTrue(this.log.toString().contains("Calling isMoveValid to check if BLACK can "
              + "play on " + hp.toString()));
    }
  }

  @Test
  public void testAvoidEdgesStrategyCorrectlyAvoidsEdgeWithNoOptionLeft() {
    this.validPosn.clear();
    this.validPosn.put(new HexPosn(0, -2), 1);
    this.validPosn.put(new HexPosn(0, 1), 1);

    Assert.assertEquals(this.avoidEdgesStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), List.of(new HexPosn(0, 1)));
  }

  @Test
  public void testAvoidEdgesStrategyCorrectlyAvoidsEdgeWithOptionsProvided() {
    this.validPosn.clear();
    this.validPosn.put(new HexPosn(0, -2), 1);
    this.validPosn.put(new HexPosn(0, 1), 1);
    this.validPosn.put(new HexPosn(-1, 2), 1);

    Assert.assertEquals(
            this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new HexPosn(0, 1))),
            this.mockModel), List.of(new HexPosn(0, 1)));
  }

  @Test
  public void testAvoidEdgesStrategyFindsNoMovesWhenAllMovesAreEdgeAdjacent() {
    this.validPosn.clear();
    this.validPosn.put(new HexPosn(0, -2), 1);
    this.validPosn.put(new HexPosn(2, -2), 1);
    this.validPosn.put(new HexPosn(-2, 2), 1);

    Assert.assertEquals(this.avoidEdgesStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), new ArrayList<>());
  }

  @Test
  public void testAvoidEdgesStrategyFindsNoMovesWhenAllMovesAreEdgeAdjacentMovesPassedIn() {
    this.validPosn.clear();
    this.validPosn.put(new HexPosn(0, -2), 1);
    this.validPosn.put(new HexPosn(2, -2), 1);
    this.validPosn.put(new HexPosn(-2, 2), 1);

    Assert.assertEquals(
            this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new HexPosn(-2, 0))),
            this.mockModel),
            new ArrayList<>());
  }

  // GoCornerStrategy
  @Test
  public void testGoCornerStrategyCallsCorrectMethods() {
    this.goCornersStrategy.chooseMove(new ArrayList<>(), this.mockModel);

    int n = this.numRings;
    List<HexPosn> corners = new ArrayList<>(List.of(
            new HexPosn(n, 0), new HexPosn(0, n),
            new HexPosn(n, -n), new HexPosn(0, -n),
            new HexPosn(-n, 0), new HexPosn(-n, n)
    ));

    for (HexPosn hp : corners) {
      Assert.assertTrue(this.log.toString().contains("Calling isMoveValid to check if BLACK can "
              + "play on " + hp.toString()));
    }
  }

  @Test
  public void testGoCornerStrategyCorrectlyPicksCorner() {
    this.validPosn.clear();
    this.validPosn.put(new HexPosn(3, 0), 1);
    this.validPosn.put(new HexPosn(2, 1), 1);
    this.validPosn.put(new HexPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), new ArrayList<>(List.of(new HexPosn(3, 0))));
  }

  @Test
  public void testGoCornerStrategyCorrectlySortsResultingList() {
    this.validPosn.clear();
    this.validPosn.put(new HexPosn(3, 0), 1);
    this.validPosn.put(new HexPosn(3, -3), 1);
    this.validPosn.put(new HexPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(), this.mockModel),
            new ArrayList<>(List.of(new HexPosn(3, -3), new HexPosn(3, 0))));
  }

  @Test
  public void testGoCornerStrategyReturnsEmptyWhenNoCornerMoves() {
    this.validPosn.clear();
    this.validPosn.put(new HexPosn(2, 0), 1);
    this.validPosn.put(new HexPosn(-1, -3), 1);
    this.validPosn.put(new HexPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), new ArrayList<>());
  }

  // MinimaxStrategy
  @Test
  public void testMinimaxStrategyReturnsAllPossibleMovesInitiallyInCorrectOrder() {
    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new HexPosn(-1, -1), new HexPosn(1, 1),
                    new HexPosn(1, -2), new HexPosn(2, -1),
                    new HexPosn(-2, 1), new HexPosn(-1, 2))));
  }

  @Test
  public void testMinimaxStrategyReturnsAllPossibleMovesAfterOneMoveInCorrectOrder() {
    this.fullModel.playMove(PieceColor.BLACK, new HexPosn(1, -2));

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new HexPosn(2, -3), new HexPosn(2, -1),
                    new HexPosn(-2, 1), new HexPosn(-1, 2))));
  }

  private Map<Posn, Optional<PieceColor>> createEmptyBoard(int numRings) {
    int start = 0;
    int end = numRings;
    Map<Posn, Optional<PieceColor>> board = new HashMap<>();
    for (int r = -numRings; r <= numRings; r++) {
      for (int q = start; q <= end; q++) {
        HexPosn hp = new HexPosn(q, r);
        board.put(hp, Optional.empty());
      }

      if (start == -numRings) {
        end--;
      } else {
        start--;
      }
    }

    return board;
  }

  @Test
  public void testMinimaxStrategyPicksWinningMoveForBlack() {
    Map<Posn, Optional<PieceColor>> board = this.createEmptyBoard(2);

    board.put(new HexPosn(0, 0), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(-1, 1), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(0, -1), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(-1, 2), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(-2, 2), Optional.of(PieceColor.BLACK));

    this.fullModel = new HexModel(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new HexPosn(1, -1),
                    new HexPosn(0, 1),
                    new HexPosn(-1, 0))));
  }

  @Test
  public void testMinimaxStrategyReturnsEmptyWhenNoMoves() {
    Map<Posn, Optional<PieceColor>> board = this.createEmptyBoard(2);
    board.put(new HexPosn(0, 0), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(0, 1), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(0, 2), Optional.of(PieceColor.WHITE));
    this.fullModel = new HexModel(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>());
  }

  @Test
  public void testMinimaxStrategyReturnsMovesWhenPlayersTurn() {
    Map<Posn, Optional<PieceColor>> board = this.createEmptyBoard(2);
    board.put(new HexPosn(0, 0), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(0, 1), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(0, 2), Optional.of(PieceColor.WHITE));
    this.fullModel = new HexModel(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();
    
    this.fullModel.pass(PieceColor.BLACK);

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(new HexPosn(0, -1))));
  }

  @Test
  public void testMinimaxStrategyChoosesNonLosingMoveForPlayer() {
    Map<Posn, Optional<PieceColor>> board = this.createEmptyBoard(2);
    board.put(new HexPosn(0, 0), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(0, 1), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(0, 2), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(-2, 1), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(-2, 2), Optional.of(PieceColor.BLACK));
    this.fullModel = new HexModel(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(new HexPosn(-2, 0), new HexPosn(0, -1))));
  }

  @Test
  public void testMinimaxStrategyWorksDifferentlyAtDifferentDepth() {
    this.minimaxStrategy = new MinimaxStrategyDepth(new AndStrategy(
            new GoCornerStrategy(),
            new AndStrategy(new AvoidEdgesStrategy(), new CaptureMostStrategy())), 3);
    Map<Posn, Optional<PieceColor>> board = this.createEmptyBoard(2);

    board.put(new HexPosn(0, 0), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(-1, 1), Optional.of(PieceColor.WHITE));
    board.put(new HexPosn(0, -1), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(-1, 2), Optional.of(PieceColor.BLACK));
    board.put(new HexPosn(-2, 2), Optional.of(PieceColor.BLACK));

    this.fullModel = new HexModel(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new HexPosn(1, -1),
                    new HexPosn(-1, 0),
                    new HexPosn(0, 1))));
  }

}
