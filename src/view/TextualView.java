package view;

import player.Player;
import model.Posn;
import java.util.Optional;
import model.IROModel;

/**
 * The TextualView class provides a textual representation of a read-only model.
 */
public class TextualView {
  private final IROModel model;

  /**
   * Constructs a TextualView with a given read-only model.
   *
   * @param model The read-only model to be displayed.
   */
  public TextualView(IROModel model) {
    this.model = model;
  }

  /**
   * Generates a textual representation of the read-only model.
   *
   * @return A string representing the read-only model.
   */
  @Override
  public String toString() {
    int rings = this.model.getNumRings();
    int totalRows = rings * 2 + 1;
    StringBuilder str = new StringBuilder();

    for (int i = 0; i < totalRows; i++) {
      int numSpaces = Math.abs(rings - i);

      str.append(" ".repeat(Math.max(0, numSpaces)));

      for (int j = 0; j < totalRows; j++) {
        try {
          Optional<Player> cell = this.model.getPlayerAt(new Posn(j, i));
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
