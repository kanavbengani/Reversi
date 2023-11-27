package cs3500.reversi.player;

import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.PlayerFeatures;

public interface Player {
    void playAMove();
    void addListener(PlayerFeatures playerFeatures);
}
