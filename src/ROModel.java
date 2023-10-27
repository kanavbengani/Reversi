import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ROModel implements IROModel {

  protected final Player player1;
  protected final Player player2;
  protected Player currentPlayer;
  protected Map<HexPosn, Optional<Player>> board;
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
    this.board = new HashMap<>();

    for (int q = -rings; q <= rings; q++) {
      int r1 = Math.max(-rings, -q - rings);
      int r2 = Math.min(rings, -q + rings);

      for (int r = r1; r <= r2; r++) {
        HexPosn hp = new HexPosn(q, r);
        this.board.put(hp, Optional.empty());
      }
    }

    this.initializePlayers();
  }

  private void initializePlayers() {
    for (int i = 0; i < HexPosn.OFFSETS.size(); i += 1) {
      this.board.put(HexPosn.OFFSETS.get(i), Optional.of(i % 2 == 0 ? player1 : player2));
    }
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
