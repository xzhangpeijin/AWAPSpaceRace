package awap;

import java.util.List;

public class NaiveGame extends Game {

  public NaiveGame(State init) {
    super(init);
  }

  @Override
  protected Move findMove() {
    int N = state.getDimension();
    List<Block> blocks = state.getBlocks().get(number);

    for (int x = 0; x < N; x++) {
      for (int y = 0; y < N; y++) {
        for (int rot = 0; rot < 4; rot++) {
          for (int i = 0; i < blocks.size(); i++) {
            if (canPlace(blocks.get(i).rotate(rot), new Point(x, y))) {
              return new Move(i, rot, x, y);
            }
          }
        }
      }
    }

    return null;
  }
}
