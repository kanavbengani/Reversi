package cs3500.reversi.model.hex;

import org.junit.Assert;
import org.junit.Test;

public class PackagePrivateHexModelTests {
  @Test
  public void testGetDirections() {
    Assert.assertArrayEquals(
        new HexModel(2).getDirections(),
        HexDirection.values()
    );
  }
}
