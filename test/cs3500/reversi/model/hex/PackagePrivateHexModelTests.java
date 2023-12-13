package cs3500.reversi.model.hex;

import org.junit.Assert;
import org.junit.Test;

/**
 * Represents a set of JUnit tests that tests the package-private functionality of the
 * cs3500.reversi.model.hex.
 */
public class PackagePrivateHexModelTests {
  @Test
  public void testGetDirections() {
    Assert.assertArrayEquals(
        new HexModel(2).getDirections(),
        HexDirection.values()
    );
  }
}
