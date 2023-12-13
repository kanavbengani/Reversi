package cs3500.reversi.model.square;

import org.junit.Assert;
import org.junit.Test;

public class PackagePrivateSquareModelTests {
  @Test
  public void testGetDirections() {
    Assert.assertArrayEquals(
        new SquareModel(2).getDirections(),
            SquareDirection.values()
    );
  }
}
