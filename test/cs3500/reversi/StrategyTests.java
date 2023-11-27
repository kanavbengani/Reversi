package cs3500.reversi;

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

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;

/**
 * A JUnit4 testing class for testing Reversi strategies.
 */
public class StrategyTests {
  private StringBuilder log;
  private final Map<AxialPosn, Integer> validPosn = new HashMap<>();
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
    this.validPosn.put(new AxialPosn(1, -2), 5);
    this.validPosn.put(new AxialPosn(2, -1), 3);
    this.validPosn.put(new AxialPosn(1, 1), 1);
    this.numRings = 3;

    this.mockModel = new MockModelForStrategy(this.log, this.validPosn, this.numRings);
    this.fullModel = new Model(this.numRings);
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

    for (AxialPosn ap : this.mockModel.getAllPosn()) {
      Assert.assertTrue(this.log.toString().contains("Calling isMoveValid to check if BLACK" +
              " can play on " + ap.toString()));
    }
  }

  @Test
  public void testCaptureMostStrategyGetsCorrectMove() {
    this.validPosn.put(new AxialPosn(2, -1), 5);
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), mockModel).get(0),
            new AxialPosn(1, -2)); // The "best" move according to this strategy [index 0]
  }

  @Test
  public void testCaptureMostStrategyMultipleMovesSameNumCapturesSortCorrectly() {
    this.validPosn.put(new AxialPosn(2, -1), 5);
    this.validPosn.put(new AxialPosn(1, 1), 5);
    // Mock model assumes the given list (validPosn) are the valid moves and returns
    // true accordingly to the strategy.

    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), mockModel),
            List.of(new AxialPosn(1, -2), new AxialPosn(2, -1),
            new AxialPosn(1, 1)));
  }

  @Test
  public void testCaptureMostStrategyNoCaptures() {
    this.validPosn.clear();
    Assert.assertEquals(this.captureMostStrategy.chooseMove(
            new ArrayList<>(), this.mockModel).size(),0);
  }

  @Test
  public void testCaptureMostStrategyTakesPossibleMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new AxialPosn(2, -1)),
            this.mockModel), List.of(new AxialPosn(2, -1)));
  }

  @Test
  public void testCaptureMostStrategyDisjointMovesAndValidMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new AxialPosn(3, -2)),
            this.mockModel).size(), 0);
  }

  // AvoidEdgesStrategy
  @Test
  public void testAvoidEdgesStrategyCallsCorrectMethods() {
    this.avoidEdgesStrategy.chooseMove(new ArrayList<>(), this.mockModel);

    int n = this.numRings;
    List<AxialPosn> edges = new ArrayList<>(List.of(
            new AxialPosn(-1, -n + 1), new AxialPosn(0, -n + 1), new AxialPosn(1, -n),
            new AxialPosn(n - 1, -n), new AxialPosn(n - 1, -n + 1), new AxialPosn(n, -n + 1),
            new AxialPosn(n, -1), new AxialPosn(n - 1, 0), new AxialPosn(n - 1, 1),
            new AxialPosn(1, n - 1), new AxialPosn(0, n - 1), new AxialPosn(-1, n),
            new AxialPosn(-n + 1, n), new AxialPosn(-n + 1, n - 1), new AxialPosn(-n, n - 1),
            new AxialPosn(-n, 1), new AxialPosn(-n + 1, 0), new AxialPosn(-n + 1, -1)
    ));

    for (AxialPosn ap : edges) {
      Assert.assertTrue(this.log.toString().contains("Calling isMoveValid to check if BLACK can "
              + "play on " + ap.toString()));
    }
  }

  @Test
  public void testAvoidEdgesStrategyCorrectlyAvoidsEdgeWithNoOptionLeft() {
    this.validPosn.clear();
    this.validPosn.put(new AxialPosn(0, -2), 1);
    this.validPosn.put(new AxialPosn(0, 1), 1);

    Assert.assertEquals(this.avoidEdgesStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), List.of(new AxialPosn(0, 1)));
  }

  @Test
  public void testAvoidEdgesStrategyCorrectlyAvoidsEdgeWithOptionsProvided() {
    this.validPosn.clear();
    this.validPosn.put(new AxialPosn(0, -2), 1);
    this.validPosn.put(new AxialPosn(0, 1), 1);
    this.validPosn.put(new AxialPosn(-1, 2), 1);

    Assert.assertEquals(
            this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new AxialPosn(0, 1))),
            this.mockModel), List.of(new AxialPosn(0, 1)));
  }

  @Test
  public void testAvoidEdgesStrategyFindsNoMovesWhenAllMovesAreEdgeAdjacent() {
    this.validPosn.clear();
    this.validPosn.put(new AxialPosn(0, -2), 1);
    this.validPosn.put(new AxialPosn(2, -2), 1);
    this.validPosn.put(new AxialPosn(-2, 2), 1);

    Assert.assertEquals(this.avoidEdgesStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), new ArrayList<>());
  }

  @Test
  public void testAvoidEdgesStrategyFindsNoMovesWhenAllMovesAreEdgeAdjacentMovesPassedIn() {
    this.validPosn.clear();
    this.validPosn.put(new AxialPosn(0, -2), 1);
    this.validPosn.put(new AxialPosn(2, -2), 1);
    this.validPosn.put(new AxialPosn(-2, 2), 1);

    Assert.assertEquals(
            this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new AxialPosn(-2, 0))),
            this.mockModel),
            new ArrayList<>());
  }

  // GoCornerStrategy
  @Test
  public void testGoCornerStrategyCallsCorrectMethods() {
    this.goCornersStrategy.chooseMove(new ArrayList<>(), this.mockModel);

    int n = this.numRings;
    List<AxialPosn> corners = new ArrayList<>(List.of(
            new AxialPosn(n, 0), new AxialPosn(0, n),
            new AxialPosn(n, -n), new AxialPosn(0, -n),
            new AxialPosn(-n, 0), new AxialPosn(-n, n)
    ));

    for (AxialPosn ap : corners) {
      Assert.assertTrue(this.log.toString().contains("Calling isMoveValid to check if BLACK can "
              + "play on " + ap.toString()));
    }
  }

  @Test
  public void testGoCornerStrategyCorrectlyPicksCorner() {
    this.validPosn.clear();
    this.validPosn.put(new AxialPosn(3, 0), 1);
    this.validPosn.put(new AxialPosn(2, 1), 1);
    this.validPosn.put(new AxialPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), new ArrayList<>(List.of(new AxialPosn(3, 0))));
  }

  @Test
  public void testGoCornerStrategyCorrectlySortsResultingList() {
    this.validPosn.clear();
    this.validPosn.put(new AxialPosn(3, 0), 1);
    this.validPosn.put(new AxialPosn(3, -3), 1);
    this.validPosn.put(new AxialPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(), this.mockModel),
            new ArrayList<>(List.of(new AxialPosn(3, -3), new AxialPosn(3, 0))));
  }

  @Test
  public void testGoCornerStrategyReturnsEmptyWhenNoCornerMoves() {
    this.validPosn.clear();
    this.validPosn.put(new AxialPosn(2, 0), 1);
    this.validPosn.put(new AxialPosn(-1, -3), 1);
    this.validPosn.put(new AxialPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(),
            this.mockModel), new ArrayList<>());
  }

  // MinimaxStrategy
  @Test
  public void testMinimaxStrategyReturnsAllPossibleMovesInitiallyInCorrectOrder() {
    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new AxialPosn(-1, -1), new AxialPosn(1, 1),
                    new AxialPosn(1, -2), new AxialPosn(2, -1),
                    new AxialPosn(-2, 1), new AxialPosn(-1, 2))));
  }

  @Test
  public void testMinimaxStrategyReturnsAllPossibleMovesAfterOneMoveInCorrectOrder() {
    this.fullModel.playMove(PieceColor.BLACK, new AxialPosn(1, -2));

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new AxialPosn(2, -3), new AxialPosn(2, -1),
                    new AxialPosn(-2, 1), new AxialPosn(-1, 2))));
  }

  private Map<AxialPosn, Optional<PieceColor>> createEmptyBoard(int numRings) {
    int start = 0;
    int end = numRings;
    Map<AxialPosn, Optional<PieceColor>> board = new HashMap<>();
    for (int r = -numRings; r <= numRings; r++) {
      for (int q = start; q <= end; q++) {
        AxialPosn ap = new AxialPosn(q, r);
        board.put(ap, Optional.empty());
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
    Map<AxialPosn, Optional<PieceColor>> board = this.createEmptyBoard(2);

    board.put(new AxialPosn(0, 0), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(-1, 1), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(0, -1), Optional.of(PieceColor.BLACK));
    board.put(new AxialPosn(-1, 2), Optional.of(PieceColor.BLACK));
    board.put(new AxialPosn(-2, 2), Optional.of(PieceColor.BLACK));

    this.fullModel = new Model(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new AxialPosn(1, -1),
                    new AxialPosn(0, 1),
                    new AxialPosn(-1, 0))));
  }

  @Test
  public void testMinimaxStrategyReturnsEmptyWhenNoMoves() {
    Map<AxialPosn, Optional<PieceColor>> board = this.createEmptyBoard(2);
    board.put(new AxialPosn(0, 0), Optional.of(PieceColor.BLACK));
    board.put(new AxialPosn(0, 1), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(0, 2), Optional.of(PieceColor.WHITE));
    this.fullModel = new Model(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>());
  }

  @Test
  public void testMinimaxStrategyReturnsMovesWhenPlayersTurn() {
    Map<AxialPosn, Optional<PieceColor>> board = this.createEmptyBoard(2);
    board.put(new AxialPosn(0, 0), Optional.of(PieceColor.BLACK));
    board.put(new AxialPosn(0, 1), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(0, 2), Optional.of(PieceColor.WHITE));
    this.fullModel = new Model(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();
    
    this.fullModel.pass(PieceColor.BLACK);

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(new AxialPosn(0, -1))));
  }

  @Test
  public void testMinimaxStrategyChoosesNonLosingMoveForPlayer() {
    Map<AxialPosn, Optional<PieceColor>> board = this.createEmptyBoard(2);
    board.put(new AxialPosn(0, 0), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(0, 1), Optional.of(PieceColor.BLACK));
    board.put(new AxialPosn(0, 2), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(-2, 1), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(-2, 2), Optional.of(PieceColor.BLACK));
    this.fullModel = new Model(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(new AxialPosn(-2, 0), new AxialPosn(0, -1))));
  }

  @Test
  public void testMinimaxStrategyWorksDifferentlyAtDifferentDepths() {
    this.minimaxStrategy = new MinimaxStrategyDepth(new AndStrategy(
            new GoCornerStrategy(),
            new AndStrategy(new AvoidEdgesStrategy(), new CaptureMostStrategy())), 3);
    Map<AxialPosn, Optional<PieceColor>> board = this.createEmptyBoard(2);

    board.put(new AxialPosn(0, 0), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(-1, 1), Optional.of(PieceColor.WHITE));
    board.put(new AxialPosn(0, -1), Optional.of(PieceColor.BLACK));
    board.put(new AxialPosn(-1, 2), Optional.of(PieceColor.BLACK));
    board.put(new AxialPosn(-2, 2), Optional.of(PieceColor.BLACK));

    this.fullModel = new Model(board);
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.addListener(new MockModelListener(new StringBuilder()));
    this.fullModel.startGame();

    Assert.assertEquals(this.minimaxStrategy.chooseMove(new ArrayList<>(), this.fullModel),
            new ArrayList<>(List.of(
                    new AxialPosn(1, -1),
                    new AxialPosn(-1, 0),
                    new AxialPosn(0, 1))));
  }

}
