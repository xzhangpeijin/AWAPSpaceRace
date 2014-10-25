package awap;

public class Move {
	int index;
	int rotations;
	Point point;
	
	public Move(int index, int rotations, int x, int y) {
		this.index = index;
		this.rotations = rotations;
		point = new Point(x, y);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getRotations() {
		return rotations;
	}

	public void setRotations(int rotations) {
		this.rotations = rotations;
	}

	public int getX() {
		return point.getX();
	}
	
	public void setPoint(int x, int y) {
	  point = new Point(x, y);
	}

	public void setX(int x) {
		setPoint(x, getY());
	}

	public int getY() {
		return point.getY();
	}

	public void setY(int y) {
		setPoint(getX(), y);
	}
	
	public Point getPoint() {
	  return point;
	}

	@Override
	public String toString() {
		return String.format("%d %d %d %d", index, rotations, point.getX(), point.getY());
	}
}
