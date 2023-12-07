package cs3500.reversi.model;

public interface Posn {
  Posn add(Direction other);
  int getFirst();
  int getSecond();
}
