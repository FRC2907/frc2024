package frc.robot.bodges.rawrlib.units;

import java.util.function.Function;

import edu.wpi.first.units.*;

public class ControlConversions<From extends Unit<From>, To extends Unit<To>> {
  protected final UnitConverter<From, To> _pos;
  protected final UnitConverter<Velocity<From>, Velocity<To>> _vel;
  protected final UnitConverter<Velocity<Velocity<From>>, Velocity<Velocity<To>>> _acc;

  public final Function<Measure<From>, Measure<To>> pos;
  public final Function<Measure<Velocity<From>>, Measure<Velocity<To>>> vel;
  public final Function<Measure<Velocity<Velocity<From>>>, Measure<Velocity<Velocity<To>>>> acc;

  public ControlConversions(Measure<From> from, Measure<To> to) {
    this._pos = new UnitConverter<>(from, to);
    this._vel = new UnitConverter<>(from.per(Units.Second), to.per(Units.Second));
    this._acc = new UnitConverter<>(from.per(Units.Second).per(Units.Second), to.per(Units.Second).per(Units.Second));

    this.pos = _pos::convert;
    this.vel = _vel::convert;
    this.acc = _acc::convert;
  }
}
