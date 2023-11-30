package cs3500.reversi.view;

import cs3500.reversi.MockPlayerListener;
import cs3500.reversi.model.AxialPosn;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents a set of JUnit tests that test the package-private functionality of the
 * cs3500.reversi.view.
 */
public class PackagePrivateViewTests {
  private ReversiPanel rp;
  private StringBuilder log;
  private KeyListener keyListener;
  private MouseAdapter mouseAdapter;
  
  @Before
  public void setupTests() {
    this.rp = new ReversiPanel(new Model(5).getReadOnlyModel(), PieceColor.BLACK);
    this.log = new StringBuilder();
    this.rp.addFeaturesListener(new MockPlayerListener(this.log));
    this.keyListener = this.rp.getKeyListener();
    this.mouseAdapter = this.rp.getMouseAdapter();
  }
  
  @Test
  public void testViewKeyboardPassOutputsCorrectlyToPlayerFeatures() {
    this.keyListener.keyPressed(new KeyEvent(this.rp, KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_P, KeyEvent.CHAR_UNDEFINED,
        KeyEvent.KEY_LOCATION_STANDARD)
    );
    
    Assert.assertEquals(this.log.toString(), "Pass was called\n");
  }
  
  @Test
  public void testViewKeyboardEnterOutputsNothingToPlayerFeaturesWithoutHighlightedHex() {
    this.keyListener.keyPressed(new KeyEvent(this.rp, KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED,
        KeyEvent.KEY_LOCATION_STANDARD)
    );
    
    Assert.assertEquals(this.log.toString(), "");
  }
  
  @Test
  public void testViewKeyboardEnterOutputsNothingToPlayerFeatures() {
    // This test serves as a way to test whether the view is computing the correct logical
    // coordinates of the clicked cell. This is because it takes in a mouse clicked coordinate
    // and the log outputs the correct coordinate.
    
    // Highlighting (0, 0)
    this.mouseAdapter.mouseReleased(new MouseEvent(this.rp, MouseEvent.BUTTON1,
        System.currentTimeMillis(), 0, this.rp.getPreferredSize().width / 2,
        this.rp.getPreferredSize().height / 2, 1, false)
    );
    
    // Clicking Enter to play a move on (0, 0)
    this.keyListener.keyPressed(new KeyEvent(this.rp, KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED,
        KeyEvent.KEY_LOCATION_STANDARD)
    );
    
    Assert.assertEquals(this.log.toString(),
        "Move was called with axial position (0, 0).\n");
  }
  
  @Test
  public void testViewKeyboardEnterOutputsNothingToPlayerFeaturesOrigin() {
    // Highlighting (0, 0)
    this.mouseAdapter.mouseReleased(new MouseEvent(this.rp, MouseEvent.BUTTON1,
        System.currentTimeMillis(), 0, 0, 0, 1, false)
    );
    // Clicking Enter to play a move on (0, 0)
    this.keyListener.keyPressed(new KeyEvent(this.rp, KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED,
        KeyEvent.KEY_LOCATION_STANDARD)
    );
    
    Assert.assertEquals(this.log.toString(), "");
  }
  
  @Test
  public void testViewKeyboardEnterOutputsNothingToPlayerFeaturesDifferentCoords() {
    // Highlighting (0, 0)
    this.mouseAdapter.mouseReleased(new MouseEvent(this.rp, MouseEvent.BUTTON1,
        System.currentTimeMillis(), 0, this.rp.getPreferredSize().width / 4,
        this.rp.getPreferredSize().height / 4, 0, false)
    );
    // Clicking Enter to play a move on (0, 0)
    this.keyListener.keyPressed(new KeyEvent(this.rp, KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED,
        KeyEvent.KEY_LOCATION_STANDARD)
    );
    
    Assert.assertEquals(this.log.toString(),
        "Move was called with axial position (-1, -3).\n");
  }
  
  // CartesianPosn
  // Equals
  @Test
  public void testAxialPosnEquals() {
    Assert.assertNotEquals("(0, 0)", new CartesianPosn(0, 0));
    Assert.assertNotEquals(new CartesianPosn(0, 1), new CartesianPosn(1, 0));
    Assert.assertEquals(new CartesianPosn(0, 0), new CartesianPosn(0, 0));
  }
  
  // toString
  @Test
  public void testAxialPosnToString() {
    Assert.assertEquals(new CartesianPosn(0, 0).toString(), "(0.0, 0.0)");
    Assert.assertEquals(new CartesianPosn(0, 1).toString(), "(0.0, 1.0)");
  }
}
