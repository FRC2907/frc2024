// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.constants.MechanismConstraints;
import frc.robot.subsystems.ISubsystem;
import frc.robot.subsystems.NoteTargetingPipeline;
import frc.robot.subsystems.Superduperstructure;

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

  /**
   * Configure the loop rate for periodic methods.
   */
  public Robot() {
    super(MechanismConstraints.kPeriod.in(Units.Seconds));
  }

  private SendableChooser<Class<? extends Routine>> autoChooser;
  private Routine auto;

  private ISubsystem everything;

  @Override
  public void robotInit() {

    if (MechanismConstraints.camera.kEnabled) {
      Thread noteTargetingThread = new Thread(new NoteTargetingPipeline());
      noteTargetingThread.setDaemon(true);
      noteTargetingThread.start();
    }

    this.everything = 
        Superduperstructure.getInstance()
        //new LinearMotorControllerTest()
    ;

    autoChooser = new SendableChooser<>();
    for (Class<? extends Routine> routineClass : Routine.getRoutines())
      autoChooser.addOption(routineClass.getSimpleName(), routineClass);
    SmartDashboard.putData(autoChooser);

  }

  @Override
  public void robotPeriodic() {
    everything.onLoop();
  }

  @Override
  public void autonomousInit() {
    try {
    auto = autoChooser.getSelected().getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
    auto.onStart();
  }

  @Override
  public void autonomousPeriodic() {
    auto.onLoop();
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
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