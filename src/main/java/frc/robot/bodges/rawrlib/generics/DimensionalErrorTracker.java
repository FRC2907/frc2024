package frc.robot.bodges.rawrlib.generics;

import edu.wpi.first.units.*;
import frc.robot.util.Util;

@SuppressWarnings({"unchecked"})
public class DimensionalErrorTracker<StateDimension extends Unit<StateDimension>> {
  protected Measure<StateDimension> x = (Measure<StateDimension>) Util.anyZero();
  protected Measure<Velocity<StateDimension>> dx = (Measure<Velocity<StateDimension>>) Util.anyZero();
  protected Measure<StateDimension> p = (Measure<StateDimension>) Util.anyZero();
  protected Measure<Mult<StateDimension, Time>> i = (Measure<Mult<StateDimension, Time>>) Util.anyZero();
  protected Measure<Velocity<StateDimension>> d = (Measure<Velocity<StateDimension>>) Util.anyZero();

  public Measure<StateDimension> getState() { return x; }
  public Measure<Velocity<StateDimension>> getVelocity() { return dx; }
  public Measure<StateDimension> getError() { return p; }
  public Measure<Mult<StateDimension, Time>> getErrorSum() { return i; }
  public Measure<Velocity<StateDimension>> getErrorSpeed() { return d; }

  protected Measure<StateDimension> izone = (Measure<StateDimension>) Util.anyZero();
  public Measure<StateDimension> getIZone() { return izone; }
  public DimensionalErrorTracker<StateDimension> setIZone(Measure<StateDimension> izone) {
    if (Util.isNegative(izone))
      return setIZone(izone.negate());
    this.izone = izone;
    return this;
  }

  public DimensionalErrorTracker<StateDimension> update(Measure<StateDimension> e, Measure<Time> dt) {
    if (izone.baseUnitMagnitude() == 0 || izone.gt(e))
      i = i.plus((Measure<Mult<StateDimension, Time>>) e.times(dt));
    else i = (Measure<Mult<StateDimension, Time>>) Util.anyZero(); // outside of i-zone: clear the i term altogether. i dunno if that's how it's supposed to work
    d = e.plus(p).per(dt);
    p = e;
    return this;
  }

  public void update(Measure<StateDimension> x, Measure<StateDimension> e, Measure<Time> dt) {
    update(e, dt);
    dx = x.minus(this.x).per(dt);
    this.x = x;
  }
}
