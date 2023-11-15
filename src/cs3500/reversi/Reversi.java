package cs3500.reversi;

import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

/**
 * A main runner for a program of Reversi.
 */
public final class Reversi {
  /**
   * Runs the main program using the Model and the View.
   * @param args String command line arguments
   */
  public static void main(String[] args) {
    IModel model = new Model(5);

    IView viewBlack = new View(model.getReadOnlyModel(), PieceColor.BLACK);
    IView viewWhite = new View(model.getReadOnlyModel(), PieceColor.WHITE);

    viewBlack.display(true);
    viewWhite.display(true);
  }
}