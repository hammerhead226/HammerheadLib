package libs;

/**
 * Class representing an ordered pair, (x ,y)
 */
public class Pair {

	final double x;
	final double y;

	public Pair(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[ " + x + ", " + y + " ]";
	}
}