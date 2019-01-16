/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc157.vision.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc157.vision.PID;
import org.usfirst.frc157.vision.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
/**
 * Add your docs here.
 */
public class Drive extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private TalonSRX driveRight1 = RobotMap.driveRight1;
  private TalonSRX driveRight2 = RobotMap.driveRight2;
  private TalonSRX driveleft1 = RobotMap.driveleft1;
  private TalonSRX driveleft2 = RobotMap.driveleft2;
  public PID turnPid = new PID(0.005, 0, 0.0000003, 9999999, 9999999, 9999999, 999999);
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void tankDrive(double speed, double turn)
  {
    double right = speed + turn;
    double left = speed - turn;
    driveRight1.set(ControlMode.PercentOutput, right);
    driveRight2.set(ControlMode.PercentOutput, right);
    driveleft1.set(ControlMode.PercentOutput, left);
    driveleft2.set(ControlMode.PercentOutput, left);
  }
}
