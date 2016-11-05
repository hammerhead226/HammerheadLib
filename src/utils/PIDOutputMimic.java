package utils;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 * Mimic of a real PIDOutput. Allows a PID controller's PID output value to be read without
 * setting any hardware to that value. This object should be passed to the
 * PIDController class and then be used to read the PID output elsewhere in your
 * code. Mainly useful for movements that use multiple PID controllers at once.
 * 
 * @author Alec Minchington, Team 226
 * 
 * @version 1.0
 */

public class PIDOutputMimic implements PIDOutput {

	private double output;

	public PIDOutputMimic() {
		output = 0;
	}

	@Override
	public void pidWrite(double output) {
		this.output = output;
	}

	public double getPIDOutput() {
		return output;
	}
}
