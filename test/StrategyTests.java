import org.junit.Test;

import java.util.ArrayList;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.strategy.AndStrategy;
import cs3500.reversi.strategy.AvoidEdgesStrategy;
import cs3500.reversi.strategy.CaptureMostStrategy;
import cs3500.reversi.strategy.GoCornerStrategy;
import cs3500.reversi.strategy.ReversiStrategy;

public class StrategyTests {
  @Test
  public void testStrategy() {
    IModel model = new Model(3);
    ReversiStrategy s = new AndStrategy(new GoCornerStrategy(),
            new AndStrategy(new AvoidEdgesStrategy(), new CaptureMostStrategy()));

    System.out.println(s.chooseMove(new ArrayList<>(), model, PieceColor.BLACK));
    }
}
