import java.awt.Color;
import java.util.Objects;

public class AIPlayer implements Player {
  private final Color color;

  AIPlayer(Color color) {
    this.color = color;
  }

  @Override
  public Posn getNextMove(IROModel model) {
    for (Posn p : model.getAllPosn()) {

    }
    return new Posn(0, 0);
  }

  @Override
  public String toString() {
    if (color.equals(Color.BLACK)) {
      return "X";
    }
    return "O";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AIPlayer aiPlayer = (AIPlayer) o;
    return Objects.equals(color, aiPlayer.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }
}