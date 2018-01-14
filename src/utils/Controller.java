package utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * A robot Java wrapper class for the Xbox 360 and Logitech F310 controllers.
 * Includes built-in variable joystick deadband.
 * <p>
 * 
 * @author Alec Minchington, Team 226
 * 
 * @version 1.3
 */

public class Controller extends Joystick {

	private final double DEFAULT_DEADBAND = 0.15;
	private double deadband;

	public Controller(int usbPort) {
		super(usbPort);
		this.deadband = DEFAULT_DEADBAND;
	}

	public Controller(int usbPort, double deadband) {
		super(usbPort);
		this.deadband = deadband;
	}

	private Button A = new JoystickButton(this, 1);
	private Button B = new JoystickButton(this, 2);
	private Button X = new JoystickButton(this, 3);
	private Button Y = new JoystickButton(this, 4);
	private Button LB = new JoystickButton(this, 5);
	private Button RB = new JoystickButton(this, 6);
	private Button SELECT = new JoystickButton(this, 7);
	private Button START = new JoystickButton(this, 8);
	private Button LS = new JoystickButton(this, 9);
	private Button RS = new JoystickButton(this, 10);

	// JOYSTICKBUTTONS

	public Button getAButton() {
		return A;
	}

	public Button getBButton() {
		return B;
	}

	public Button getXButton() {
		return X;
	}

	public Button getYButton() {
		return Y;
	}

	public Button getLBButton() {
		return LB;
	}

	public Button getRBButton() {
		return RB;
	}

	public Button getSELECTButton() {
		return SELECT;
	}

	public Button getSTARTButton() {
		return START;
	}

	public Button getLSButton() {
		return LS;
	}

	public Button getRSButton() {
		return RS;
	}

	// STICKS

	/**
	 * @return X-value of the left joystick
	 */
	public double getLeftJoystick_X() {
		if (Math.abs(getX()) > deadband) {
			// Correctly inverted -- stick left returns 1.0
			return -getX();
		} else {
			return 0;
		}
	}

	/**
	 * @return Y-value of the left joystick
	 */
	public double getLeftJoystick_Y() {
		if (Math.abs(getY()) > deadband) {
			// Correctly inverted -- stick up returns 1.0
			return -getY();
		} else {
			return 0;
		}
	}

	/**
	 * @return X-value of the right joystick
	 */
	public double getRightJoystick_X() {
		if (Math.abs(getRawAxis(4)) > deadband) {
			// Correctly inverted -- stick left returns 1.0
			return -getRawAxis(4);
		} else {
			return 0;
		}
	}

	/**
	 * @return Y-value of the right joystick
	 */
	public double getRightJoystick_Y() {
		if (Math.abs(getRawAxis(5)) > deadband) {
			// Correctly inverted -- stick up returns 1.0
			return -getRawAxis(5);
		} else {
			return 0;
		}
	}

	// BUTTONS

	/**
	 * Gets current state of the A button.
	 * <p>
	 * 
	 * @return {@code true} if the A button is pressed, {@code false} otherwise
	 */
	public boolean getAButtonPressed() {
		return getRawButton(1);
	}

	/**
	 * Gets current state of the B button.
	 * <p>
	 * 
	 * @return {@code true} if the B button is pressed, {@code false} otherwise
	 */
	public boolean getBButtonPressed() {
		return getRawButton(2);
	}

	/**
	 * Gets current state of the X button.
	 * <p>
	 * 
	 * @return {@code true} if the X button is pressed, {@code false} otherwise
	 */
	public boolean getXButtonPressed() {
		return getRawButton(3);
	}

	/**
	 * Gets current state of the Y button.
	 * <p>
	 * 
	 * @return {@code true} if the Y button is pressed, {@code false} otherwise
	 */
	public boolean getYButtonPressed() {
		return getRawButton(4);
	}

	/**
	 * Gets current state of the left bumper.
	 * <p>
	 * 
	 * @return {@code true} if the left bumper is pressed, {@code false}
	 *         otherwise
	 */
	public boolean getLBButtonPressed() {
		return getRawButton(5);
	}

