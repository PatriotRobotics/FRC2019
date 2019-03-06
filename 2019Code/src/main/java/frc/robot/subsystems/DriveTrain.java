/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Log;
import frc.robot.Robot;
import frc.robot.commands.drive.Halt;
import frc.robot.triggers.IsBrowningOut;
import frc.robotMap.AutoMap;
import frc.robotMap.inputs.EncoderMap;
import frc.robotMap.outputs.MotorControllerMap;

/**
 * Subsystem to control all functions of drive train.
 */
public class DriveTrain extends Subsystem {
  private static DriveTrain driveTrain;
	
	/**Put all ports for drive train motors here, front, back, middle for each side.
	 * The first port given will act as a 'master' to the remaining motors.
	 * To configure for 4 motor drive, simply exclude a 'middle' port. */
	private static int[] DTL_IDs = { //Drive Train Left
			MotorControllerMap.DTL_FRONT,
			MotorControllerMap.DTL_BACK,
			MotorControllerMap.DTL_MIDDLE
	};
	private static boolean[] DTL_INVs = { //Drive Train Left
			MotorControllerMap.DTL_FRONT_INV,
			MotorControllerMap.DTL_BACK_INV,
			MotorControllerMap.DTL_MIDDLE_INV
	};
	
	private static final int[] DTR_IDs = { //Drive Train Right
			MotorControllerMap.DTR_FRONT, 
			MotorControllerMap.DTR_BACK,
			MotorControllerMap.DTR_MIDDLE
	};
	private static boolean[] DTR_INVs = { //Drive Train Right
			MotorControllerMap.DTR_FRONT_INV,
			MotorControllerMap.DTR_BACK_INV,
			MotorControllerMap.DTR_MIDDLE_INV
	};
	
	private CANSparkMax leftMaster, leftSlave, leftSlaveB;
	
	private CANSparkMax rightMaster, rightSlave, rightSlaveB;

	private DifferentialDrive robotDrive;
	
	private static Encoder DTLEncoder;
	private static Encoder DTREncoder;
	
	private int startOfBrownOut = 0;

    /**Initialize motors and drive encoders here*/
    public DriveTrain() {

    	//Initialize encoders
    	DTLEncoder = new Encoder(EncoderMap.DTL_A, EncoderMap.DTL_B, EncoderMap.DTL_INVERTED);
    	DTREncoder = new Encoder(EncoderMap.DTR_A, EncoderMap.DTR_B, EncoderMap.DTR_INVERTED);
    	
    	leftMaster = new CANSparkMax(DTL_IDs[0], MotorType.kBrushless);
    	leftMaster.setInverted(DTL_INVs[0]);
    	leftSlave = new CANSparkMax(DTL_IDs[1], MotorType.kBrushless);
    	leftSlave.follow(leftMaster, DTL_INVs[1]);
    	
    	rightMaster = new CANSparkMax(DTR_IDs[0], MotorType.kBrushless);
    	rightMaster.setInverted(DTR_INVs[0]);
    	rightSlave = new CANSparkMax(DTR_IDs[1], MotorType.kBrushless);
    	rightSlave.follow(rightMaster, DTR_INVs[1]);
    	
    	if(DTL_IDs.length == 3){
    		leftSlaveB = new CANSparkMax(DTL_IDs[2], MotorType.kBrushless);
    		leftSlaveB.follow(leftMaster, DTL_INVs[2]);
    	} else leftSlaveB.close();
    	if(DTR_IDs.length == 3){
    		rightSlaveB = new CANSparkMax(DTR_IDs[2], MotorType.kBrushless);
    		rightSlaveB.follow(rightMaster, DTR_INVs[2]);
		} else rightSlaveB.close();

    	robotDrive = new DifferentialDrive(leftMaster, rightMaster);
    }
    
    public static DriveTrain getInstance(){
		if(driveTrain == null) driveTrain = new DriveTrain();
    	return driveTrain;
    }

    @Override
    public void initDefaultCommand() {
		setDefaultCommand(new Halt()); 
    }
    
    /*Begin Drive methods*/
    public void arcadeDrive(Joystick joystick){
		if(IsBrowningOut.get()) {
			robotDrive.setMaxOutput(0.75);
			if(startOfBrownOut == 0) {
				Log.recoverable("Voltage", "The voltage dropped below 10V.");
				startOfBrownOut = 1;
			}
		} else {
			startOfBrownOut = 0;
			robotDrive.setMaxOutput(1);
		}
		robotDrive.arcadeDrive(-joystick.getY(), joystick.getX());
    }
    
    public void tankDrive(double left, double right){
		if(IsBrowningOut.get()) {
			robotDrive.setMaxOutput(0.75);
			if(startOfBrownOut == 0) {
				Log.recoverable("Voltage", "The voltage dropped below 10V.");
				startOfBrownOut = 1;
			}
		} else {
			startOfBrownOut = 0;
			robotDrive.setMaxOutput(1);
		}
		robotDrive.tankDrive(left, right);
	}
	
    public void driveStraight(double speed){
    	double angle = Robot.adaptor.navx.getAngle();
    	double curve = -angle * AutoMap.kP;
    	robotDrive.curvatureDrive(speed, curve, false);
	}

	public void halt() {
		robotDrive.stopMotor();
	}
    
    /*Begin Encoder methods*/
    public void reset(){
    	DTLEncoder.reset();
    	DTREncoder.reset();
    }
    
    public double getDTLCount(){
    	return DTLEncoder.getDistance();
    }
    
    public double getDTRCount(){
    	return DTREncoder.getDistance();
    }
    
    public double getDTLRate(){
    	return DTLEncoder.getRate();
    }
    
    public double getDTRRate(){
    	return DTREncoder.getRate();
	}
}
