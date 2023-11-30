package cs3500.reversi;

import cs3500.reversi.model.PieceColor;
import cs3500.reversi.view.IView;
import cs3500.reversi.player.PlayerFeatures;

/**
 * Represents a mock view that implements all the methods of a view.
 * A log is maintained to track which methods of this class were called.
 */
public class MockView implements IView {
  private final StringBuilder log;
  
  /**
   * Constructs a mock view to execute the purpose statement of this class.
   * @param log represents the string builder that will keep track of which methods were called.
   */
  public MockView(StringBuilder log) {
    this.log = log;
  }
  
  @Override
  public void display(boolean b) {
    String result = "display called with " + b + ".\n";
    this.log.append(result);
  }
  
  @Override
  public void disableInput() {
    String result = "disableInput called.\n";
    this.log.append(result);
  }
  
  @Override
  public void refresh() {
    this.log.append("refresh called\n");
  }
  
  @Override
  public void addListener(PlayerFeatures features) {
    this.log.append("addListener called in MockView.\n");
  }
  
  @Override
  public void promptMessage(String message) {
    String result = "promptMessage called with message: " + message + "\n";
    this.log.append(result);
  }
  
  @Override
  public void itsTheTurnOf(PieceColor pieceColor) {
    String result = "itsTheTurnOf " + pieceColor + "\n";
    this.log.append(result);
  }
}
