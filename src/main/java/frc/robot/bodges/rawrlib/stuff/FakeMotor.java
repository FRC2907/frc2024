package frc.robot.bodges.rawrlib.stuff;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.Util;

public class FakeMotor<D extends Unit<D>> extends AWheeMotor<D> {
  Measure<Angle> position = Units.Rotations.zero();
  Measure<Velocity<Angle>> velocity = Units.RotationsPerSecond.zero();
  Timer timer = new Timer();

	@Override
  @SuppressWarnings("unchecked")
	public Measure<Angle> getPosition_downstream() {
    this.position = position.plus((Measure<Angle>) velocity.times(Units.Seconds.of(timer.get())));
    return position;
	}

	@Override
	public Measure<Velocity<Angle>> getVelocity_downstream() {
    return velocity;
	}

	@Override
	public AWheeMotor<D> setPosition_downstream(Measure<Angle> reference) {
    this.position = reference;
    this.velocity = Units.RotationsPerSecond.zero();
    return this;
	}

	@Override
	public AWheeMotor<D> setVelocity_downstream(Measure<Velocity<Angle>> reference) {
    this.velocity = reference;
    timer.restart();
    return this;
	}

	@Override
  @SuppressWarnings("unchecked")
	public AWheeMotor<D> setVoltage_downstream(Measure<Voltage> voltage) {
    return setVelocity(
      ((Measure<Velocity<D>>) Util.anyZero()).unit().ofBaseUnits(
        (1.0 / getVelocityF().baseUnitMagnitude()) * voltage.in(Units.Volts)
      )
    );
	}

  @Override
  public AWheeMotor<D> setPositionOffset(Measure<D> offset) {
    this.position = outputToEncoder.pos.apply(offset);
    return this;
  }

	@Override
	public AWheeMotor<D> zero() {
    this.position = Units.Rotations.zero();
    return this;
	}
}
