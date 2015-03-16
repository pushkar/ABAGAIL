/**
 * @author kmanda1
 */
package opt.ga;

public class BoardLocation {
	int xCoOrdinate, yCoOrdinate;

	public BoardLocation(int i, int j) {
		xCoOrdinate = i;
		yCoOrdinate = j;
	}

	public int getXCoOrdinate() {
		return xCoOrdinate;
	}

	@Override
	public boolean equals(Object o) {
		BoardLocation anotherLoc = (BoardLocation) o;
		return ((anotherLoc.getXCoOrdinate() == xCoOrdinate) && (anotherLoc
				.getYCoOrdinate() == yCoOrdinate));
	}

	public int getYCoOrdinate() {
		return yCoOrdinate;
	}

	public BoardLocation west() {
		return new BoardLocation(xCoOrdinate - 1, yCoOrdinate);
	}

	public BoardLocation east() {
		return new BoardLocation(xCoOrdinate + 1, yCoOrdinate);
	}

	public BoardLocation north() {
		return new BoardLocation(xCoOrdinate, yCoOrdinate - 1);
	}

	public BoardLocation south() {
		return new BoardLocation(xCoOrdinate, yCoOrdinate + 1);
	}

	public BoardLocation right() {
		return east();
	}

	public BoardLocation left() {
		return west();
	}

	public BoardLocation up() {
		return north();
	}

	public BoardLocation down() {
		return south();
	}

	public BoardLocation locationAt(String direction) {
		if (direction.equals("North")) {
			return north();
		}
		if (direction.equals("South")) {
			return south();
		}
		if (direction.equals("East")) {
			return east();
		}
		if (direction.equals("West")) {
			return west();
		} else {
			throw new RuntimeException("Unknown direction " + direction);
		}
	}

	@Override
	public String toString() {
		return " ( " + xCoOrdinate + " , " + yCoOrdinate + " ) ";
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + xCoOrdinate;
		result = result + yCoOrdinate;
		return result;
	}

}
