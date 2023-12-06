package cs3500.reversi;

import cs3500.reversi.adapter.ViewAdapter;
import cs3500.reversi.controller.Controller;
import cs3500.reversi.model.PieceColor;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.adapter.ModelAdapter;
import cs3500.reversi.view.IView;
import cs3500.reversi.view.View;

public final class ReversiWithProvider {
  public static void main(String[] args) {
    ModelAdapter model = new ModelAdapter(3);

    IView viewBlack = new ViewAdapter(model);
    viewBlack.display(true);
    
    IView viewWhite = new View(model, PieceColor.WHITE);

    Player player1 = new HumanPlayer();
    Player player2 = new HumanPlayer();

    new Controller(model, player1, viewBlack, PieceColor.BLACK);
    new Controller(model, player2, viewWhite, PieceColor.WHITE);

    model.startGame();
  }
}
