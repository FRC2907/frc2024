package frc.robot.bodges;

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

  public DimensionalPIDFGains<StateDimension, InputDimension> setP(Measure<Per<InputDimension, StateDimension>> kP) { this.kP = kP; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setI(Measure<Per<InputDimension, Mult<StateDimension, Time>>> kI) { this.kI = kI; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setD(Measure<Per<InputDimension, Velocity<StateDimension>>> kD) { this.kD = kD; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setS(Measure<InputDimension> kS) { this.kS = kS; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setF(Function<Measure<StateDimension>, Measure<InputDimension>> kF) { this.kF = kF; return this; }
  public DimensionalPIDFGains<StateDimension, InputDimension> setG(Measure<InputDimension> kG) { this.kG = kG; return this; }

  public Measure<Per<InputDimension, StateDimension>> getP() { return kP; }
  public Measure<Per<InputDimension, Mult<StateDimension, Time>>> getI() { return kI; }
  public Measure<Per<InputDimension, Velocity<StateDimension>>> getD() { return kD; }
  public Measure<InputDimension> getS() { return kS; }
  public Measure<InputDimension> getF(Measure<StateDimension> x) { return kF.apply(x); }
  public Measure<InputDimension> getG() { return kG; }

  public DimensionalPIDFGains<StateDimension, InputDimension> setF(Measure<Per<InputDimension, StateDimension>> factor) {
    setF(x -> { return (Measure<InputDimension>) factor.times(x); });
    return this;
  }

  //public DimensionalPIDFGains(Measure<Per<InputDimension, StateDimension>> kP, Function<Measure<StateDimension>, Measure<InputDimension>> kF) {
  //  setP(kP);
  //  setF(kF);
  //}
  //public DimensionalPIDFGains(Measure<Per<InputDimension, StateDimension>> kP, Measure<Per<InputDimension, StateDimension>> kF) {
  //  setP(kP);
  //  setF(kF);
  //}
}
