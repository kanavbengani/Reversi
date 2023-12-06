import cs3500.reversi.adapter.ModelAdapter;
import cs3500.reversi.provider.model.ReadonlyReversiModel;
import org.junit.Test;

import java.util.Arrays;

public class NeedToDeleteTests {
  @Test
  public void testAdapter() {
    ReadonlyReversiModel model = new ModelAdapter(2);
    System.out.println(Arrays.deepToString(model.copyBoard()));
  }
}
