package frc.robot.bodges.rawrlib.stuff;

import java.util.Arrays;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.controls.*;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.junk.AWheeMotor;

public class TalonFX<D extends Unit<D>> extends AWheeMotor<D> {

  protected com.ctre.phoenix6.hardware.TalonFX m;

  public TalonFX(com.ctre.phoenix6.hardware.TalonFX downstream) {
    this.m = downstream;
    m.getConfigurator().apply(new TalonFXConfiguration());
  }
  public TalonFX(Integer id) {
    this(new com.ctre.phoenix6.hardware.TalonFX(id));
  }
  public static <D extends Unit<D>> AWheeMotor<D> of(Integer... ids) {
    return Arrays.stream(ids).<AWheeMotor<D>>map(id -> new TalonFX<D>(id)).reduce((leader, follower) -> follower.follow(leader)).get();
  }
  public static <D extends Unit<D>> AWheeMotor<D> opposedPairOf(Integer a, Integer b) {
		AWheeMotor<D> mA = new TalonFX<>(a);
		AWheeMotor<D> mB = new TalonFX<>(b);
		mB.follow(mA);
		mB.setInverted(true);
		return mA;
  }

  @Override
  public AWheeMotor<D> setPositionOffset(Measure<D> offset) {
    m.setPosition(outputToEncoder.pos.apply(offset).in(Units.Rotations));
    return this;
  }

  @Override
  public AWheeMotor<D> zero() {
    m.setPosition(0);
    return this;
  }

	@Override
	public AWheeMotor<D> setPositionP(Measure<Per<Voltage, D>> pP) {
    super.setPositionP(pP);
    pushPositionGains();
    return this;
	}

	@Override
	public AWheeMotor<D> setPositionD(Measure<Per<Voltage, Velocity<D>>> pD) {
    super.setPositionD(pD);
    pushPositionGains();
    return this;
	}

  protected TalonFX<D> pushPositionGains() {
    m.getConfigurator().apply(
    new Slot0Configs()
      .withKP(outputToEncoder.kP.apply(pP).in(Units.Volts.per(Units.Rotations)))
      .withKD(outputToEncoder.kD.apply(pD).in(Units.Volts.per(Units.RotationsPerSecond)))
    );
    return this;
  }

	@Override
	public AWheeMotor<D> setVelocityP(Measure<Per<Voltage, Velocity<D>>> vP) {
    super.setVelocityP(vP);
    pushVelocityGains();
    return this;
	}

	@Override
	public AWheeMotor<D> setVelocityD(Measure<Per<Voltage, Velocity<Velocity<D>>>> vD) {
    super.setVelocityD(vD);
    pushVelocityGains();
    return this;
	}

	@Override
	public AWheeMotor<D> setVelocityF(Measure<Per<Voltage, Velocity<D>>> vF) {
    super.setVelocityF(vF);
    pushVelocityGains();
    return this;
	}

  protected TalonFX<D> pushVelocityGains() {
    m.getConfigurator().apply(
    new Slot1Configs()
      .withKP(outputToEncoder.kD.apply(vP).in(Units.Volts.per(Units.RotationsPerSecond)))
      .withKD(outputToEncoder.kDD.apply(vD).in(Units.Volts.per(Units.RotationsPerSecond.per(Units.Second))))
      .withKV(outputToEncoder.kD.apply(vF).in(Units.Volts.per(Units.RotationsPerSecond)))
    );
    return this;
  }

  // TODO: support Motion Magic

	@Override
	public Measure<Angle> getPosition_downstream() {
    return Units.Rotations.of(m.getPosition().getValueAsDouble());
	}

	@Override
	public Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.RotationsPerSecond.of(m.getVelocity().getValueAsDouble());
	}

	@Override
	public AWheeMotor<D> setPosition_downstream(Measure<Angle> reference) {
    m.setControl(new PositionVoltage(reference.in(Units.Rotations)).withSlot(0));
    return this;
	}

	@Override
	public AWheeMotor<D> setVelocity_downstream(Measure<Velocity<Angle>> reference) {
    m.setControl(new VelocityVoltage(reference.in(Units.RotationsPerSecond)).withSlot(1));
    return this;
	}

	@Override
	public AWheeMotor<D> setVoltage_downstream(Measure<Voltage> voltage) {
    m.setVoltage(voltage.in(Units.Volts));
    return this;
	}

}