package model;

import player.Player;

import java.util.Optional;
import java.util.List;

/**
 * The `Model` class represents the game model, allowing players to make moves and switch turns.
 */
public class Model extends ROModel implements IModel {
  /**
   * Constructs a game model with the given players and number of rings.
   *
   * @param player1 The first player.
   * @param player2 The second player.
   * @param numRings The number of rings on the game board.
   * @throws IllegalArgumentException if the number of rings is less than 2 or if any player is
   *         null.
   */
  public Model(Player player1, Player player2, int numRings) {
    super(player1, player2, numRings);
  }

  @Override
  public void playMove(Player player, Posn p)
          throws IllegalStateException, IllegalArgumentException {
    if (super.isGameOver()) {
      throw new IllegalStateException("Cannot be played when game is over.");
    }
    if (!super.board.containsKey(p)) {
      throw new IllegalArgumentException("The passed-in position is out of bounds.");
    }

    List<Posn> points = super.validateMove(player, p);

    this.switchTurn();
    super.board.put(p, Optional.of(player));

    for (Posn posn : points) {
      super.board.put(posn, Optional.of(player));
    }

    this.currentPlayer.playMove(this);
  }

  @Override
  public void switchTurn()
          throws IllegalStateException {
    if (super.isGameOver()) {
      throw new IllegalStateException("Cannot be played when game is over.");
    }
    super.currentPlayer = super.currentPlayer.equals(player1) ? player2 : player1;
  }

  @Override
  public IROModel getReadOnlyModel() {
    return this;
  }
}
