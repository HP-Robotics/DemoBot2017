package org.usfirst.frc.team2823.robot;

//import edu.wpi.cscore.UsbCamera;
//import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Talon tLeft;
	Talon tRight;
	Spark sLeft;
	Spark sRight;
	//Relay light;
	
	//NetworkTable table;
	
	boolean lastButton1 = false;
	boolean lastLight = false;
	
	boolean toggle = false;
	
	boolean going = false;
	boolean on = false;
	
	double[] defaultValues;
	double[] areas;
	double[] centerX;
	
	final double ENCODER_RESOLUTION = 2048;
	final double DRIVE_RATIO = 1.432 / 3.826;
	final double FUDGE_FACTOR = 194.0 / 196.0;
	final double WHEEL_RADIUS = 5.875 * FUDGE_FACTOR;
	
	final double ENC_TO_IN = 2 * Math.PI * WHEEL_RADIUS * DRIVE_RATIO / ENCODER_RESOLUTION;
	final double IN_TO_ENC = ENCODER_RESOLUTION / (2 * Math.PI * WHEEL_RADIUS * DRIVE_RATIO);
	
	Joystick joystick;
	
	AdvancedPIDController dControl;
	AdvancedPIDController rControl; 
	AdvancedPIDController leftControl;
	AdvancedPIDController rightControl;
	
	RotationPIDOutput rOutput;
	DrivePIDOutput dOutput;
	
	AverageEncoderSource daPotatoSource;
	Encoder l;
	Encoder r;
	
	ADXRS450_Gyro daGSource;
	
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		tRight = new Talon(0);
		sRight = new Spark(1);
		tLeft = new Talon(2);
		//light = new Relay(0,Relay.Direction.kForward);
		
		l = new Encoder(2, 3);
		r = new Encoder(0, 1);
		
		sLeft = new Spark(3);
		
		joystick = new Joystick(0);
		
		rOutput = new RotationPIDOutput(this);
		dOutput = new DrivePIDOutput(this);
		
		daPotatoSource = new AverageEncoderSource(this, l, r);
		daGSource = new ADXRS450_Gyro();
		
		dControl = new AdvancedPIDController(0, 0, 0, daPotatoSource, dOutput, 0.01);
		rControl = new AdvancedPIDController(0, 0, 0, daGSource, rOutput, 0.01);
		leftControl = new AdvancedPIDController(1, 0, 0, l, tLeft, 0.01);
		rightControl = new AdvancedPIDController(1, 0, 0, r, new InvertedPIDOutput(tRight), 0.01);
		
		//table = NetworkTable.getTable("GRIP/myContoursReport");
		
		defaultValues = new double[0];
		
		
		SmartDashboard.putNumber("P", 0.01);
		SmartDashboard.putNumber("I", 0.0);
		SmartDashboard.putNumber("D", 0.0);
		SmartDashboard.putNumber("Setpoint", 0);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		tLeft.set(-joystick.getRawAxis(1));
		sLeft.set(-joystick.getRawAxis(1));
		tRight.set(joystick.getRawAxis(3));
		sRight.set(joystick.getRawAxis(3));
		
		/*System.out.println("right: "+ r.get() + ", left: " + l.get());
		getTableValues();
		if(centerX.length == areas.length && areas.length > 0 ){
			double angle = getAngle();
			
			if(joystick.getRawButton(1) & (joystick.getRawButton(1) != lastButton1)){
				going = !going;
				if(going){
					l.reset();
					r.reset();
					leftControl.setSetpoint((96.0*(angle/360.0)) * IN_TO_ENC);
					leftControl.enableLog("LeftControl.csv");
					leftControl.enable();
					rightControl.setSetpoint((-96.0*(angle/360.0))* IN_TO_ENC);
					rightControl.enableLog("RightControl.csv");
					rightControl.enable();
					
					System.out.println("Start");
				} else {
					leftControl.closeLog();
					leftControl.disable();
					rightControl.closeLog();
					rightControl.disable();
					
					System.out.println("Stop");
				}
			}
			//System.out.println(angle);
		}
		
		
		
		if(!going){
			tLeft.set(-joystick.getRawAxis(1));
			sLeft.set(-joystick.getRawAxis(1));
		
			tRight.set(joystick.getRawAxis(3));
			sRight.set(joystick.getRawAxis(3));
		}
		
		/*if(joystick.getRawButton(1) & (joystick.getRawButton(1) != lastButton1)){
			going = !going;
			if(going){
				daGSource.reset();
				rControl.setSetpoint(SmartDashboard.getNumber("Setpoint", 0));
				rControl.enable();
			} else {
				rControl.disable();
			}
			
		}*/
		
		
			
		/*if(joystick.getRawButton(3) & (joystick.getRawButton(3) != lastLight)){
			on = !on;
			if(on){
				light.set(Relay.Value.kOn);
			}else{
				light.set(Relay.Value.kOff);
			}
		}*/
		
		leftControl.setPID(SmartDashboard.getNumber("P", 0), SmartDashboard.getNumber("I", 0), SmartDashboard.getNumber("D", 0));
		rightControl.setPID(SmartDashboard.getNumber("P", 0), SmartDashboard.getNumber("I", 0), SmartDashboard.getNumber("D", 0));
		
		lastButton1 = joystick.getRawButton(1);
		lastLight = joystick.getRawButton(3);
		//System.out.println(l.getDistance()*ENC_TO_IN);
		//System.out.println(r.getDistance()*ENC_TO_IN);
		//System.out.println("Left Enc: " + l.getDistance() + "Right Enc: " + r.getDistance());
		//System.out.println("Gyro: " + daGSource.getAngle());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic(){
		System.out.println(joystick.getRawButton(1));
		if(joystick.getRawButton(1) && !toggle){
			leftControl.setSetpoint(1000* IN_TO_ENC);
			leftControl.enable();
			leftControl.enableLog("Left.csv");
			
			rightControl.setSetpoint(1000 * IN_TO_ENC);
			rightControl.enable();
			rightControl.enableLog("Right.csv");
			
			toggle = true;
			
		} else if(!joystick.getRawButton(1) && toggle){
			leftControl.disable();
			leftControl.closeLog();
			
			rightControl.disable();
			rightControl.closeLog();
			
			toggle = false;
			
		}
		
	}
	
	public double getAngle(){
		int maxIndex = 0;
		for (int i = 1; i < areas.length; i++){
			if(areas[i] > areas[maxIndex]){
				maxIndex = i;
			}
		}
		double center = centerX[maxIndex];
		
		double angle = (center - 320.0) * (30.0 / 320.0);
		
		System.out.println(angle);
		
		return angle;
	}
	
	public void getTableValues(){
		
		//centerX = table.getNumberArray("centerX", defaultValues);
		//areas = table.getNumberArray("area", defaultValues);
	}
}

