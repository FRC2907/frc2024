package frc.robot.bodges.rawrlib.units;

import edu.wpi.first.units.*;

class UnitConverter<From extends Unit<From>, To extends Unit<To>> {
	protected final To b_type;
	protected final Measure<Per<To, From>> factorBperA;

	protected double d(double a) {
		return a * factorBperA.baseUnitMagnitude();
	}
	public Measure<To> convert(Measure<From> a) {
		return b_type.ofBaseUnits(d(a.baseUnitMagnitude()));
	}

  public UnitConverter(Measure<From> from, Measure<To> to) {
    this.b_type = to.unit();
    this.factorBperA = to.divide(from.magnitude()).per(from.unit());
  }

}