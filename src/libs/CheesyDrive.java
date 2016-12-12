package libs;

import edu.wpi.first.wpilibj.RobotDrive;

/**
 * A Java implementation of Team 254's Cheesy Drive.
 * <p>
 * Math from <a href=
 * "https://www.chiefdelphi.com/forums/showpost.php?p=1181728&postcount=2">this
 * thread</a>
 * 
 * @author Alec Minchington, Team 226
 *
 * @version 1.3
 */

public final class CheesyDrive {

	// TODO tune gains

	// THROTTLE/TURN VALUES - for testing functions
	/*
	 * double[] throttleTurn = {-1.0, -0.75, -0.5, -0.25, 0, 0.25, 0.5, 0.75,
	 * 1.0};
	 */

	// CHEESY DRIVE CONSTANTS

	private static final double SKIM_GAIN = 0.5;
	private static final double TURN_GAIN = 1.5;
	private static double THROTTLE_THRESHOLD = 0.5;

	// CHEESY DRIVE CALCULATION METHODS

	/**
	 * Calculates motor output for Cheesy Drive using the quickturn button
	 * method.
	 * <p>
	 * 
	 * @param rd
	 *            RobotDrive object to be driven
	 * @param throttle
	 *            throttle value
	 * @param turn
	 *            turn value
	 * @param quickTurn
	 *            {@code true} to enable quick turning (turning in place),
	 *            {@code false} to disable
	 */
	public static void cheesyDrive(RobotDrive rd, double throttle, double turn, boolean quickTurn) {
		if (!quickTurn) {
			turn = turn * (TURN_GAIN * Math.abs(throttle));
		}

		double leftRaw = throttle - turn;
		double rightRaw = throttle + turn;

		double left = leftRaw + skim(rightRaw);
		double right = rightRaw + skim(leftRaw);

		rd.tankDrive(limit(left), limit(right));
	}
	
	/**
	 * Calculates motor output for Cheesy Drive using the alternate method.
	 * Quickturning enables after a certain throttle threshold.
	 * <p>
	 * 
	 * @param rd
	 *            RobotDrive object to be driven
	 * @param throttle
	 *            throttle value
	 * @param turn
	 *            turn value
	 */
	public static void cheesyDriveAlt(RobotDrive rd, double throttle, double turn) {
		if (throttle > THROTTLE_THRESHOLD) {
			turn = turn * (TURN_GAIN * Math.abs(throttle));
		}

		double leftRaw = throttle - turn;
		double rightRaw = throttle + turn;

		double left = leftRaw + skim(rightRaw);
		double right = rightRaw + skim(leftRaw);

		rd.tankDrive(limit(left), limit(right));
	}

	// AUXILIARY CALCULATION METHODS

	/**
	 * Calculates the value to be skimmed off of the given input.
	 * <p>
	 * 
	 * @param v
	 *            number to be skimmed
	 * @return the amount skimmed off of <b>v</b>
	 */
	private static double skim(double v) {
		if (v > 1.0) {
			return -((v - 1.0) * SKIM_GAIN);
		} else if (v < -1.0) {
			return -((v + 1.0) * SKIM_GAIN);
		} else {
			return 0;
		}
	}

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
		}
		return arg;
	}
}
