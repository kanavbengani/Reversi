package cs3500.reversi.strategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.TextualView;
import cs3500.reversi.view.View;

public class StrategyTests {
  private StringBuilder log;
  private final Map<AxialPosn, Integer> posnCaptures = new HashMap<>();
  private int numRings;
  private IModel model;
  private ReversiStrategy captureMostStrategy;
  private ReversiStrategy avoidEdgesStrategy;
  private ReversiStrategy goCornersStrategy;
  private ReversiStrategy minimaxStrategy;

  @Before
  public void initTest() {
    this.log = new StringBuilder();
    this.posnCaptures.put(new AxialPosn(1, -2), 5);
    this.posnCaptures.put(new AxialPosn(2, -1), 3);
    this.posnCaptures.put(new AxialPosn(1, 1), 1);
    this.numRings = 3;

    this.model = new MockModelForStrategy(this.log, this.posnCaptures, this.numRings);
    this.captureMostStrategy = new CaptureMostStrategy();
    this.avoidEdgesStrategy = new AvoidEdgesStrategy();
    this.goCornersStrategy = new GoCornerStrategy();
    this.minimaxStrategy = new MinimaxStrategy();
  }

  // CaptureMostStrategy
  @Test
  public void testCaptureMostStrategyCallsCorrectMethods() {
    this.captureMostStrategy.chooseMove(new ArrayList<>(), model);

    for (AxialPosn ap : this.posnCaptures.keySet()) {
      Assert.assertTrue(this.log.toString().contains("getAllCapturedPieces if BLACK plays on " +
              ap.toString()));
    }
  }

  @Test
  public void testCaptureMostStrategyMultipleMovesSameNumCaptures() {
    this.posnCaptures.put(new AxialPosn(2, -1), 5);
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), model),
            List.of(new AxialPosn(1, -2), new AxialPosn(2, -1)));
  }

  @Test
  public void testCaptureMostStrategyMultipleMovesSameNumCapturesSortCorrectly() {
    this.posnCaptures.put(new AxialPosn(2, -1), 5);
    this.posnCaptures.put(new AxialPosn(1, 1), 5);

    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), model),
            List.of(new AxialPosn(1, -2), new AxialPosn(2, -1),
            new AxialPosn(1, 1)));
  }

  @Test
  public void testCaptureMostStrategyNoCaptures() {
    this.posnCaptures.clear();
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), this.model).size(),
            0);
  }

  @Test
  public void testCaptureMostStrategyTakesPossibleMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new AxialPosn(2, -1)),
            this.model), List.of(new AxialPosn(2, -1)));
  }

  @Test
  public void testCaptureMostStrategyDisjointMovesAndValidMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new AxialPosn(3, -2)),
            this.model).size(), 0);
  }

  // AvoidEdgesStrategy
  @Test
  public void testAvoidEdgesStrategyCallsCorrectMethods() {
    this.avoidEdgesStrategy.chooseMove(new ArrayList<>(), this.model);

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
    this.posnCaptures.clear();
    this.posnCaptures.put(new AxialPosn(0, -2), 1);
    this.posnCaptures.put(new AxialPosn(0, 1), 1);

    Assert.assertEquals(this.avoidEdgesStrategy.chooseMove(new ArrayList<>(),
            this.model), List.of(new AxialPosn(0, 1)));
  }

  @Test
  public void testAvoidEdgesStrategyCorrectlyAvoidsEdgeWithOptionsProvided() {
    this.posnCaptures.clear();
    this.posnCaptures.put(new AxialPosn(0, -2), 1);
    this.posnCaptures.put(new AxialPosn(0, 1), 1);
    this.posnCaptures.put(new AxialPosn(-1, 2), 1);

    Assert.assertEquals(
            this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new AxialPosn(0, 1))),
            this.model), List.of(new AxialPosn(0, 1)));
  }

  @Test
  public void testAvoidEdgesStrategyFindsNoMovesWhenAllMovesAreEdgeAdjacent() {
    this.posnCaptures.clear();
    this.posnCaptures.put(new AxialPosn(0, -2), 1);
    this.posnCaptures.put(new AxialPosn(2, -2), 1);
    this.posnCaptures.put(new AxialPosn(-2, 2), 1);

    Assert.assertEquals(this.avoidEdgesStrategy.chooseMove(new ArrayList<>(),
            this.model), new ArrayList<>());
  }

  @Test
  public void testAvoidEdgesStrategyFindsNoMovesWhenAllMovesAreEdgeAdjacentMovesPassedIn() {
    this.posnCaptures.clear();
    this.posnCaptures.put(new AxialPosn(0, -2), 1);
    this.posnCaptures.put(new AxialPosn(2, -2), 1);
    this.posnCaptures.put(new AxialPosn(-2, 2), 1);

    Assert.assertEquals(
            this.avoidEdgesStrategy.chooseMove(new ArrayList<>(List.of(new AxialPosn(-2, 0))),
            this.model),
            new ArrayList<>());
  }

  // GoCornerStrategy
  @Test
  public void testGoCornerStrategyCallsCorrectMethods() {
    this.goCornersStrategy.chooseMove(new ArrayList<>(), this.model);

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
    this.posnCaptures.clear();
    this.posnCaptures.put(new AxialPosn(3, 0), 1);
    this.posnCaptures.put(new AxialPosn(2, 1), 1);
    this.posnCaptures.put(new AxialPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(),
            this.model), new ArrayList<>(List.of(new AxialPosn(3, 0))));
  }

  @Test
  public void testGoCornerStrategyCorrectlySortsResultingList() {
    this.posnCaptures.clear();
    this.posnCaptures.put(new AxialPosn(3, 0), 1);
    this.posnCaptures.put(new AxialPosn(3, -3), 1);
    this.posnCaptures.put(new AxialPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(), this.model),
            new ArrayList<>(List.of(new AxialPosn(3, -3), new AxialPosn(3, 0))));
  }

  @Test
  public void testGoCornerStrategyReturnsEmptyWhenNoCornerMoves() {
    this.posnCaptures.clear();
    this.posnCaptures.put(new AxialPosn(2, 0), 1);
    this.posnCaptures.put(new AxialPosn(-1, -3), 1);
    this.posnCaptures.put(new AxialPosn(-2, -1), 2);

    Assert.assertEquals(this.goCornersStrategy.chooseMove(new ArrayList<>(),
            this.model), new ArrayList<>());
  }

  @Test
  public void testMinimaxReturnsBestOutcome() {
    IModel prod = new Model(10);

    View view = new View(prod.getReadOnlyModel(), PieceColor.BLACK);
    view.setVisible(true);

    while (!prod.isGameOver()) {
      List<AxialPosn> moves = this.minimaxStrategy.chooseMove(new ArrayList<>(), prod);

      PieceColor turn = prod.getTurn();
      if (!moves.isEmpty()) {
        prod.playMove(turn, moves.get(0));
      } else {
        prod.pass(turn);
      }
      view.repaint();
    }
  }
}
