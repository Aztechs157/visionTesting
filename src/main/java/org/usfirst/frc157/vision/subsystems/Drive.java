/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc157.vision.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import org.usfirst.frc157.vision.PID;
import org.usfirst.frc157.vision.RobotMap;
import org.usfirst.frc157.vision.commands.driveController;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
/**
 * Add your docs here.
 */
public class Drive extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private CANSparkMax driveRight1 = RobotMap.driveRight1;
  private CANSparkMax driveRight2 = RobotMap.driveRight2;
  private CANSparkMax driveleft1 = RobotMap.driveLeft1;
  private CANSparkMax driveleft2 = RobotMap.driveLeft2;
  public PID turnPID = new PID(0.002, 0, 0.0000003, 9999999, 9999999, 9999999, 999999);
  public PID drivePID = new PID(0.002, 0, 0.0000003, 9999999, 9999999, 9999999, 999999);
  //public MecanumDrive driveSystem = new MecanumDrive(driveleft1, driveleft2, driveRight1, driveRight2);

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new driveController());
  }
  public void mechDrive(double speed, double turn, double strafe) {
    double frontLeft = -speed + turn - strafe;
    double backLeft = -speed + turn + strafe; 
    double frontRight = speed + turn - strafe; 
    double backRight = speed + turn + strafe; 
    driveRight1.set(frontRight);
    driveRight2.set(backRight);
    driveleft1.set(frontLeft);
    driveleft2.set(backLeft);
  }
  public void tankDrive(double speed, double turn)
  {
    double right = -speed - turn;
    double left = speed - turn;
    driveRight1.set(right);
    driveRight2.set(right);
    driveleft1.set(left);
    driveleft2.set(left);
  }

}
