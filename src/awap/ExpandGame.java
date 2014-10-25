package awap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExpandGame extends Game {
  
  public ExpandGame(State init) {
    super(init);
  }

  public Comparator<Move> getComparator(final Point start) {
    return new Comparator<Move>() {
      @Override
      public int compare(Move a, Move b) {
        List<Block> blocks = state.getBlocks().get(number);
        Block aBlock = blocks.get(a.index);
        aBlock.rotate(a.rotations);
        Block bBlock = blocks.get(b.index);
        bBlock.rotate(b.rotations);

        int aSize = aBlock.size();
        int bSize = bBlock.size();

        boolean aMult = false;
        int adist = -1;
        Point aPoint = a.getPoint();
        for (Point offset : aBlock.getOffsets()) {
          Point q = offset.add(aPoint);
          adist = Math.max(adist, q.distance(start));
          
          aMult |= bonusCoins.contains(q.getX() + " " + q.getY());
        }

        boolean bMult = false;
        int bdist = -1;
        Point bPoint = b.getPoint();
        for (Point offset : bBlock.getOffsets()) {
          Point q = offset.add(bPoint);
          bdist = Math.max(bdist, q.distance(start));
          
          bMult |= bonusCoins.contains(q.getX() + " " + q.getY());
        }
        
        if (aMult) {
          aSize *= 3;
        }
        
        if (bMult) {
          bSize *= 3;
        }

        if (aSize == bSize) {
          return bdist - adist;
        } else {
          return bSize - aSize;
        }
      }
    };
  }

  @Override
  protected Move findMove() {
    int N = state.getDimension();
    List<Block> blocks = state.getBlocks().get(number);

    List<Move> validMoves = new ArrayList<Move>();
    for (int x = 0; x < N; x++) {
      for (int y = 0; y < N; y++) {
        for (int rot = 0; rot < 4; rot++) {
          for (int i = 0; i < blocks.size(); i++) {
            if (canPlace(blocks.get(i).rotate(rot), new Point(x, y))) {
              validMoves.add(new Move(i, rot, x, y));
            }
          }
        }
      }
    }

    if (validMoves.size() > 0) {
      Collections.sort(validMoves, getComparator(corners[number]));
      return validMoves.get(0);
    } else {
      return null;
    }
  }
}
