import java.util.Optional;
import java.util.List;

public class Model extends ROModel implements IModel {
  public Model(Player player1, Player player2, int rings) {
    super(player1, player2, rings);
  }

  @Override
  public void playMove(Player player, Posn p)
          throws IllegalStateException, IllegalArgumentException {
    if (!super.board.containsKey(p)) {
      throw new IllegalArgumentException("The passed-in position is out of bounds.");
    }

    List<Posn> points = super.validateMove(player, p);

    super.board.put(p, Optional.of(player));

    for (Posn posn : points) {
      super.board.put(posn, Optional.of(player));
    }

    this.switchTurn();
  }

  @Override
  public void switchTurn() {
    super.currentPlayer = super.currentPlayer.equals(player1) ? player2 : player1;
  }

  public IROModel getReadOnlyModel() {
    return this;
  }
}
