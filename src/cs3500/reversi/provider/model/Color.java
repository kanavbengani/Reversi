package cs3500.reversi.provider.model;

/**
 * Representation of a player in Reversi, can either be of type Black or White. This enum can be
 * extended further on to encompass more player types, and number of players if needed.
 */
public enum Color {
  BLACK, WHITE;

  @Override
  public String toString() {
    if (this.equals(BLACK)) {
      return "X";
    } else {
      return "O";
    }
  }
}