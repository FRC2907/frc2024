// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.auto.routines.SampleRoutine;
import frc.robot.auto.routines.templates.Routine;
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

  private Superduperstructure superduperstructure;
  private Routine auto;
  private Thread noteTargetingThread;

  @Override
  public void robotInit() {
    noteTargetingThread = new Thread(
      new NoteTargetingPipeline(
        Control.camera.WIDTH
        , Control.camera.HEIGHT
        , Control.camera.kOrangeLow
        , Control.camera.kOrangeHigh
        )
    );
    noteTargetingThread.setDaemon(true);
    noteTargetingThread.start();
    superduperstructure = Superduperstructure.getInstance();
  }

  @Override
  public void robotPeriodic() {
    superduperstructure.onLoop();
  }

  @Override
  public void autonomousInit() {
    // TODO implement NT autonomous chooser
    // TODO also write auto routines
    auto = new SampleRoutine();
  }

  @Override
  public void autonomousPeriodic() {
    auto.onLoop();
  }

  @Override
  public void teleopInit() {
    superduperstructure.neutralPosition();
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
