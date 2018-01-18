package libs;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

/**
 * Swerve drive wheel speed & angle calculator.
 * <p>
 * Math derived from
 * <a href="https://www.chiefdelphi.com/media/papers/download/3027">this
 * paper</a>.
 * 
 * @author Alec Minchington, Team 226
 *
 */
public class SwerveDrive {

	private double length;
	private double width;
	private double phi;

	private double[] speeds;
	private double[] angles;

	private Vector[] outputVectors;

	private Pair[] wheelCoords;

	/**
	 * Construct a new SwerveDrive given the drive base's length and width. Assumes
	 * 4 swerve modules.
	 * 
	 * @param l
	 *            Length
	 * @param w
	 *            Width
	 */
	public SwerveDrive(double l, double w) {
		this.length = l;
		this.width = w;
		this.phi = atan2(length, width);
	}

	/**
	 * Construct a new SwerveDrive object given the coordinates of the each swerve
	 * modules in relation to a point on the robot (usually center)
	 * 
	 * @param pairs
	 *            x, y coordinates of each swerve module
	 */
	public SwerveDrive(Pair[] pairs) {
		wheelCoords = pairs;
		speeds = new double[wheelCoords.length];
		angles = new double[wheelCoords.length];
		outputVectors = new Vector[wheelCoords.length];
		for (int i = 0; i < outputVectors.length; i++) {
			outputVectors[i] = new Vector(0, 0);
		}
	}

	/**
	 * Calculate the vector for each module of a 4-module swerve chassis
	 * 
	 * @param str
	 *            Strafe
	 * @param fwd
	 *            Forward throttle
	 * @param rcw
	 *            Clockwise rotation
	 * @param gyro
	 *            Gyro angle, for field-centric driving
	 * @return An array containing the desired heading and wheel speed for each
	 *         module
	 */
	public double[][] calc4WheelVectors(double str, double fwd, double rcw, double gyro) {
		double mag = mag(str, fwd);

		double fcTheta = atan2(fwd, str) - toRadians(gyro);

		double fcFwd = mag * sin(fcTheta);
		double fcStr = mag * cos(fcTheta);

		double A = fcStr - rcw * sin(phi);
		double B = fcStr + rcw * sin(phi);
		double C = fcFwd - rcw * cos(phi);
		double D = fcFwd + rcw * cos(phi);

		double[] fl = { B, D };
		double[] fr = { B, C };
		double[] rl = { A, D };
		double[] rr = { A, C };

		double[] speeds = { mag(fl[0], fl[1]), mag(fr[0], fr[1]), mag(rl[0], rl[1]), mag(rr[0], rr[1]) };

		normalize(speeds);

		double[] angles = { degreeAngle(fl[0], fl[1]), degreeAngle(fr[0], fr[1]), degreeAngle(rl[0], rl[1]),
				degreeAngle(rr[0], rr[1]) };

		double[][] v = new double[4][2];
		for (int i = 0; i < 4; i++) {
			v[i][0] = speeds[i];
			v[i][1] = angles[i];
		}

		return v;
	}

	/**
	 * Calculate the vector for each module of a swerve chassis with n number of
	 * modules
	 * 
	 * @param strafe
	 *            Left/right movement
	 * @param throttle
	 *            Forward/backward movement
	 * @param rotation
	 *            Clockwise rotation
	 * @param gyroAngle
	 *            Angle reading of a gyroscope sensor, for field-centric drive
	 * @return a Vector array containing the heading and wheel speed for each module
	 */
	public Vector[] calcWheelVectorsN(double strafe, double throttle, double rotation, double gyroAngle) {

		double mag = mag(throttle, strafe);
		double fcTheta = atan2(throttle, strafe) - toRadians(gyroAngle);

		for (int i = 0; i < wheelCoords.length; i++) {
			double fcFwd = mag * sin(fcTheta);
			double fcStr = mag * cos(fcTheta);

			double phi = atan2(wheelCoords[i].y, wheelCoords[i].x);
			double Wx = fcFwd + rotation * sin(phi);
			double Wy = fcStr + rotation * cos(phi);

			speeds[i] = mag(Wx, Wy);
			angles[i] = degreeAngle(Wx, Wy);
		}

		normalize(speeds);

		for (int i = 0; i < outputVectors.length; i++) {
			outputVectors[i].magnitude = speeds[i];
			outputVectors[i].angle = angles[i];
		}

		return outputVectors;
	}

	public double findAbsoluteAngle(double currentAngle, double targetAngle) {
		return currentAngle + toBase180(targetAngle - toBase180(currentAngle));
	}

	/**
	 * Normalize the given angle to between +/- 180
	 * 
	 * @param angle
	 *            angle to normalize
	 * @return normalized angle
	 */
	private static double toBase180(double angle) {
		while (angle > 180) {
			angle -= 360;
		}
		while (angle < -180) {
			angle += 360;
		}

		return angle;
	}

	/**
	 * Divides all the elements in the given array by the largest value in that
	 * array.
	 * 
	 * @param arg
	 *            the list to normalize
	 */
	private static void normalize(double[] arg) {
		double max = 1.0;
		for (double s : arg) {
			if (abs(s) > max) {
				max = abs(s);
			}
		}
		for (int i = 0; i < arg.length; i++) {
			arg[i] /= max;
		}
	}

	/**
	 * Calculate cartesian magnitude.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return distance from the origin
	 */
	private static double mag(double x, double y) {
		return sqrt(x * x + y * y);
	}

	/**
	 * Calculate degree angle between the given vectors
	 * 
	 * @param y
	 *            vector 1
	 * @param x
	 *            vector 2
	 * @return degree angle
	 */
	private static double degreeAngle(double y, double x) {
		return toDegrees(atan2(y, x));
	}

}
