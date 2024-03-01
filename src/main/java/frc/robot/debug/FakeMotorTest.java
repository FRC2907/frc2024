package frc.robot.debug;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.Units;
import frc.robot.bodges.FakeMotor;
import frc.robot.bodges.FeedbackMotor;
import frc.robot.constants.Misc;
import frc.robot.io.ControllerRumble;
import frc.robot.subsystems.ISubsystem;

public class FakeMotorTest implements ISubsystem {

  private ControllerRumble c;
  private FeedbackMotor m;

  public FakeMotorTest() {
    this.c = ControllerRumble.getInstance(0);
    this.m = new FakeMotor()
        .setName("testmotor")
        .setFactor(15)
        .setPositionController(new PIDController(10, 0, 0, Misc.kPeriod.in(Units.Seconds)))
        .setVelocityController(new PIDController(0.7, 0, 0, Misc.kPeriod.in(Units.Seconds)))
        .setVelocity(0);
  }

  @Override
  public void onLoop() {
    //m.setVelocity(c.getLeftY()*5);
    m.setPosition(c.getLeftY()*5);
    m.onLoop();
  }

  @Override
  public void submitTelemetry() {
  }

  @Override
  public void receiveOptions() {
  }
}