package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AverageEncoderSource implements PIDSource {
	
	Robot robot;
	Encoder l;
	Encoder r;
	
	public AverageEncoderSource (Robot robot, Encoder l, Encoder r){
		this.robot = robot;
		this.l = l;
		this.r = r;
		
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return (l.getDistance() + r.getDistance()) / 2.0;
	}

}
