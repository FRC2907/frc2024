package frc.robot.debug;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.Units;
import frc.robot.bodges.FakeMotor;
import frc.robot.bodges.FeedbackMotor;
import frc.robot.bodges.RampingProfile;
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
        .setVelocityController(new PIDController(0.8, 0, 10, Misc.kPeriod.in(Units.Seconds)))
        .setVelocityFF(0.8)
        .setVelocity(0);
  }

  @Override
  public void onLoop() {
    m.setVelocity(c.getLeftY()*5);
    //m.setPosition(c.getLeftY()*5);
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
