package frc.robot.bodges.rawrlib.stuff;

import edu.wpi.first.units.*;
import frc.robot.util.Util;

@SuppressWarnings({"unchecked"})
public class GainContainer<Xdim extends Unit<Xdim>, Udim extends Unit<Udim>> {
  protected Measure<Per<Udim, Xdim>> kP = (Measure<Per<Udim, Xdim>>) Util.anyZero(); // proportional gain
  protected Measure<Per<Udim, Mult<Xdim, Time>>> kI = (Measure<Per<Udim, Mult<Xdim, Time>>>) Util.anyZero(); // integral gain
  protected Measure<Per<Udim, Velocity<Xdim>>> kD = (Measure<Per<Udim, Velocity<Xdim>>>) Util.anyZero(); // derivative gain
  protected Measure<Udim> kS = (Measure<Udim>) Util.anyZero(); // static feedforward
  protected Measure<Per<Udim, Xdim>> kF = (Measure<Per<Udim, Xdim>>) Util.anyZero(); // ff per setpoint (use for velocity control or arm position)
  protected Measure<Udim> kG = (Measure<Udim>) Util.anyZero(); // gravity feedforward

  public GainContainer<Xdim, Udim> setP(Measure<Per<Udim, Xdim>> kP) { this.kP = kP; return this; }
  public GainContainer<Xdim, Udim> setI(Measure<Per<Udim, Mult<Xdim, Time>>> kI) { this.kI = kI; return this; }
  public GainContainer<Xdim, Udim> setD(Measure<Per<Udim, Velocity<Xdim>>> kD) { this.kD = kD; return this; }
  public GainContainer<Xdim, Udim> setS(Measure<Udim> kS) { this.kS = kS; return this; }
  public GainContainer<Xdim, Udim> setF(Measure<Per<Udim, Xdim>> kF) { this.kF = kF; return this; }
  public GainContainer<Xdim, Udim> setG(Measure<Udim> kG) { this.kG = kG; return this; }

  public Measure<Per<Udim, Xdim>> getP() { return kP; }
  public Measure<Per<Udim, Mult<Xdim, Time>>> getI() { return kI; }
  public Measure<Per<Udim, Velocity<Xdim>>> getD() { return kD; }
  public Measure<Udim> getS() { return kS; }
  public Measure<Per<Udim, Xdim>> getF() { return kF; }
  public Measure<Udim> getG() { return kG; }
}
