package frc.robot.bodges.rawrlib.generics;

import java.util.Optional;
import java.util.function.Function;

import edu.wpi.first.units.*;
import frc.robot.util.Util;

/**
 * i'm going to hell for sure
 */
@SuppressWarnings({"unchecked"})
public class DimensionalPIDFGains<StateDimension extends Unit<StateDimension>, InputDimension extends Unit<InputDimension>> {
  protected Measure<Per<InputDimension, StateDimension>> kP = (Measure<Per<InputDimension, StateDimension>>) Util.anyZero(); // proportional gain
  protected Measure<Per<InputDimension, Mult<StateDimension, Time>>> kI = (Measure<Per<InputDimension, Mult<StateDimension, Time>>>) Util.anyZero(); // integral gain
  protected Measure<Per<InputDimension, Velocity<StateDimension>>> kD = (Measure<Per<InputDimension, Velocity<StateDimension>>>) Util.anyZero(); // derivative gain
  protected Measure<InputDimension> kS = (Measure<InputDimension>) Util.anyZero(); // static feedforward
  protected Function<Measure<StateDimension>, Measure<InputDimension>> kF = (unused) -> (Measure<InputDimension>)Util.anyZero(); // ff per setpoint (use for velocity control or arm position)
  protected Measure<InputDimension> kG = (Measure<InputDimension>) Util.anyZero(); // gravity feedforward
  // the following need to be set or the thing just won't move from zero
  protected Optional<Measure<StateDimension>> kMinRef = Optional.of(null); // minimum reference value
  protected Optional<Measure<StateDimension>> kMaxRef = Optional.of(null); // maximum reference value
  protected Optional<Measure<Velocity<StateDimension>>> kMinVel = Optional.of(null); // minimum reference derivative
  protected Optional<Measure<Velocity<StateDimension>>> kMaxVel = Optional.of(null); // maximum reference derivative
  protected Optional<Measure<Velocity<Velocity<StateDimension>>>> kMinAccel = Optional.of(null); // minimum reference second derivative
  protected Optional<Measure<Velocity<Velocity<StateDimension>>>> kMaxAccel = Optional.of(null); // maximum reference second derivative

  public DimensionalPIDFGains<StateDimension, InputDimension> setP(Measure<Per<InputDimension, StateDimension>> kP) { this.kP = kP; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setI(Measure<Per<InputDimension, Mult<StateDimension, Time>>> kI) { this.kI = kI; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setD(Measure<Per<InputDimension, Velocity<StateDimension>>> kD) { this.kD = kD; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setS(Measure<InputDimension> kS) { this.kS = kS; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setF(Function<Measure<StateDimension>, Measure<InputDimension>> kF) { this.kF = kF; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setG(Measure<InputDimension> kG) { this.kG = kG; return this; }

  public DimensionalPIDFGains<StateDimension, InputDimension> setMinRef(Measure<StateDimension> kMinRef) { this.kMinRef = Optional.of(kMinRef); return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setMaxRef(Measure<StateDimension> kMaxRef) { this.kMaxRef = Optional.of(kMaxRef); return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setMinVel(Measure<Velocity<StateDimension>> kMinVel) { this.kMinVel = Optional.of(kMinVel); return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setMaxVel(Measure<Velocity<StateDimension>> kMaxVel) { this.kMaxVel = Optional.of(kMaxVel); return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setMinAccel(Measure<Velocity<Velocity<StateDimension>>> kMinAccel) { this.kMinAccel = Optional.of(kMinAccel); return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setMaxAccel(Measure<Velocity<Velocity<StateDimension>>> kMaxAccel) { this.kMaxAccel = Optional.of(kMaxAccel); return this; }

  public Measure<Per<InputDimension, StateDimension>> getP() { return kP; }
  public Measure<Per<InputDimension, Mult<StateDimension, Time>>> getI() { return kI; }
  public Measure<Per<InputDimension, Velocity<StateDimension>>> getD() { return kD; }
  public Measure<InputDimension> getS() { return kS; }
  public Measure<InputDimension> getF(Measure<StateDimension> x) { return kF.apply(x); }
  public Measure<InputDimension> getG() { return kG; }

  public Optional<Measure<StateDimension>> getMinRef() { return kMinRef; }
  public Optional<Measure<StateDimension>> getMaxRef() { return kMaxRef; }
  public Optional<Measure<Velocity<StateDimension>>> getMinVel() { return kMinVel; }
  public Optional<Measure<Velocity<StateDimension>>> getMaxVel() { return kMaxVel; }
  public Optional<Measure<Velocity<Velocity<StateDimension>>>> getMinAccel() { return kMinAccel; }
  public Optional<Measure<Velocity<Velocity<StateDimension>>>> getMaxAccel() { return kMaxAccel; }

  public DimensionalPIDFGains<StateDimension, InputDimension> setF(Measure<Per<InputDimension, StateDimension>> factor) {
    setF(x -> { return (Measure<InputDimension>) factor.times(x); });
    return this;
  }
}
