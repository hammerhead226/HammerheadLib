package libs;

import edu.wpi.first.wpilibj.RobotDrive;

/**
 * A Java implementation of Team 33's Culver Drive.
 * <p>
 * Credit goes to Team 33's Bryan Culver for original idea and math.
 * 
 * @author Alec Minchington
 * @version 1.6
 */

public final class CulverDrive {

	// TODO tune gains

	private CulverDrive() {
	};

	// UNIT CIRCLE X/Y COORDS - for testing functions
	/*
	 * double[] x = { 1, Math.sqrt(3) / 2, Math.sqrt(2) / 2, 0.5, 0, -0.5,
	 * -Math.sqrt(2) / 2, -Math.sqrt(3) / 2, -1, -Math.sqrt(3) / 2,
	 * -Math.sqrt(2) / 2, -0.5, 0, 0.5, Math.sqrt(2) / 2, Math.sqrt(3) / 2, 1 };
	 * 
	 * double[] y = { 0, 0.5, Math.sqrt(2) / 2, Math.sqrt(3) / 2, 1,
	 * Math.sqrt(3) / 2, Math.sqrt(2) / 2, 0.5, 0, -0.5, -Math.sqrt(2) / 2,
	 * -Math.sqrt(3) / 2, -1, -Math.sqrt(3) / 2, -Math.sqrt(2) / 2, -0.5, 0 };
	 * 
	 * int[] angles = { 0, 30, 45, 60, 90, 120, 135, 150, 180, 210, 225, 240,
	 * 270, 300, 315, 330, 360 };
	 */

	// CULVER DRIVE CONSTANTS

	private static final double CULVER_DRIVE_RADIUS_GAIN = 1; // Tune this
	private static final double CULVER_DRIVE_RAW_GAIN = 1; // and this
	private static final double CULVER_DRIVE_ALT_RAW_GAIN = 1; // and this

	private static final double ONE_NINTIETH = 0.011111;
	private static final double ONE_TWENTIETH = 0.05;
	private static final double ONE_FORTY_FIFTH = 0.022222;
	private static final double ONE_FIFTH = 0.2;

	// CULVER DRIVE

	/**
	 * Calculates motor output for Culver Drive using the 'quickturn' button
	 * method.
	 * <p>
	 * 
	 * @param throttle
	 *            throttle value
	 * @param x
	 *            x coordinate of the steering stick
	 * @param y
	 *            y coordinate of the steering stick
	 * @param quickTurn
	 *            {@code true} to enable turning while throttle is 0,
	 *            {@code false} to disable
	 */
	public static void culverDrive(RobotDrive rd, double throttle, double x, double y, boolean quickTurn) {
		double radius = culverDriveCalculateRadius(throttle, x, y);
		double raw = culverDriveCalculateRaw(x, y);

		double left = throttle;
		double right = throttle;

		if (quickTurn) {
			left += raw;
			right -= raw;
		} else {
			// Reverse turning if moving backwards
			if (throttle < 0) {
				left -= radius;
				right += radius;
			} else {
				left += radius;
				right -= radius;
			}
		}

		// System.out.println("left: " + limitMotorOutput(left));
		// System.out.println("right: " + limitMotorOutput(right));

		rd.tankDrive(limit(left), limit(right));
	}

	/**
	 * Calculates motor output for Culver Drive using the 'alternate raw' (no
	 * quickturn) method.
	 * <p>
	 * 
	 * @param throttle
	 *            throttle value
	 * @param x
	 *            x coordinate of the steering stick
	 * @param y
	 *            y coordinate of the steering stick
	 */
	public static void culverDriveAlt(RobotDrive rd, double throttle, double x, double y) {
		double radius = culverDriveCalculateRadius(throttle, x, y);
		double raw = culverDriveCalculateAltRaw(x, y);

		double left = throttle;
		double right = throttle;

		// Reverse turning if moving backwards
		if (throttle > 0) {
			left -= radius + raw;
			right += radius + raw;
		} else {
			left += radius + raw;
			right -= radius + raw;
		}
		// System.out.println("left: " + limitMotorOutput(left));
		// System.out.println("right: " + limitMotorOutput(right));

		rd.tankDrive(limit(left), limit(right));
	}

	// CULVER DRIVE CALCULATION METHODS

	/**
	 * Caculates the 'radius' element of the Culver Drive algorithm.
	 * <p>
	 * 
	 * @param throttle
	 *            throttle value
	 * @param x
	 *            x coordinate of the steering stick
	 * @param y
	 *            y coordinate of the steering stick
	 * @return {@code -1} to {@code 1} based on the magnitude of the stick, the
	 *         angle theta from vertical, an enablement curve, and a calibrated
	 *         gain.
	 */
	private static double culverDriveCalculateRadius(double throttle, double x, double y) {
		double r = Math.sqrt((x * x) + (y * y));
		double theta = getThetaFromVertical(x, y);

		return r * throttle * normalizeAngle90(theta) * CULVER_DRIVE_RADIUS_GAIN
				* enablementCurveRadius(Math.abs(theta));
	}

