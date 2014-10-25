package awap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

public abstract class Game {
	protected State state;
	protected Integer number;
	protected final int dimension;

	private String[] bonus = {"9 2","17 9","2 10","10 17","7 7","12 7","12 12","7 12"};
	protected final Set<String> bonusCoins;
	
	protected List<Block> blocks;
	
	protected final Point[] corners;
	
	public Game(State init) {
	  this.state = init;
    this.number = init.getNumber().get();
    this.dimension = init.getDimension();

    Logger.log("Bonus Coins:");
    bonusCoins = new HashSet<String>();
    for (String coin : bonus) {
      bonusCoins.add(coin);
      Logger.log(coin.toString());
    }
    
    this.blocks = state.getBlocks().get(number);
    
    this.corners = new Point[]{ new Point(0, 0), new Point(dimension - 1, 0), 
        new Point(dimension - 1, dimension - 1), new Point(0, dimension - 1) };
	}

	public Optional<Move> updateState(State newState) {
		if (newState.getError().isPresent()) {
			Logger.log(newState.getError().get());
			return Optional.absent();
		}

		if (newState.getMove() != -1) {
	    Logger.log("Making move");
			return Optional.fromNullable(findMove());
		}

		Logger.log("Updating state");
		state = newState;
		if (newState.getNumber().isPresent()) {
      number = newState.getNumber().get();
    }
		blocks = state.getBlocks().get(number);

		return Optional.absent();
	}

	protected abstract Move findMove();

	protected int getPos(int x, int y) {
		return state.getBoard().get(x).get(y);
	}

	protected boolean canPlace(Block block, Point p) {
		boolean onAbsCorner = false, onRelCorner = false;
		int N = state.getDimension() - 1;

		Point corner = corners[number];

		for (Point offset : block.getOffsets()) {
			Point q = offset.add(p);
			int x = q.getX(), y = q.getY();

			if (x > N || x < 0 || y < 0 || y > N
          || getPos(x, y) >= 0
          || getPos(x, y) == -2
					|| (x > 0 && getPos(x - 1, y) == number)
					|| (y > 0 && getPos(x, y - 1) == number)
					|| (x < N && getPos(x + 1, y) == number)
					|| (y < N && getPos(x, y + 1) == number)) {
				return false;
			}

			onAbsCorner = onAbsCorner || q.equals(corner);
			onRelCorner = onRelCorner
					|| (x > 0 && y > 0 && getPos(x - 1, y - 1) == number)
					|| (x < N && y > 0 && getPos(x + 1, y - 1) == number)
					|| (x > 0 && y < N && getPos(x - 1, y + 1) == number)
					|| (x < N && y < N && getPos(x + 1, y + 1) == number);
		}

		return !((getPos(corner.getX(), corner.getY()) < 0 && !onAbsCorner) 
		    || (!onAbsCorner && !onRelCorner));
	}
}
