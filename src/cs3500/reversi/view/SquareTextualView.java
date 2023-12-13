package cs3500.reversi.view;

import cs3500.reversi.model.*;
import cs3500.reversi.model.square.SquarePosn;
import cs3500.reversi.view.ITextualView;

import java.util.Optional;

/**
 * The HexTextualView class provides a textual representation of a read-only Reversi model.
 */
public class SquareTextualView implements ITextualView {
  
  /**
   * Constructs a SquareTextualView with a given read-only cs3500.reversi.model.
   *
   * @param model The read-only cs3500.reversi.model to be displayed.
   */
  private final IROModel model;
  
  public SquareTextualView(IROModel model) {
    this.model = model;
  }
  
  @Override
  public String toString() {
    int rings = this.model.getNumRings();
    StringBuilder str = new StringBuilder();
    
    for (int y = -rings + 1; y <= rings; y++) {
      for (int x = -rings + 1; x <= rings; x++) {
        Optional<PieceColor> cell = this.model.getPieceAt(new SquarePosn(x, y));
        if (cell.isEmpty()) {
          str.append("_");
        } else {
          PieceColor p = cell.get();
          str.append(p.str);
        }
        str.append(" ");
      }
      str.append("\n");
    }
    
    return str.toString();
  }
}