	/**
	 * Calculates the 'raw' element used in the Culver Drive algorithm.
	 * <p>
	 * 
	 * @param x
	 *            x coordinate of the steering stick
	 * @param y
	 *            y coordinate of the steering stick
	 * @return {@code -1} to {@code 1} based on the magnitude of the stick, the
	 *         angle theta from vertical, an enablement curve, and a calibrated
	 *         gain.
	 */
	private static double culverDriveCalculateRaw(double x, double y) {
		double r = Math.sqrt((x * x) + (y * y));
		double theta = getThetaFromVertical(x, y);

		return r * normalizeAngle90(theta) * CULVER_DRIVE_RAW_GAIN * enablementCurveRadius(Math.abs(theta));
	}

	/**
	 * Calculates the 'alternate raw' element used in the 'alternate raw' (no
	 * quickturn) Culver Drive method.
	 * <p>
	 * 
	 * @param x
	 *            x coordinate of the steering stick
	 * @param y
	 *            y coordinate of the steering stick
	 * @return {@code -1} to {@code 1} based on the magnitude of the stick, the
	 *         angle theta form vertical, an alternate enablement curve, and a
	 *         calibrated gain.
	 */
	private static double culverDriveCalculateAltRaw(double x, double y) {
		double r = Math.sqrt((x * x) + (y * y));
		double theta = getThetaFromVertical(x, y);

		return r * getThetaSign(theta) * CULVER_DRIVE_ALT_RAW_GAIN * enablementCurveAltRaw(Math.abs(theta));
	}

	// ANGLE OPERATIONS

	/**
	 * Calculates the angle from stick vertical.
	 * <p>
	 * 
	 * @param x
	 *            x coordinate of the steering stick
	 * @param y
	 *            y coordinate of the steering stick
	 * @return the angle theta from stick vertical in degrees.
	 */
	private static double getThetaFromVertical(double x, double y) {
		/*
		 * Old code -> double theta = Math.toDegrees(Math.atan2(y, x)) - 90; if
		 * (theta <= -180) { theta += 360; } return theta;
		 */
		// 0.0 is added to fix rounding to -0.0
		return 0.0 + (-Math.toDegrees(Math.atan2(x, y)));
	}

	/**
	 * Gets the sign of the angle theta. (Technically works for any number
	 * including non-angles)
	 * <p>
	 * 
	 * @param theta
	 *            an angle measure in degrees
	 * @return {@code 1} if {@code theta > 0}, else {@code -1}.
	 */
	private static double getThetaSign(double theta) {
		if (theta > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Normalizes an angle between 0 and 90 degrees
	 * <p>
	 * 
	 * @param absTheta
	 *            absolute value of an angle theta in degrees
	 * @return {@code 0} fading to {@code 1} as <b>absTheta</b> approaches 90
	 *         degrees.
	 */
	private static double normalizeAngle90(double absTheta) {
		if (absTheta <= 90) {
			return ONE_NINTIETH * absTheta;
		} else {
			return 0;
		}
	}

	// ENABLEMENT CURVE METHODS

	/**
	 * Calculates the enablement curve of the 'radius' element of the Culver
	 * Drive algorithm.
	 * <p>
	 * 
	 * @param absTheta
	 *            absolute value of an angle theta in degrees
	 * @return {@code 1} if <b>absTheta</b> {@code <= 95}, fading to {@code 0}
	 *         at 115.
	 */
	private static double enablementCurveRadius(double absTheta) {
		if (absTheta < 95) {
			return 1;
		} else {
			absTheta -= 95;
			double curve = 1 - (ONE_TWENTIETH * absTheta);
			if (curve < 0) {
				return 0;
			} else {
				return curve;
			}
		}
	}

	/**
	 * Calculates the enablement curve of the 'radius' element of the Culver
	 * Drive algorithm.
	 * <p>
	 * 
	 * @param absTheta
	 *            absolute value of an angle theta in degrees
	 * @return {@code 1} if <b>absTheta</b> {@code < 95}, fading to {@code 0} at
	 *         115.
	 */
	private static double enablementCurveAltRaw(double absTheta) {
		if (absTheta <= 90) {
			return 0;
		} else if (absTheta > 90 && absTheta <= 135) {
			absTheta -= 90;
			double curve = (ONE_FORTY_FIFTH * absTheta);
			return curve;
		} else if (absTheta > 135 && absTheta < 175) {
			return 1;
		} else if (absTheta >= 175 && absTheta < 180) {
			absTheta -= 175;
			double curve = 1 - (ONE_FIFTH * absTheta);
			return curve;
		} else {
			return 0;
		}
	}

	// OUTPUT LIMITER

	/**
	 * Limits the input to +/- 1.
	 * <p>
	 * 
	 * @param arg
	 *            the number to be limited
	 * @return {@code 1} if <b>arg</b> {@code > 1}, {@code -1} if <b>arg</b>
	 *         {@code < -1}.
	 */
	private static double limit(double arg) {
		if (arg > 1) {
			return 1;
		} else if (arg < -1) {
			return -1;
		} else {
			return arg;
		}
	}
}
