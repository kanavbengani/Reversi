package model;

import player.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The `ROModel` class represents a read-only game model.
 */
public class ROModel implements IROModel {
  protected final Player player1;
  protected final Player player2;
  protected Player currentPlayer;
  protected final Map<Posn, Optional<Player>> board;
  private final int rings;

  /**
   * Constructs a read-only model with the given players and number of rings.
   *
   * @param player1 The first player.
   * @param player2 The second player.
   * @param rings   The number of rings on the game board.
   * @throws IllegalArgumentException if the number of rings is less than 2 or if any player is null.
   */
  public ROModel(Player player1, Player player2, int rings) {
    if (rings < 2) {
      throw new IllegalArgumentException("Number of rings must be at least 2.");
    }

    if (player1 == null || player2 == null) {
      throw new IllegalArgumentException("Passed-in players cannot be null.");
    }

    this.player1 = player1;
    this.player2 = player2;
    this.currentPlayer = player1;
    this.rings = rings;
    this.board = new LinkedHashMap<>();

    initializeBoard(rings);
  }

  private void initializeBoard(int rings) {
    int start = rings;
    int end = 2 * rings;

    for (int i = 0; i <= rings * 2; i++) {
      for (int j = start; j <= end; j++) {
        Posn posn = new Posn(j, i);
        this.board.put(posn, Optional.empty());
      }

      if (start == 0) {
        end--;
      } else {
        start--;
      }
    }

    this.initializePlayers();
  }

  private void initializePlayers() {
    Posn center = new Posn(this.rings, this.rings);

    for (int i = 0; i < Posn.OFFSETS.size(); i++) {
      this.board.put(center.add(Posn.OFFSETS.get(i)), Optional.of(i % 2 == 0 ? player1 : player2));
    }
  }

  @Override
  public boolean isGameOver() {
    for (Posn point : this.board.keySet()) {
      if (isMoveValid(player1, point) || isMoveValid(player2, point)) {
        return false;
      }
    }
    return true;
  }

  // Checking if the given move is valid with the given player
  private boolean isMoveValid(Player player, Posn point) {
    try {
      return !validateMove(player, point).isEmpty();
    } catch (IllegalStateException | IllegalArgumentException ignored) {
      return false;
    }
  }

  @Override
  public Optional<Player> getWinner() throws IllegalStateException {
    if (!isGameOver()) {
      throw new IllegalStateException("The game is not over.");
    }

    long player1Count = countPlayerChips(player1);
    long player2Count = countPlayerChips(player2);

    if (player1Count > player2Count) {
      return Optional.of(player1);
    } else if (player2Count > player1Count) {
      return Optional.of(player2);
    }

    return Optional.empty();
  }

  // counts the number of chips for the given player on the board.
  private long countPlayerChips(Player player) {
    return this.board.values().stream()
            .filter(optional -> optional.equals(Optional.of(player)))
            .count();
  }

  @Override
  public Optional<Player> getPlayerAt(Posn p) throws IllegalArgumentException {
    if (!this.board.containsKey(p)) {
      throw new IllegalArgumentException("Invalid coordinate passed in");
    }

    return this.board.get(p);
  }

  // Validates if the given hexagonal position can place this player assuming the given hexagonal
  // position is in-bounds.
  protected List<Posn> validateMove(Player player, Posn hp)
          throws IllegalStateException, IllegalArgumentException {
    if (this.board.get(hp).isPresent()) {
      throw new IllegalStateException("Chip cannot be placed in an already occupied cell.");
    }

    if (!player.equals(this.currentPlayer)) {
      throw new IllegalArgumentException("Not the player's turn.");
    }

    List<Posn> finalPoints = new ArrayList<>();

    for (Posn offset : Posn.OFFSETS) {
      Posn tempHp = hp.add(offset);
      int counter = 0;
      List<Posn> tempPoints = new ArrayList<>();
      while (this.board.getOrDefault(tempHp, Optional.empty()).isPresent()) {
        tempPoints.add(tempHp);
        if (this.board.get(tempHp).isEmpty()) {
          break;
        }

        Player p = this.board.get(tempHp).get();

        if (p.equals(player)) {
          if (counter == 0) {
            tempHp = tempHp.add(offset);
            counter += 1;
            continue;
          }
          finalPoints.addAll(tempPoints);
          break;
        }

        tempHp = tempHp.add(offset);
        counter += 1;
      }
    }

    if (finalPoints.isEmpty()) {
      throw new IllegalStateException("Move is not valid.");
    }

    return finalPoints;
  }

  @Override
  public Player getTurn() {
    return this.currentPlayer;
  }

  @Override
  public int getRings() {
    return this.rings;
  }
}
