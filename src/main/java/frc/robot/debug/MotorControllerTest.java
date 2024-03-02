package frc.robot.debug;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.raw.DcMotorSpeedCurve;
import frc.robot.bodges.rawrlib.raw.FeedbackMotor;
import frc.robot.bodges.rawrlib.raw.StupidTalonFX;
import frc.robot.io.ControllerRumble;
import frc.robot.subsystems.ISubsystem;

public class MotorControllerTest implements ISubsystem {
  private ControllerRumble c;
  private FeedbackMotor m;

  public MotorControllerTest() {
    //this.c = ControllerRumble.getInstance(0);
    this.m = new StupidTalonFX(1)
        .setName("testmotor")
        .setFactor(1)
        .setPositionController(new PIDController(0.5, 0, 0, 0.02))
        .setVelocityController(new PIDController(0.2, 0, 0, 0.02))
        .setSpeedCurve(new DcMotorSpeedCurve(0, 0.12)) // volts per rps
        .setVelocity(0.0);
  }

  @Override
  public void onLoop() {
    //m.setVelocity(c.getLeftY());
    m.onLoop();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
  }

  @Override
  public void receiveOptions() {
  }
}
