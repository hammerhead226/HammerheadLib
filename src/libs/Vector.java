package libs;


/**
 * Class representing a vector, (magnitude, angle)
 */
public class Vector {
	double magnitude;
	double angle;

	public Vector(double magnitude, double angle) {
		this.magnitude = magnitude;
		this.angle = angle;
	}

	@Override
	public String toString() {
		return "[ " + magnitude + ", " + angle + " ]";
	}
}