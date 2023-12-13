package cs3500.reversi.model.square;

import org.junit.Assert;
import org.junit.Test;

/**
 * Represents a set of JUnit tests that tests the package-private functionality of the
 * cs3500.reversi.model.square.
 */
public class PackagePrivateSquareModelTests {
  @Test
  public void testGetDirections() {
    Assert.assertArrayEquals(
        new SquareModel(2).getDirections(),
            SquareDirection.values()
    );
  }
}
