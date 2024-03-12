package frc.robot.bodges.rawrlib.units;

import java.util.function.Function;

import edu.wpi.first.units.*;

public class MotorConversions<From extends Unit<From>, To extends Unit<To>> extends ControlConversions<From, To> {

  protected final UnitConverter<Per<Voltage, From>, Per<Voltage, To>> _kP;
  protected final UnitConverter<Per<Voltage, Velocity<From>>, Per<Voltage, Velocity<To>>> _kD;
  protected final UnitConverter<Per<Voltage, Velocity<Velocity<From>>>, Per<Voltage, Velocity<Velocity<To>>>> _kDD;

  public final Function<Measure<Per<Voltage, From>>, Measure<Per<Voltage, To>>> kP;
  public final Function<Measure<Per<Voltage, Velocity<From>>>, Measure<Per<Voltage, Velocity<To>>>> kD;
  public final Function<Measure<Per<Voltage, Velocity<Velocity<From>>>>, Measure<Per<Voltage, Velocity<Velocity<To>>>>> kDD;

	public MotorConversions(Measure<From> from, Measure<To> to) {
		super(from, to);
    this._kP = new UnitConverter<>(
      Units.Volts.of(1).divide(from.magnitude()).per(from.unit())
    , Units.Volts.of(1).divide(to.magnitude()).per(to.unit())
    );
    this._kD = new UnitConverter<>(
      Units.Volts.of(1).divide(from.magnitude()).per(from.unit().per(Units.Second))
    , Units.Volts.of(1).divide(to.magnitude()).per(to.unit().per(Units.Second))
    );
    this._kDD = new UnitConverter<>(
      Units.Volts.of(1).divide(from.magnitude()).per(from.unit().per(Units.Second).per(Units.Second))
    , Units.Volts.of(1).divide(to.magnitude()).per(to.unit().per(Units.Second).per(Units.Second))
    );

    this.kP = _kP::convert;
    this.kD = _kD::convert;
    this.kDD = _kDD::convert;
	}
}
