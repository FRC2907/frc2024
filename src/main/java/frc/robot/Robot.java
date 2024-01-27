// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {

  private PS4Controller driver, operator;
  private Drivetrain drivetrain;
  private Arm arm;
  private Intake intake;
  private Shooter shooter;

  @Override
  public void robotInit() {
    driver = new PS4Controller(Ports.HID.DRIVER);
    operator = new PS4Controller(Ports.HID.OPERATOR);
    drivetrain = Drivetrain.getInstance();
    arm = Arm.getInstance();
    intake = Intake.getInstance();
    shooter = Shooter.getInstance();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}
  
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    drivetrain.curvatureDrive(driver.getLeftY(), driver.getLeftX(), false);
    if (operator.getCircleButtonPressed())
    arm.setSetPoint(Control.arm.kFloorPosition);
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
