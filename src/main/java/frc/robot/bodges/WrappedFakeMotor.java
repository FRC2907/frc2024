package frc.robot.bodges;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.Velocity;

public class WrappedFakeMotor extends WrappedMotorController {

  public WrappedFakeMotor() {
    setMotor(new FakeMotor());
  }

  @Override
  protected Measure<Angle> getPosition_downstream() {
    return Units.Degrees.zero();
  }

  @Override
  protected Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.DegreesPerSecond.zero();
  }
  
}
