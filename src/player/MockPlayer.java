package player;

import model.IModel;

/**
 * A mock implementation of the interface for testing and logging purposes.
 * This class allows you to log method calls made to the player for testing and debugging.
 */
public class MockPlayer implements Player {

  private final StringBuilder log;

  /**
   * Constructs a new MockPlayer with the provided StringBuilder for logging.
   *
   * @param log The StringBuilder to which method calls will be logged.
   */
  public MockPlayer(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void playMove(IModel model) {
    this.log.append("playMove called");
  }

  @Override
  public String toString() {
    this.log.append("toString called");
    return "_";
  }
}