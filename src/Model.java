public interface Model extends ROModel {
  void playMove(Player player, HexPosn hp);

  void switchTurn(Player player);
}
