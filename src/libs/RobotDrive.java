package src.libs;

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
	private TalonSRX left, right;

	/**
	 * Sensitivity value to be used in {@link #drive}
	 */
	private double sensitivity;
	private double defaultSensitivity = 0.5;

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
		sensitivity = defaultSensitivity;
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
		
		CheesyDrive.cheesyDrive(this, throttle, turn, quickTurn, squaredInputs);
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
		
		CheesyDrive.cheesyDriveAlt(this, throttle, turn, squaredInputs);
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
	public void culverDrive(double throttle, double x, double y, boolean quickTurn, boolean squaredInputs) {
		CulverDrive.culverDrive(this, throttle, x, y, quickTurn, squaredInputs);
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
	public void culverDrive(double throttle, double x, double y, boolean squaredInputs) {
		CulverDrive.culverDriveAlt(this, throttle, x, y, squaredInputs);
	}

	/**
	 * Drive the motors at "outputMagnitude" and "curve". outputMagnitude is a -1.0
	 * to +1.0 value, where 0.0 for both represents stopped and not turning.
	 * {@literal curve < 0 will turn left
	 * and curve > 0} will turn right.
	 *
	 * <p>
	 * The algorithm for steering provides a constant turn radius for any normal
	 * speed range, both forward and backward. This method uses a default
	 * sensitivity of 0.5
	 *
	 * <p>
	 * This function will most likely be used in an autonomous routine.
	 *
	 * @param outputMagnitude
	 *            The speed setting for the outside wheel in a turn, forward or
	 *            backwards, +1 to -1.
	 * @param curve
	 *            The rate of turn, constant for different forward speeds. Set
	 *            {@literal
	 *                        curve < 0 for left turn or curve > 0 for right turn.}
	 *            Set curve = e^(-r/w) to get a turn radius r for wheelbase w of
	 *            your robot. Conversely, turn radius r = -ln(curve)*w for a given
	 *            value of curve and wheelbase w.
	 */
	public void drive(double outputMagnitude, double curve) {
		final double leftOutput;
		final double rightOutput;

		if (curve < 0) {
			double value = Math.log(-curve);
			double ratio = (value - sensitivity) / (value + sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude / ratio;
			rightOutput = outputMagnitude;
		} else if (curve > 0) {
			double value = Math.log(curve);
			double ratio = (value - sensitivity) / (value + sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude / ratio;
		} else {
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude;
		}
		tankDrive(leftOutput, rightOutput, false);
	}

	/**
	 * Method to set sensitivity value to use in {@link #drive}. A higher
	 * sensitivity will result in sharper turns for a given curve value.
	 * 
	 * @param value
	 *            sets the turning sensitvity
	 */
	public void setSensitivity(double value) {
		sensitivity = value;
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
