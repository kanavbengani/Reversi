package model;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import player.PlayerListener;

/**
 * The `Model` class represents the mutable game model, allowing players to make moves and switch
 * turns. This class extends the `ROModel` and adds mutable game state features to allow players
 * to make moves and control the flow of the game.
 * The `Model` class maintains the game's current state, including the game board, the current
 * player's turn, and the list of player listeners. Players can make moves and switch turns, and
 * the class provides methods to notify listeners when it's a player's turn.
 */
public class Model extends ROModel implements IModel {
  private final ArrayList<PlayerListener> listeners;

  /**
   * Constructs a game model with the given players and number of rings.
   *
   * @param numRings The number of rings on the game board.
   * @throws IllegalArgumentException if the number of rings is less than 2 or if any player is
   *         null.
   */
  public Model(int numRings) {
    super(numRings);
    this.listeners = new ArrayList<>();
  }

  @Override
  public void playMove(PieceColor pc, AxialPosn ap)
          throws IllegalStateException, IllegalArgumentException {
    if (super.isGameOver()) {
      throw new IllegalStateException("Cannot be played when game is over.");
    }
    if (!super.board.containsKey(ap)) {
      throw new IllegalArgumentException("The passed-in position is out of bounds.");
    }

    List<AxialPosn> points = super.getAllCapturedPieces(pc, ap);

    super.board.put(ap, Optional.of(pc));

    for (AxialPosn tempAp : points) {
      super.board.put(tempAp, Optional.of(pc));
    }

    this.switchTurn();
  }

  @Override
  public void pass(PieceColor pc)
          throws IllegalStateException {
    if (super.isGameOver()) {
      throw new IllegalStateException("Cannot be played when game is over.");
    }

    if (!super.currentPieceColor.equals(pc)) {
      throw new IllegalStateException("Cannot pass when its not your turn.");
    }

    this.switchTurn();
  }

  @Override
  public IROModel getReadOnlyModel() {
    return this;
  }

  @Override
  public void addListener(PlayerListener playerListener) {
    this.listeners.add(playerListener);
  }

  // switches turn to a different color and triggers event saying it is the current piece's move.
  private void switchTurn() {
    // Switching current piece color.
    super.currentPieceColor =
            super.currentPieceColor.equals(pieceColor1)
                    ? pieceColor2
                    : pieceColor1;

    // Triggering event broadcasting its the given color's move!
    for (PlayerListener f : this.listeners) {
      f.itsTheMoveOf(super.currentPieceColor);
    }
  }
}
