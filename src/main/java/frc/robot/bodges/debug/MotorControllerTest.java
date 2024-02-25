package frc.robot.bodges.debug;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.FeedbackMotor;
import frc.robot.bodges.StupidTalonFX;
import frc.robot.subsystems.ISubsystem;

public class MotorControllerTest implements ISubsystem {
  private FeedbackMotor m;

  @SuppressWarnings({ "resource" })
  public MotorControllerTest() {
    this.m = new StupidTalonFX(1)
        .setFactor(1)
        .setPositionController(new PIDController(0, 0, 0, 0.02))
        .setVelocityController(new PIDController(0.12, 0, 0, 0.02))
        .set_velocity(1);
  }

  @Override
  public void onLoop() {
    m.onLoop();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("debug/state", m.get_velocity());
    SmartDashboard.putNumber("debug/rfrnc", m.get_reference());
    SmartDashboard.putNumber("debug/error", m.get_error());
    SmartDashboard.putNumber("debug/input", m.get_lastControlEffort());
  }

  @Override
  public void receiveOptions() {
  }
}
