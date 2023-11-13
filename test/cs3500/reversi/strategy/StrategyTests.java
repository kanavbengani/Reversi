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
import cs3500.reversi.model.PieceColor;

public class StrategyTests {
  private StringBuilder log;
  private Map<AxialPosn, Integer> posnCaptures = new HashMap<>();
  private int numRings;
  private IModel model;
  private ReversiStrategy captureMostStrategy;
  private ReversiStrategy avoidEdgesStrategy;

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
  }

  // CaptureMostStrategy
  @Test
  public void testCaptureMostStrategyCallsCorrectMethods() {
    this.captureMostStrategy.chooseMove(new ArrayList<>(), model, PieceColor.BLACK);

    for (AxialPosn ap : this.posnCaptures.keySet()) {
      Assert.assertTrue(this.log.toString().contains("getAllCapturedPieces if BLACK plays on " +
              ap.toString()));
    }
  }

  @Test
  public void testCaptureMostStrategyMultipleMovesSameNumCaptures() {
    this.posnCaptures.put(new AxialPosn(2, -1), 5);
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), model,
                    PieceColor.BLACK), List.of(new AxialPosn(1, -2), new AxialPosn(2, -1)));
  }

  @Test
  public void testCaptureMostStrategyMultipleMovesSameNumCapturesSortCorrectly() {
    this.posnCaptures.put(new AxialPosn(2, -1), 5);
    this.posnCaptures.put(new AxialPosn(1, 1), 5);

    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), model,
            PieceColor.BLACK), List.of(new AxialPosn(1, -2), new AxialPosn(2, -1),
            new AxialPosn(1, 1)));
  }

  @Test
  public void testCaptureMostStrategyNoCaptures() {
    this.posnCaptures.clear();
    Assert.assertEquals(this.captureMostStrategy.chooseMove(new ArrayList<>(), this.model,
            PieceColor.BLACK).size(), 0);
  }

  @Test
  public void testCaptureMostStrategyTakesPossibleMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new AxialPosn(2, -1)), this.model,
            PieceColor.BLACK), List.of(new AxialPosn(2, -1)));
  }

  @Test
  public void testCaptureMostStrategyDisjointMovesAndValidMoves() {
    Assert.assertEquals(this.captureMostStrategy.chooseMove(List.of(new AxialPosn(3, -2)),
            this.model, PieceColor.BLACK).size(), 0);
  }

  // AvoidEdgesStrategy
  @Test
  public void testAvoidEdgesStrategyCallsCorrectMethods() {
    this.avoidEdgesStrategy.chooseMove(new ArrayList<>(), this.model, PieceColor.BLACK);

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
            this.model, PieceColor.BLACK), List.of(new AxialPosn(0, 1)));
  }
}
