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
 * @version 1.1
 */

public final class CheesyDrive {

	// TODO tune gains

	public static final double SKIM_GAIN = 0.5;
	public static final double TURN_GAIN = 1;

	// CHEESY DRIVE CALCULATION METHOD

	/**
	 * Calculates motor output for Cheesy Drive.
	 * <p>
	 * 
	 * @param rd
	 *            RobotDrive object to be driven
	 * @param throttle
	 *            throttle value
	 * @param turn
	 *            turn value
	 * @param quickTurn
	 *            {@code true} to enable quick turning, {@code false} to disable
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

	// AUXILIARY CALCULATION METHODS

	/**
	 * Calculates the value to be skimmed off of the given input.
	 * <p>
	 * 
	 * @param v
	 *            number to be skimmed
	 * @return the amount skimmed off of <b>v</b>
	 */
	static double skim(double v) {
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
	static double limit(double arg) {
		if (arg > 1) {
			return 1;
		} else if (arg < -1) {
			return -1;
		}
		return arg;
	}
}
