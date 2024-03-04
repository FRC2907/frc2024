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
import frc.robot.debug.LinearMotorControllerTest;
import frc.robot.subsystems.ISubsystem;
import frc.robot.subsystems.NoteTargetingPipeline;
//never used
import frc.robot.subsystems.Superduperstructure;

public class Robot extends TimedRobot {

  // Configure the loop rate for periodic methods.
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
        //Superduperstructure.getInstance()
        new LinearMotorControllerTest()
    ;

    autoChooser = new SendableChooser<>();
    for (Class<? extends Routine> routineClass : Routine.getRoutines())
      autoChooser.addOption(routineClass.getSimpleName(), routineClass);
    SmartDashboard.putData(autoChooser);

  }

  @Override
  public void robotPeriodic() {
    if (everything != null)
      everything.onLoop();
  }

  @Override
  public void autonomousInit() {
    try {
      auto = autoChooser.getSelected().getDeclaredConstructor().newInstance();
      auto.onStart();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void autonomousPeriodic() {
    if (auto != null)
      auto.onLoop();
  }
}