package frc.robot.bodges.rawrlib.motors;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.Velocity;

public class WrappedFakeMotor extends WrappedMotorController {
  public FakeMotor m;

  public WrappedFakeMotor() {
    this(new FakeMotor());
  }

  public WrappedFakeMotor(FakeMotor motor) {
    this.m = motor;
    setMotor(motor);
  }

  @Override
  public Measure<Angle> getPosition_downstream() {
    return Units.Rotations.of(m.getPosition());
  }

  @Override
  public Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.RotationsPerSecond.of(m.getVelocity());
  }

  @Override
  public void onLoop() {
    m.onLoop();
  }

}
