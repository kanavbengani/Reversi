package view;

import model.AxialPosn;
import java.util.Optional;
import model.IROModel;
import model.PieceColor;

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
