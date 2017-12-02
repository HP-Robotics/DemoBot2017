package org.usfirst.frc.team2823.robot;

import java.util.TimerTask;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;

public class SnazzyMotionPlanner extends SnazzyPIDCalculator {
	private java.util.Timer m_controlLoop;
	private boolean m_calibrating;
	private SnazzyLog m_log;
	private double m_calStart;
	private double m_lastCal;
	private double m_lastDist;
	private double m_lastV;
	private int m_count;

	
	public SnazzyMotionPlanner(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output,
			double period, String fname) {
		super(Kp, Ki, Kd, Kf, source, output, period, fname);
		m_controlLoop = new java.util.Timer();
		m_controlLoop.schedule(new PIDTask(this), 0L, (long) (period * 1000));
		m_log = new SnazzyLog();
	}	 

 public void free() {
	    m_controlLoop.cancel();
	    synchronized (this) {
	      m_controlLoop = null;
	    }
	  super.free();
	  }
 public void runCalibration() {
	 m_log.open("Calibration" + m_file, "Timestamp, Distance, Velocity" + "\n");
	 synchronized(this) {
		 m_pidOutput.pidWrite(1.0);
     }
	 double currentCal = Timer.getFPGATimestamp();
	 double currentDist = m_pidInput.pidGet();
	 double currentV = (currentDist-m_lastDist)/(currentCal-m_lastCal);
	 m_log.write(currentCal-m_calStart + ", " + currentDist + ", " + currentV + "\n");
	 if(currentV <= (m_lastV*1.01)) {
		 m_count +=1;
		 if(m_count >= 10) {
			 stopCalibration();
		 }
	 }else {
		 m_count = 0;
	 }
	 m_lastDist = currentDist;
	 m_lastCal = currentCal;
	 m_lastV = currentV;
 }
 public void startCalibration() {
	 m_calibrating = true;
	 m_calStart = Timer.getFPGATimestamp();
	 
 }
 public void stopCalibration() {
	 m_log.close();
	 synchronized(this) {
		 m_pidOutput.pidWrite(0.0);
		 m_calibrating = false;
	 }
 }

private class PIDTask extends TimerTask {

	    private SnazzyMotionPlanner m_controller;

	    public PIDTask(SnazzyMotionPlanner snazzyMotionPlanner) {
	      if (snazzyMotionPlanner == null) {
	        throw new NullPointerException("Given MotionPlanner was null");
	      }
	      m_controller = snazzyMotionPlanner;
	    }

	    @Override
	    public void run() {
	    	if(isEnabled()) {
		    	if(m_calibrating) {
		    		runCalibration();
		    	}else {
		    		//TODO Implement Motion Planning
		    	}
		    	
	    	}
	    }
	  }
}
