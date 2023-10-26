public class TextualView {
  private final ROModel m;

  TextualView(ROModel m) {
    this.m = m;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();

    for (int i = 0; i < this.m.getRings() * 2 + 1; i++) {
      int num = Math.abs(this.m.getRings() - i);
      // adds correct number of spaces before each line
      for (int j = 0; j < num; j++) {
        str.append(" ");
      }
      str.append("_");
      for (int k = 0; k < (this.m.getRings() * 2 + 1) - num - 1; k++) {
        str.append(" _");
      }
      str.append("\n");
    }

    return str.toString();
  }
}
