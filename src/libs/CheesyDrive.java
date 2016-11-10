package libs;

import edu.wpi.first.wpilibj.RobotDrive;

/**
 * A Java implementation of Team 254's Cheesy Drive.
 * <p>
 * Math from  <a href="https://www.chiefdelphi.com/forums/showpost.php?p=1181728&postcount=2">this thread</a>
 * 
 * @author Alec Minchington, Team 226
 *
 * @version 1.0
 */

public final class CheesyDrive {
	
	//TODO tune gains

	public static final double SKIM_GAIN = 0.5;
	public static final double TURN_GAIN = 1;

	// CHEESY DRIVE CALCULATION METHOD

	public static void cheesyDrive(double throttle, double turn, boolean quickTurn, RobotDrive drive) {
		if (!quickTurn) {
			turn = turn * (TURN_GAIN * Math.abs(throttle));
		}

		double leftRaw = throttle - turn;
		double rightRaw = throttle + turn;

		double left = leftRaw + skim(rightRaw);
		double right = rightRaw + skim(leftRaw);

		drive.tankDrive(limit(left), limit(right));
	}

	// AUXILIARY CALCULATION METHODS

	static double skim(double v) {
		if (v > 1.0) {
			return -((v - 1.0) * SKIM_GAIN);
		} else if (v < -1.0) {
			return -((v + 1.0) * SKIM_GAIN);
		} else {
			return 0;
		}
	}

	static double limit(double arg) {
		if (arg > 1) {
			return 1;
		} else if (arg < -1) {
			return -1;
		}
		return arg;
	}
}
