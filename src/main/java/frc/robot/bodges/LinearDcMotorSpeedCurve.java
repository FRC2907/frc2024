package frc.robot.bodges;

import edu.wpi.first.units.*;

public class LinearDcMotorSpeedCurve extends DimensionalDcMotorSpeedCurve<Distance> {
  
  public LinearDcMotorSpeedCurve(Measure<Per<Velocity<Distance>, Voltage>> lower_slope, Measure<Voltage> lower_knee,
      Measure<Voltage> upper_knee, Measure<Per<Velocity<Distance>, Voltage>> upper_slope) {
    super(lower_slope, lower_knee, upper_knee, upper_slope);
  }

  public LinearDcMotorSpeedCurve(Measure<Voltage> knee, Measure<Per<Velocity<Distance>, Voltage>> slope) {
    super(knee, slope);
  }

  public Measure<Velocity<Distance>> getSpeed(Measure<Voltage> voltage) {
    if (voltage.gt(upper_knee))
      return Units.MetersPerSecond.of(upper_slope.in(Units.MetersPerSecond.per(Units.Volts)) * voltage.minus(upper_knee).in(Units.Volts));
    if (voltage.lt(lower_knee))
      return Units.MetersPerSecond.of(lower_slope.in(Units.MetersPerSecond.per(Units.Volts)) * voltage.minus(lower_knee).in(Units.Volts));
    return Units.MetersPerSecond.of(0);
  }

  public Measure<Voltage> getVoltage(Measure<Velocity<Distance>> speed) {
    if (speed.gt(Units.MetersPerSecond.of(0)))
      return upper_knee.plus(Units.Volts.of(speed.in(Units.MetersPerSecond) / upper_slope.in(Units.MetersPerSecond.per(Units.Volts))));
    if (speed.lt(Units.MetersPerSecond.of(0)))
      return lower_knee.plus(Units.Volts.of(speed.in(Units.MetersPerSecond) / lower_slope.in(Units.MetersPerSecond.per(Units.Volts))));
    return Units.Volts.of(0);
  }
}
