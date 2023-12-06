//package cs3500.reversi.provider.view;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import cs3500.reversi.provider.model.Color;
//import cs3500.reversi.provider.model.ReadonlyReversiModel;
//
///**
// * Representation of the textual view of the Reversi mode, where black pieces
// * are represented as X and white pieces are represented by O.
// */
//public class ReversiTextualView implements TextualView {
//  private final ReadonlyReversiModel model;
//
//  public ReversiTextualView(ReadonlyReversiModel model) {
//    this.model = Objects.requireNonNull(model);
//  }
//
//  @Override
//  public String toString() {
//    StringBuilder sb = new StringBuilder();
//    for (int i = 0; i < model.getNumRows(); i++) {
//      Color[] currentRow = model.getRow(i);
//      appendSpacing(sb, currentRow.length);
//      List<String> rowStrings = new ArrayList<>();
//      for (Color player : currentRow) {
//        if (player == Color.BLACK) {
//          rowStrings.add("X");
//        } else if (player == Color.WHITE) {
//          rowStrings.add("O");
//        } else {
//          rowStrings.add("_");
//        }
//      }
//      sb.append(String.join(" ", rowStrings));
//      sb.append("\n");
//    }
//    return sb.toString();
//  }
//
//  private void appendSpacing(StringBuilder sb, int rowLength) {
//    int difference = model.getSize() - rowLength;
//    sb.append(" ".repeat(Math.max(0, difference)));
//  }
//}
