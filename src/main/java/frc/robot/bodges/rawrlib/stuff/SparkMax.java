package frc.robot.bodges.rawrlib.stuff;

import java.util.Arrays;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.junk.AWheeMotor;

public class SparkMax<D extends Unit<D>> extends AWheeMotor<D> {

  protected final CANSparkMax m;

  public SparkMax(CANSparkMax downstream) {
    this.m = downstream;
    m.restoreFactoryDefaults();
  }
	public SparkMax(MotorType type, Integer id) {
		this(new CANSparkMax(id, type));
	}
	public static <D extends Unit<D>> AWheeMotor<D> of(MotorType type, Integer... ids) {
		return Arrays.stream(ids).<AWheeMotor<D>>map(id -> new SparkMax<D>(type, id)).reduce((leader, follower) -> follower.follow(leader)).get();
	}
	public static <D extends Unit<D>> AWheeMotor<D> opposedPairOf(CANSparkMax a, CANSparkMax b) {
		AWheeMotor<D> mA = new SparkMax<>(a);
		AWheeMotor<D> mB = new SparkMax<>(b);
		mB.follow(mA);
		mB.setInverted(true);
		return mA;
	}
	public static <D extends Unit<D>> AWheeMotor<D> opposedPairOf(MotorType type, Integer a, Integer b) {
		return opposedPairOf(new CANSparkMax(a, type), new CANSparkMax(b, type));
	}

  @Override
  public AWheeMotor<D> setPositionOffset(Measure<D> offset) {
    m.getEncoder().setPosition(outputToEncoder.pos.apply(offset).in(Units.Rotations));
    return this;
  }

	@Override
	public AWheeMotor<D> zero() {
		m.getEncoder().setPosition(0);
		return this;
	}

	@Override
	public AWheeMotor<D> setPositionP(Measure<Per<Voltage, D>> pP) {
    super.setPositionP(pP);
    m.getPIDController().setP(outputToEncoder.kP.apply(pP).in(Units.Volts.per(Units.Rotations)), 0);
    return this;
	}

	@Override
	public AWheeMotor<D> setPositionD(Measure<Per<Voltage, Velocity<D>>> pD) {
    super.setPositionD(pD);
    m.getPIDController().setD(outputToEncoder.kD.apply(pD).in(Units.Volts.per(Units.RotationsPerSecond)), 0);
    return this;
	}

	@Override
	public AWheeMotor<D> setVelocityP(Measure<Per<Voltage, Velocity<D>>> vP) {
    super.setVelocityP(vP);
    m.getPIDController().setP(outputToEncoder.kD.apply(vP).in(Units.Volts.per(Units.RotationsPerSecond)), 1);
    return this;
	}

	@Override
	public AWheeMotor<D> setVelocityD(Measure<Per<Voltage, Velocity<Velocity<D>>>> vD) {
    super.setVelocityD(vD);
    m.getPIDController().setD(outputToEncoder.kDD.apply(vD).in(Units.Volts.per(Units.RotationsPerSecond.per(Units.Second))), 1);
    return this;
	}

	@Override
	public Measure<Angle> getPosition_downstream() {
		return Units.Rotations.of(m.getEncoder().getPosition());
	}
	@Override
	public Measure<Velocity<Angle>> getVelocity_downstream() {
		return Units.RPM.of(m.getEncoder().getVelocity());
	}
	@Override
	public AWheeMotor<D> setPosition_downstream(Measure<Angle> reference) {
		m.getPIDController().setReference(reference.in(Units.Rotations), ControlType.kPosition, 0);
		return this;
	}
	@Override
	public AWheeMotor<D> setVelocity_downstream(Measure<Velocity<Angle>> reference) {
		// FIXME ff might not be correct, but then, it might be. Math looks good but idk
		// about how it applies the ff after the control loop
		m.getPIDController().setReference(
			reference.in(Units.RPM)
			, ControlType.kVelocity
			, 1
			//, ((Measure<Voltage>)outputToEncoder.kD.apply(vF).times(reference)).in(Units.Volts) // FIXME npe
		);
		return this;
	}

	@Override
	public AWheeMotor<D> setVoltage_downstream(Measure<Voltage> voltage) {
    m.setVoltage(voltage.in(Units.Volts));
    return this;
	}

}
