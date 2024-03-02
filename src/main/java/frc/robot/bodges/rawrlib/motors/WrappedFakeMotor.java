package frc.robot.bodges.rawrlib.motors;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.Velocity;

public class WrappedFakeMotor extends WrappedMotorController {

  public WrappedFakeMotor() {
    setMotor(new FakeMotor());
  }

  @Override
  public Measure<Angle> getPosition_downstream() {
    return Units.Degrees.zero();
  }

  @Override
  public Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.DegreesPerSecond.zero();
  }
  
}
