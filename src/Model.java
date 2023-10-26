import java.util.Optional;

public class Model extends ROModel implements IModel {

  public Model(Player player1, Player player2, int rings) {
    super(player1, player2, rings);
  }

  @Override
  public void playMove(Player player, HexPosn hp) throws IllegalStateException,
          IllegalArgumentException {
    if (!super.board.containsKey(hp)) {
      throw new IllegalArgumentException("The passed-in hexagonal position is out of bounds.");
    }

    this.validateMove(player, hp);

    super.board.put(hp, Optional.of(player));
  }

  // Validates if the given hexagonal position can place this player assuming given hexagonal
  // position is in-bounds.
  private void validateMove(Player player, HexPosn hp) throws IllegalStateException {
    if (super.board.get(hp).isPresent()) {
      throw new IllegalStateException("Chip cannot be placed in an already occupied cell.");
    }

    for (HexPosn offset : HexPosn.OFFSETS) {
      HexPosn tempHp = hp;
      tempHp.add(offset);
      while (super.board.getOrDefault(tempHp, Optional.empty()).isPresent()) {
        Player p = super.board.get(tempHp).get();

        if (p.equals(player)) {
          return;
        }

        tempHp = tempHp.add(offset);
      }
    }

    throw new IllegalStateException("Move is not valid.");
  }

  @Override
  public void switchTurn() {
    super.currentPlayer = super.currentPlayer.equals(player1) ? player2 : player1;
  }
}
