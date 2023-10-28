import java.util.Optional;
public class TextualView {
  private final IROModel m;

  TextualView(IROModel m) {
    this.m = m;
  }

  @Override
  public String toString() {
    int rings = this.m.getRings();
    int totalRows = rings * 2 + 1;
    StringBuilder str = new StringBuilder();

    for (int i = 0; i < totalRows; i++) {
      int numSpaces = Math.abs(rings - i);

      for (int j = 0; j < numSpaces; j++) {
        str.append(" ");
      }

      for (int j = 0; j < totalRows; j++) {
        try {
          Optional<Player> cell = this.m.getPlayerAt(new Posn(j, i));
          if (cell.isEmpty()) {
            str.append("_");
          } else {
            Player p = cell.get();
            str.append(p);
          }
          str.append(" ");
        } catch (IllegalArgumentException ignored) {
        }
      }
      str.append("\n");
    }

    return str.toString().stripTrailing();
  }
}
