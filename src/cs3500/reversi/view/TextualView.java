package cs3500.reversi.view;

import cs3500.reversi.model.AxialPosn;
import java.util.Optional;
import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

/**
 * The TextualView class provides a textual representation of a read-only cs3500.reversi.model.
 */
public class TextualView {
  private final IROModel model;

  /**
   * Constructs a TextualView with a given read-only cs3500.reversi.model.
   *
   * @param model The read-only cs3500.reversi.model to be displayed.
   */
  public TextualView(IROModel model) {
    this.model = model;
  }

  /**
   * Generates a textual representation of the read-only cs3500.reversi.model.
   *
   * @return A string representing the read-only cs3500.reversi.model.
   */
  @Override
  public String toString() {
    int rings = this.model.getNumRings();
    int totalRows = rings * 2 + 1;
    StringBuilder str = new StringBuilder();

    for (int r = -rings; r <= rings; r++) {
      int numSpaces = Math.abs(r);

      str.append(" ".repeat(Math.max(0, numSpaces)));

      for (int q = -rings; q <= rings; q++) {
        try {
          Optional<PieceColor> cell = this.model.getPieceAt(new AxialPosn(q, r));
          if (cell.isEmpty()) {
            str.append("_");
          } else {
            PieceColor p = cell.get();
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
