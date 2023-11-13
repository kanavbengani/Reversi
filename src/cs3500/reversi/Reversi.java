package cs3500.reversi;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

public final class Reversi {
  public static void main(String[] args) {
    IModel model = new Model(5);
//    IView viewBlack = new View(model.getReadOnlyModel(), PieceColor.BLACK);
    IView viewWhite = new View(model.getReadOnlyModel(), PieceColor.WHITE);

    viewWhite.display(true);
//    viewWhite.display(true);
  }
}