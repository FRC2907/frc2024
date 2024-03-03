package frc.robot.bodges.rawrlib.generics;

import edu.wpi.first.units.*;
import frc.robot.util.Util;

public class DimensionalDcMotorSpeedCurve<D extends Unit<D>> {
  protected final Measure<Per<Velocity<D>, Voltage>> lower_slope, upper_slope;
  protected final Measure<Voltage> lower_knee, upper_knee;

  public DimensionalDcMotorSpeedCurve(
      Measure<Per<Velocity<D>, Voltage>> lower_slope,
      Measure<Voltage> lower_knee,
      Measure<Voltage> upper_knee,
      Measure<Per<Velocity<D>, Voltage>> upper_slope) {
    this.lower_slope = lower_slope;
    this.lower_knee = lower_knee;
    this.upper_knee = upper_knee;
    this.upper_slope = upper_slope;
  }

  public DimensionalDcMotorSpeedCurve(Measure<Voltage> knee, Measure<Per<Velocity<D>, Voltage>> slope) {
    this(slope, knee.negate(), knee, slope);
  }

  @SuppressWarnings("unchecked")
  public Measure<Velocity<D>> getSpeed(Measure<Voltage> voltage) {
    if (voltage.gt(upper_knee))
      return (Measure<Velocity<D>>) upper_slope.times(voltage.minus(upper_knee));
    if (voltage.lt(lower_knee))
      return (Measure<Velocity<D>>) lower_slope.times(voltage.minus(lower_knee));
    return (Measure<Velocity<D>>) Util.anyZero();
  }

  public Measure<Voltage> getVoltage(Measure<Velocity<D>> speed) {
    // this could be wrong and bad! idk!
    if (Util.isPositive(speed))
      return upper_knee.plus(Units.Volts.of(speed.baseUnitMagnitude() / upper_slope.baseUnitMagnitude()));
    if (Util.isNegative(speed))
      return lower_knee.plus(Units.Volts.of(speed.baseUnitMagnitude() / lower_slope.baseUnitMagnitude()));
    return Units.Volts.zero();
  }
}
