package org.usfirst.frc.team226.robot;

import edu.wpi.first.wpilibj.AnalogInput;

/**

 * A robot Java wrapper class for the Sharp GP2Y0A21YK sensor.

 * <p>

 * 

 * @author Nidhi Jaison, Team 226

 * 

 * @version 1.0

 */

public class Photoeye extends AnalogInput {

	private final double DEFAULT_MINIMUM = 0.5;

	private double minVolts;

	public Photoeye(int AnalogPort) {

		super(AnalogPort);

		minVolts = DEFAULT_MINIMUM;

	}

	public Photoeye(int AnalogPort, double minVoltage) {

		super(AnalogPort);

		minVolts = minVoltage;

	}

	public boolean getCovered() {

		if (this.getVoltage() < minVolts) {

			return false;

		} else {

			return true;
		}
	}

}
