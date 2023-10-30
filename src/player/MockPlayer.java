package player;

import model.IModel;

public class MockPlayer implements Player {

  private final StringBuilder log;

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