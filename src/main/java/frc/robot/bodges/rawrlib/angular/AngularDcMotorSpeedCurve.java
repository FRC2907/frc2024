package frc.robot.bodges.rawrlib.angular;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalDcMotorSpeedCurve;

public class AngularDcMotorSpeedCurve extends DimensionalDcMotorSpeedCurve<Angle> {
  
  public AngularDcMotorSpeedCurve(Measure<Per<Velocity<Angle>, Voltage>> lower_slope, Measure<Voltage> lower_knee,
      Measure<Voltage> upper_knee, Measure<Per<Velocity<Angle>, Voltage>> upper_slope) {
    super(lower_slope, lower_knee, upper_knee, upper_slope);
  }

  public AngularDcMotorSpeedCurve(Measure<Voltage> knee, Measure<Per<Velocity<Angle>, Voltage>> slope) {
    super(knee, slope);
  }

  public Measure<Velocity<Angle>> getSpeed(Measure<Voltage> voltage) {
    if (voltage.gt(upper_knee))
      return Units.RPM.of(upper_slope.in(Units.RPM.per(Units.Volts)) * voltage.minus(upper_knee).in(Units.Volts));
    if (voltage.lt(lower_knee))
      return Units.RPM.of(lower_slope.in(Units.RPM.per(Units.Volts)) * voltage.minus(lower_knee).in(Units.Volts));
    return Units.RPM.of(0);
  }

  public Measure<Voltage> getVoltage(Measure<Velocity<Angle>> speed) {
    if (speed.gt(Units.RPM.of(0)))
      return upper_knee.plus(Units.Volts.of(speed.in(Units.RPM) / upper_slope.in(Units.RPM.per(Units.Volts))));
    if (speed.lt(Units.RPM.of(0)))
      return lower_knee.plus(Units.Volts.of(speed.in(Units.RPM) / lower_slope.in(Units.RPM.per(Units.Volts))));
    return Units.Volts.of(0);
  }
}
