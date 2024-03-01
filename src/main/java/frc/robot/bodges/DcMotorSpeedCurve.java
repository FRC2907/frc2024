package frc.robot.bodges;

public class DcMotorSpeedCurve {
  // we assume (0,0) always
  private final double lower_slope, lower_knee, middle_slope, upper_knee, upper_slope;

  public DcMotorSpeedCurve(double lower_slope, double lower_knee, double middle_slope, double upper_knee,
      double upper_slope) {
    this.lower_slope = lower_slope;
    this.lower_knee = lower_knee;
    this.middle_slope = middle_slope;
    this.upper_knee = upper_knee;
    this.upper_slope = upper_slope;
  }

  public DcMotorSpeedCurve(double lower_slope, double lower_knee, double upper_knee, double upper_slope) {
    this(lower_slope, lower_knee, 0, upper_knee, upper_slope);
  }

  public DcMotorSpeedCurve(double knee, double slope) {
    this(slope, knee, knee, slope);
  }

  public double getSpeed(double voltage) {
    if (upper_knee < voltage)
      return getSpeed(upper_knee) + (voltage - upper_knee) * upper_slope;
    if (voltage < lower_knee)
      return getSpeed(lower_knee) + (voltage - lower_knee) * lower_slope;
    return voltage * middle_slope;
  }

  public double getVoltage(double speed) {
    if (0 < speed) return upper_knee + (speed/upper_slope);
    if (speed < 0) return lower_knee + (speed/lower_slope);
    return 0;
  }
}
