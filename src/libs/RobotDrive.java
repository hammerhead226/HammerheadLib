package libs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * A class for driving drive platforms such as the Kit of Parts drive base,
 * "tank drive", or West Coast Drive.
 * 
 * Currently supports {@link #tankDrive}, {@link #cheesyDrive}, and
 * {@link #culverDrive}
 * 
 * For four and six wheel drivetrains, pass in the two front (master)
 * {@link #TalonSRX}'s, and set the other Talons to follow the master on their
 * side.
 *
 * <p>
 * Inputs smaller than -1 will be set to -1, and values larger than 1 will be
 * set to 1.
 *
 * @author Nidhi Jaison, team 226
 *
 */
public class RobotDrive {

	/**
	 * Master left {@link #TalonSRX} and right {@link #TalonSRX} to be used
	 */
	TalonSRX left, right;

	/**
	 * Constructor to construct a RobotDrive object
	 * 
	 * @param left
	 *            master {@link #TalonSRX}
	 * @param right
	 *            master {@link #TalonSRX}
	 */
	public RobotDrive(TalonSRX left, TalonSRX right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Simple tank drive code used by {@link #cheesyDrive} and {@link #culverDrive}
	 * 
	 * @param leftSpeed
	 *            value to pass into the left master talon
	 * @param rightSpeed
	 *            value to pass into the right master talon
	 * @param squaredInputs
	 *            {@code true} squares both inputs to decrease sensitivity
	 *            {@code false} to disable
	 */
	public void tankDrive(double leftSpeed, double rightSpeed, boolean squaredInputs) {
		if (squaredInputs) {
			leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
			rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
		}
		left.set(ControlMode.PercentOutput, limit(leftSpeed));
		right.set(ControlMode.PercentOutput, limit(rightSpeed));
	}

	/**
	 * Calculates motor output for Cheesy Drive using the quickturn button method.
	 * <p>
	 * 
	 * @param throttle
	 *            value to move forwards and backwards
	 * @param turn
	 *            value to move left and right
	 * @param quickTurn
	 *            {@code true} to enable quick turning (turning in place),
	 *            {@code false} to disable
	 * @param squaredInputs
	 *            {@code true} squares both inputs to decrease sensitivity
	 *            {@code false} to disable
	 */
	public void cheesyDrive(double throttle, double turn, boolean quickTurn, boolean squaredInputs) {
		if (squaredInputs) {
			throttle = Math.copySign(throttle * throttle, throttle);
			turn = Math.copySign(turn * turn, turn);
		}
		CheesyDrive.cheesyDrive(this, throttle, turn, quickTurn);
	}

	/**
	 * Calculates motor output for Cheesy Drive using the alternate method.
	 * Quickturning enables after a certain throttle threshold.
	 * 
	 * @param throttle
	 *            value to move forwards and backwards
	 * 
	 * @param turn
	 *            value to move left and right
	 * 
	 * @param squaredInputs
	 *            {@code true} squares both inputs to decrease sensitivity
	 *            {@code false} to disable
	 */
	public void cheesyDrive(double throttle, double turn, boolean squaredInputs) {
		if (squaredInputs) {
			throttle = Math.copySign(throttle * throttle, throttle);
			turn = Math.copySign(turn * turn, turn);
		}
		CheesyDrive.cheesyDriveAlt(this, throttle, turn);
	}

	/**
	 * Calculates motor output for Culver Drive using the 'quickturn' button method.
	 * 
	 * @param throttle
	 *            throttle value
	 * @param x
	 *            x coordinate of the steering stick
	 * @param y
	 *            y coordinate of the steering stick
	 * @param quickTurn
	 *            {@code true} to enable turning while throttle is 0, {@code false}
	 *            to disable
	 */
	public void culverDrive(double throttle, double x, double y, boolean quickTurn) {
		CulverDrive.culverDrive(this, throttle, x, y, quickTurn);
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
	public void culverDrive(double throttle, double x, double y) {
		CulverDrive.culverDriveAlt(this, throttle, x, y);
	}

	/**
	 * Limits joystick input range to [-1,1] A value outside of this range will
	 * automatically be set to 1 or -1
	 * 
	 * @param speed
	 *            input to limit
	 * @return limited value
	 */
	private double limit(double speed) {
		if (speed > 1.0) {
			return 1.0;
		}
		if (speed < -1.0) {
			return -1.0;
		}
		return speed;
	}
}
