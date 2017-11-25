package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class RotationPIDOutput implements PIDOutput {
	
	Robot r;
	
	public RotationPIDOutput (Robot robot){
		r = robot;
	}
	
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		r.tRight.set(output);
		r.sRight.set(output);
		r.tLeft.set(output);
		r.sLeft.set(output);
		
	}

}
