import org.junit.Test;

import java.awt.Color;
import java.util.ArrayList;

public class ReversiTests {
  @Test
  public void testDeque() {
    System.out.println(new TextualView(new ROModel(new AIPlayer(Color.WHITE),
            new AIPlayer(Color.BLACK), 3)));

  }
}
