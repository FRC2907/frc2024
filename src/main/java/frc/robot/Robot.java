// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.auto.routines.SampleRoutine;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.constants.Ports;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Superstructure;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {

  private PS4Controller driver, operator;
  private Drivetrain drivetrain;
  private Superstructure superstructure;
  private Routine auto;

  @Override
  public void robotInit() {
    driver = new PS4Controller(Ports.HID.DRIVER);
    operator = new PS4Controller(Ports.HID.OPERATOR);
    drivetrain = Drivetrain.getInstance();
    superstructure = Superstructure.getInstance();
  }

  @Override
  public void robotPeriodic() {
    superstructure.onLoop();
  }

  @Override
  public void autonomousInit() {
    auto = new SampleRoutine();
  }

  @Override
  public void autonomousPeriodic() {
    auto.onLoop();
  }

  @Override
  public void teleopInit() {
    superstructure.neutralPosition();
  }

  @Override
  public void teleopPeriodic() {
    drivetrain.curvatureDrive(driver.getLeftY(), driver.getRightX());
    if (operator.getCircleButtonPressed() || driver.getCircleButtonPressed()) {
      superstructure.cancelAction(); 
    }
    if (operator.getCrossButtonPressed()) {
      superstructure.prepareForClimb();
    }
    if (operator.getSquareButtonPressed()) {
      superstructure.moveToSpeaker(); // TODO change to both speaker and amp
    }
    if (operator.getTriangleButtonPressed()) {
      superstructure.neutralPosition();
    }
    if (operator.getR2Button()) {
      superstructure.moveToSpeaker(); // TODO confirm speaker shot
    }
    if (operator.getR1Button()) {
      superstructure.moveToAmp(); // TODO confirm amp shot
    }
    if (operator.getL2Button()) {
      superstructure.moveToSpeaker(); // TODO gavin rawr
    }
    if (operator.getL1Button()) {
      superstructure.moveToIntaking();
    }
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
