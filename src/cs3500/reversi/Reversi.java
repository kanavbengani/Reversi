package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.IModel;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.strategy.AndStrategy;
import cs3500.reversi.strategy.AvoidEdgesStrategy;
import cs3500.reversi.strategy.CaptureMostStrategy;
import cs3500.reversi.strategy.GoCornerStrategy;
import cs3500.reversi.strategy.MinimaxStrategy;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

public final class Reversi {
  public static void main(String[] args) {
    IModel model = new Model(5);

    IView viewBlack = new View(model.getReadOnlyModel(), PieceColor.BLACK);
    IView viewWhite = new View(model.getReadOnlyModel(), PieceColor.WHITE);

    viewBlack.display(true);
    viewWhite.display(true);

//      IModel fullModel = new Model(6);
//
//      IView view = new View(fullModel.getReadOnlyModel(), PieceColor.BLACK);
//      view.display(true);
//
//      while (!fullModel.isGameOver()) {
//        List<AxialPosn> moves;
//        moves = new MinimaxStrategy().chooseMove(new ArrayList<>(), fullModel);
//
//        PieceColor turn = fullModel.getTurn();
//        if (!moves.isEmpty()) {
//          fullModel.playMove(turn, moves.get(0));
//        } else {
//          fullModel.pass(turn);
//        }
//        view.refresh();
//      }
//
//      Optional<PieceColor> winner = fullModel.getWinner();
//
//      if (winner.isPresent()) {
//        System.out.println("Game is over! " + winner.get() + " won the game!");
//      } else {
//        System.out.println("That was a TIE!");
//      }
    }
}