	/**
	 * Gets current state of the right bumper.
	 * <p>
	 * 
	 * @return {@code true} if the right bumper is pressed, {@code false}
	 *         otherwise
	 */
	public boolean getRBButtonPressed() {
		return getRawButton(6);
	}

	/**
	 * Gets current state of the SELECT button.
	 * <p>
	 * 
	 * @return {@code true} if the SELECT button is pressed, {@code false}
	 *         otherwise
	 */
	public boolean getSELECTButtonPressed() {
		return getRawButton(7);
	}

	/**
	 * Gets current state of the START button.
	 * <p>
	 * 
	 * @return {@code true} if the START button is pressed, {@code false}
	 *         otherwise
	 */
	public boolean getSTARTButtonPressed() {
		return getRawButton(8);
	}

	/**
	 * Gets current state of the left stick.
	 * <p>
	 * 
	 * @return {@code true} if the left stick is clicked in, {@code false}
	 *         otherwise
	 */
	public boolean getLSButtonPressed() {
		return getRawButton(9);
	}

	/**
	 * Gets current state of the right stick.
	 * <p>
	 * 
	 * @return {@code true} if the right stick is clicked in, {@code false}
	 *         otherwise
	 */
	public boolean getRSButtonPressed() {
		return getRawButton(10);
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_A = 0;

	public boolean getAButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(1)) {
			if (now - latest_A > period) {
				latest_A = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_B = 0;

	public boolean getBButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(2)) {
			if (now - latest_B > period) {
				latest_B = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_X = 0;

	public boolean getXButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(3)) {
			if (now - latest_X > period) {
				latest_X = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_Y = 0;

	public boolean getYButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(4)) {
			if (now - latest_Y > period) {
				latest_Y = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_LB = 0;

	public boolean getLBButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(5)) {
			if (now - latest_LB > period) {
				latest_LB = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_RB = 0;

	public boolean getRBButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(6)) {
			if (now - latest_RB > period) {
				latest_RB = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_BACK = 0;

	public boolean getBACKButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(7)) {
			if (now - latest_BACK > period) {
				latest_BACK = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_START = 0;

	public boolean getSTARTButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(8)) {
			if (now - latest_START > period) {
				latest_START = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_LS = 0;

	public boolean getLSButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(9)) {
			if (now - latest_LS > period) {
				latest_LS = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets current state of the button.
	 * 
	 * @param period
	 *            interval at which to return {@code true} if the button is pressed
	 * @return {@code true} every {@code period} seconds if thebutton is pressed,
	 *         {@code false} otherwise
	 */
	private double latest_RS = 0;

	public boolean getRSButtonPressed(double period) {
		double now = Timer.getFPGATimestamp();
		if (getRawButton(10)) {
			if (now - latest_RS > period) {
				latest_RS = now;
				return true;
			}
		}
		return false;
	}

	/**
	 * @return value of the left trigger
	 */
	public double getLeftTrigger() {
		return getRawAxis(2);
	}

	/**
	 * @return value of the right trigger
	 */
	public double getRightTrigger() {
		return getRawAxis(3);
	}

	/**
	 * The right trigger is positive and left trigger is negative This means
	 * that the two triggers' values add to give the result, so pressing both
	 * gives 0.
	 * <p>
	 * 
	 * @return value of the combined axis of the triggers
	 */
	public double getTriggers() {
		return getLeftTrigger() - getRightTrigger();
	}

	/**
	 * Gets current angle of the directional pad.
	 * <p>
	 * 
	 * @return value of the directional pad POV-hat (angle 0-360)
	 */
	public int getDPad() {
		return getPOV(0);
	}
	
	public void setVibration(double val) {
		setRumble(RumbleType.kLeftRumble, val);
		setRumble(RumbleType.kRightRumble, val);
	}

	// UTILS

	/**
	 * Gets the joystick deadband threshold.
	 * 
	 * @return value of the joystick deadband threshold
	 */
	public double getDeadband() {
		return deadband;
	}
}