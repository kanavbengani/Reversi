import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ROModel implements IROModel {

  protected final Player player1;
  protected final Player player2;
  protected Player currentPlayer;
  protected List<List<Optional<Player>>> board;
  private final int rings;

  public ROModel(Player player1, Player player2, int rings) {
    if (rings < 2) {
      throw new IllegalArgumentException("Number of rings must be at least 2.");
    }

    this.player1 = player1;
    this.player2 = player2;
    this.currentPlayer = player1;
    this.rings = rings;

    this.initializeBoard(rings);
  }

  private void initializeBoard(int rings) {
    this.board = new ArrayList<>();
    for (int i = 0; i < this.rings * 2 + 1; i++) {
      List<Optional<Player>> row = new ArrayList<>();
      // Adds correct number of empty Optionals
      for (int j = 0; j < this.rings * ; j++) {
        row.add(Optional.empty());
      }
      this.board.add(row);
    }

    this.initializePlayers();
  }

  private void initializePlayers() {
    // TODO
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Optional<Player> getWinner() {
    return Optional.empty();
  }

  @Override
  public Optional<Player> getCell(HexPosn hp) throws IllegalArgumentException {
    if (!this.board.containsKey(hp)) {
      throw new IllegalArgumentException("Invalid coordinate passed in");
    }

    return this.board.get(hp);
  }

  @Override
  public Player getTurn() {
    return this.currentPlayer;
  }

  @Override
  public int getRings() {
    return this.rings;
  }
}
