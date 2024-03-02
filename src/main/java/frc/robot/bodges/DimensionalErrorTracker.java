package frc.robot.bodges;

import java.lang.reflect.Type;

import edu.wpi.first.units.*;
import frc.robot.util.Util;

public class DimensionalErrorTracker<StateDimension extends Unit<StateDimension>> {
  protected Measure<StateDimension> p;
  protected MutableMeasure<Mult<StateDimension, Time>> i;
  protected Measure<Velocity<StateDimension>> d;

  public Measure<StateDimension> getError() { return p; }
  public Measure<Mult<StateDimension, Time>> getErrorSum() { return i; }
  public Measure<Velocity<StateDimension>> getErrorSpeed() { return d; }

  protected Measure<StateDimension> izone;
  public Measure<StateDimension> getIZone() { return izone; }
  public DimensionalErrorTracker<StateDimension> setIZone(Measure<StateDimension> izone) {
    this.izone = izone;
    return this;
  }

  @SuppressWarnings({"unchecked"})
  public DimensionalErrorTracker<StateDimension> update(Measure<StateDimension> e, Measure<Time> dt) {
    if (izone == null || izone.baseUnitMagnitude() == 0 || izone.gt(e)) {
      i = Util.initializeOrAdd(i, (Measure<Mult<StateDimension, Time>>)e.times(dt)).mutableCopy();
    }
    else i.mut_setMagnitude(0); // outside of i-zone: clear the i term altogether. i dunno if that's how it's supposed to work
    d = e.plus(p).per(dt);
    p = e;
    return this;
  }

  private DimensionalErrorTracker() {}

  // FIXME just break this stuff out
  public static DimensionalErrorTracker<Angle> createAngular() {
    return createAngular(Units.Degrees.zero());
  }

  public static DimensionalErrorTracker<Angle> createAngular(Measure<Angle> izone) {
    DimensionalErrorTracker<Angle> out = new DimensionalErrorTracker<>();
    out.p = Units.Degrees.zero();
    out.i = Units.Degrees.mult(Units.Seconds).zero().mutableCopy();
    out.d = Units.DegreesPerSecond.zero();
    out.izone = izone;
    return out;
  }

  public static DimensionalErrorTracker<Velocity<Angle>> createAngularVelocity(Measure<Velocity<Angle>> izone) {
    DimensionalErrorTracker<Velocity<Angle>> out = new DimensionalErrorTracker<>();
    out.p = Units.DegreesPerSecond.zero();
    out.i = Units.DegreesPerSecond.mult(Units.Seconds).zero().mutableCopy();
    out.d = Units.DegreesPerSecond.per(Units.Second).zero();
    out.izone = izone;
    return out;
  }

  public static DimensionalErrorTracker<Distance> createLinear() {
    return createLinear(Units.Meters.zero());
  }

  public static DimensionalErrorTracker<Distance> createLinear(Measure<Distance> izone) {
    DimensionalErrorTracker<Distance> out = new DimensionalErrorTracker<>();
    out.p = Units.Meters.zero();
    out.i = Units.Meters.mult(Units.Seconds).zero().mutableCopy();
    out.d = Units.MetersPerSecond.zero();
    out.izone = izone;
    return out;
  }

  public static DimensionalErrorTracker<Velocity<Distance>> createLinearVelocity(Measure<Velocity<Distance>> izone) {
    DimensionalErrorTracker<Velocity<Distance>> out = new DimensionalErrorTracker<>();
    out.p = Units.MetersPerSecond.zero();
    out.i = Units.MetersPerSecond.mult(Units.Seconds).zero().mutableCopy();
    out.d = Units.MetersPerSecondPerSecond.zero();
    out.izone = izone;
    return out;
  }

  public static DimensionalErrorTracker<?> create(Type t) {
    switch (t.getTypeName()) {
      case "Angle": return createAngular();
      case "Distance": return createLinear();
      default: throw new IllegalArgumentException();
    }
  }
}
