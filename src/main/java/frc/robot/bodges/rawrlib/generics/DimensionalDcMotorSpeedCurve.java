package frc.robot.bodges.rawrlib.generics;

import edu.wpi.first.units.*;
import frc.robot.util.Util;

public abstract class DimensionalDcMotorSpeedCurve<D extends Unit<D>> {
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

  public abstract Measure<Velocity<D>> getSpeed(Measure<Voltage> voltage);
  public abstract Measure<Voltage> getVoltage(Measure<Velocity<D>> speed);
}
