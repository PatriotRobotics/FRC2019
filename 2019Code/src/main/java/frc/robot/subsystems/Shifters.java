/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotStates.DriveGear;
import frc.robot.RobotStates.ArmLevel;
import frc.robotMap.outputs.SolenoidMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Subsystem to track drive gear and change it.
 */
public class Shifters extends Subsystem {
  private static Shifters shifters;

  private DoubleSolenoid pancakes;

  public DriveGear gear;
  public boolean state;

  public ArmLevel position;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public Shifters() {
    pancakes = new DoubleSolenoid(SolenoidMap.SHIFTER_A, SolenoidMap.SHIFTER_B);
  }

  public static Shifters getInstance() {
    if(shifters == null) shifters = new Shifters();
    return shifters;
  }

  public void shiftHigh() {
    pancakes.set(DoubleSolenoid.Value.kForward);
    gear = DriveGear.HIGH;
  }

  public void shiftLow() {
    pancakes.set(DoubleSolenoid.Value.kReverse);
    gear = DriveGear.LOW;
  }


  @Override
  public void initDefaultCommand() {
  }
}
