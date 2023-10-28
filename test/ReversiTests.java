import org.junit.Test;

import java.awt.Color;
import java.util.ArrayList;

public class ReversiTests {
  @Test
  public void testBoard() {
    Player player1 = new AIPlayer(Color.WHITE);
    Player player2 = new AIPlayer(Color.BLACK);

    IModel model = new Model(player1, player2, 3);
    System.out.println(new TextualView(model.getReadOnlyModel()));

    model.playMove(player1, new Posn(2, 2));
    System.out.println(new TextualView(model.getReadOnlyModel()));

    model.playMove(player2, new Posn(2, 1));
    System.out.println(new TextualView(model.getReadOnlyModel()));
  }
}
