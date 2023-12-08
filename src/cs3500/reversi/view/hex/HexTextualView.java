package cs3500.reversi.view.hex;

import cs3500.reversi.model.hex.HexPosn;
import java.util.Optional;

import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PieceColor;

/**
 * The HexTextualView class provides a textual representation of a read-only Reversi model.
 */
public class HexTextualView {
  private final IROModel model;

  /**
   * Constructs a HexTextualView with a given read-only cs3500.reversi.model.
   *
   * @param model The read-only cs3500.reversi.model to be displayed.
   */
  public HexTextualView(IROModel model) {
    this.model = model;
  }

  @Override
  public String toString() {
    int rings = this.model.getNumRings();
    StringBuilder str = new StringBuilder();

    for (int r = -rings; r <= rings; r++) {
      int numSpaces = Math.abs(r);

      str.append(" ".repeat(Math.max(0, numSpaces)));

      for (int q = -rings; q <= rings; q++) {
        try {
          Optional<PieceColor> cell = this.model.getPieceAt(new HexPosn(q, r));
          if (cell.isEmpty()) {
            str.append("_");
          } else {
            PieceColor p = cell.get();
            str.append(p.str);
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
