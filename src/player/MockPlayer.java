package player;

import java.awt.Color;
import java.util.Objects;

import model.IROModel;

public class MockPlayer implements Player {
  private final Color color;
  private final String str;

  public MockPlayer(Color color, String str) {
    this.color = color;
    this.str = str;
  }

  @Override
  public void playMove(IROModel model) {
  }

  @Override
  public String toString() {
    return "_";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MockPlayer mockPlayer = (MockPlayer) o;
    return this.color.equals(mockPlayer.color) && this.str.equals(mockPlayer.str);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color, this.str);
  }
}