/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.robot.subsystems.*;
import frc.robotMap.inputs.CoprocessorMap;

/**
 * Declare and initialize all systems here, and call all systems from here. The
 * use of this class prevents 'Robots don't quit' error by implementing
 * singletons for every system. Call getInstance() methods when initializing.
 * 
 * @author Robert Smith
 *
 */
public class Adaptor {
	private static Adaptor adaptor;
	
	public DriverStation ds;
	
	public PowerDistributionPanel pdp;
	
	public Compressor comp;
	
	public AHRS navx;
	
	public DriveTrain driveTrain;

	public Shifters shifters;

	public Serial serial;

	public UsbCamera cam;
	
	private Adaptor(){
		ds = DriverStation.getInstance();

		pdp = new PowerDistributionPanel();
		
		comp = new Compressor();
		
		navx = new AHRS(CoprocessorMap.NAVX_PORT);
		
		driveTrain = DriveTrain.getInstance();

		shifters = Shifters.getInstance();

		serial = Serial.getInstance();

		cam = CameraServer.getInstance().startAutomaticCapture();
	}
	
	public static Adaptor getInstance(){
		if(adaptor == null)	adaptor = new Adaptor(); 
		return adaptor;
	}
}
