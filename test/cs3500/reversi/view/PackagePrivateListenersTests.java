package cs3500.reversi.view;

import cs3500.reversi.MockPlayerListener;
import cs3500.reversi.model.Model;
import cs3500.reversi.model.PieceColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PackagePrivateListenersTests {
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
}
