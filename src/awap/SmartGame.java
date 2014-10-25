package awap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class SmartGame extends Game {
  private final String leftCoin;
  private final String rightCoin;
  private final String centerCoin;
  private final String longCoin;

  private final int left;
  private final int right;

  private int move;

  public SmartGame(State init) {
    super(init);
    move = 2;
    switch(number) {
    case 0:
      leftCoin = "9 2";
      rightCoin = "2 10";
      centerCoin = "7 7";
      longCoin = "7 12";

      left = 3;
      right = 1;
      break;
    case 1:
      leftCoin = "2 10";
      rightCoin = "10 17";
      centerCoin = "7 12";
      longCoin = "12 12";

      left = 0;
      right = 2;  
      break;
    case 2:
      leftCoin = "10 17";
      rightCoin = "17 10";
      centerCoin = "12 12";
      longCoin = "12 7";

      left = 1;
      right = 3;
      break;
    case 3:
      leftCoin = "17 10";
      rightCoin = "9 2";
      centerCoin = "12 7";
      longCoin = "7 7";

      left = 2;
      right = 0;
      break;
    default: throw new RuntimeException("Invalid player");
    }
  }

  @Override
  protected Move findMove() {
    move++;
    if (move == 1) {
      Logger.log("Move 1");
      String[] toHit = null;
      int xstart = 0, ystart = 0, xend = 0, yend = 0;
      switch (number) {
      case 0:
        toHit = new String[]{"0 0","0 1", "1 1", "2 1", "1 2"};
        xstart = 0;
        xend = 3;
        ystart = 0;
        yend = 3;
        break;
      case 1:
        toHit = new String[]{"0 19","1 19", "1 18", "1 17", "2 18"};
        xstart = 0;
        xend = 3;
        ystart = 17;
        xend = 20;
        break;
      case 2:
        toHit = new String[]{"19 19","19 18", "18 18", "18 17", "17 18"};
        xstart = 17;
        xend = 20;
        ystart = 17;
        yend = 20;
        break;
      case 3:
        toHit = new String[]{"19 0","18 0", "18 1", "18 2", "17 1"};
        xstart = 17;
        xend = 20;
        xstart = 0;
        xend = 3;
        break;
      }
      return getMove(toHit, xstart, ystart, xend, yend);
    } else if (move == 2) {
      String[] toHit = null;
      int xstart = 0, ystart = 0, xend = 0, yend = 0;
      switch (number) {
      case 0:
        toHit = new String[]{"2 3","3 3", "4 3", "3 4", "3 5"};
        xstart = 2;
        ystart = 3;
        xend = 5;
        yend = 6;
        break;
      case 1:
        toHit = new String[]{"3 17","3 16", "3 15", "4 16", "5 16"};
        xstart = 3;
        xend = 6;
        ystart = 15;
        yend = 18;
        break;
      case 2:
        toHit = new String[]{"17 16","16 16", "15 16", "16 15", "16 14"};
        xstart = 15;
        xend = 18;
        ystart = 14;
        yend = 17;
        break;
      case 3:
        toHit = new String[]{"16 2","16 3", "16 4", "15 3", "14 3"};
        xstart = 14;
        xend = 17;
        ystart = 2;
        yend = 5;
        break;
      }
      return getMove(toHit, xstart, ystart, xend, yend);
    } else {
      return validMove();
    }
  }
  
  public Move validMove() {
    List<Move> validMoves = new ArrayList<Move>();
    for (int x = 0; x < dimension; x++) {
      for (int y = 0; y < dimension; y++) {
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

  public Move getMove(String[] toHit, int xstart, int ystart, int xend, int yend) {
    HashSet<String> hits = new HashSet<String>();
    for (String hit : hits) {
      hits.add(hit);
    }
    
    for (int x = Math.max(0, xstart - 3); x < Math.min(dimension, xend + 3); x++) {
      for (int y = Math.max(0, ystart - 3); y < Math.min(dimension, yend + 3); y++) {
        Logger.log("Search " + x + " " + y);
        for (int i = 0; i < blocks.size(); i++) {
          for (int r = 0; r < 4; r++) {
            if (matchesBlock(blocks.get(i).rotate(r), new Point(x, y), hits)
                && canPlace(blocks.get(i).rotate(r), new Point(x, y))) {
              return new Move(i, r, x, y);
            }
          }
        }
      }
    }
    
    Logger.log("Could not find piece");
    return validMove();
  }

  public boolean matchesBlock(Block block, Point p, HashSet<String> hits) {
    if (block.size() != hits.size()) {
      return false;
    }
    
    int count = 0;
    for (Point offset : block.getOffsets()) {
      Point q = offset.add(p);
      if (hits.contains(q.toString())) {
        count++;
      }
    }
    if (count == hits.size()) {
      return true;
    } else {
      Logger.log("Count: " + count);
      return false;
    }
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

          aMult |= bonusCoins.contains(q.toString());
        }

        boolean bMult = false;
        int bdist = -1;
        Point bPoint = b.getPoint();
        for (Point offset : bBlock.getOffsets()) {
          Point q = offset.add(bPoint);
          bdist = Math.max(bdist, q.distance(start));

          bMult |= bonusCoins.contains(q.toString());
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
}
