package frc.robot.bodges.rawrlib.stuff;

import java.util.ArrayList;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.units.MotorConversions;
import frc.robot.util.Util;

public abstract class AWheeMotor<D extends Unit<D>> {

	public abstract Measure<Angle> getPosition_downstream();

	public abstract Measure<Velocity<Angle>> getVelocity_downstream();

	public abstract AWheeMotor<D> setPosition_downstream(Measure<Angle> reference);

	public abstract AWheeMotor<D> setVelocity_downstream(Measure<Velocity<Angle>> reference);

	public abstract AWheeMotor<D> setVoltage_downstream(Measure<Voltage> voltage);

	public abstract AWheeMotor<D> zero();

	public abstract AWheeMotor<D> setPositionOffset(Measure<D> offset);

	public MotorConversions<Angle, D> encoderToOutput;
	public MotorConversions<D, Angle> outputToEncoder;

	public AWheeMotor<D> setFactor(Measure<D> output, Measure<Angle> encoder) {
		this.encoderToOutput = new MotorConversions<>(encoder, output);
		this.outputToEncoder = new MotorConversions<>(output, encoder);
		for (var follower : followers)
			follower.setFactor(output, encoder);
		return this;
	}

	@SuppressWarnings("unchecked")
	public AWheeMotor<D> setFactor(Measure<Per<D, Angle>> ratio) {
		Measure<D> output = (Measure<D>) ratio.times(Units.Rotations.of(1));
		Measure<Angle> encoder = Units.Rotations.of(1);
		return setFactor(output, encoder);
	}

	protected boolean isInverted = false;

	public AWheeMotor<D> setInverted(Boolean invert) {
		this.isInverted = invert;
		return this;
	}

	public Measure<D> getPosition() {
		return invertIfNeeded(encoderToOutput.pos.apply(getPosition_downstream()));
	}

	public Measure<Velocity<D>> getVelocity() {
		return invertIfNeeded(encoderToOutput.vel.apply(getVelocity_downstream()));
	}

	private ArrayList<AWheeMotor<D>> followers = new ArrayList<>();

	public AWheeMotor<D> follow(AWheeMotor<D> leader) {
		leader.followers.add(this);
		this.encoderToOutput = leader.encoderToOutput;
		this.outputToEncoder = leader.outputToEncoder;
		if (this.encoderToOutput != null && this.outputToEncoder != null) {
			setMaxPosition(leader.getMaxPosition());
			setMaxVelocity(leader.getMaxVelocity());
			setMaxAcceleration(leader.getMaxAcceleration());
			setMaxVoltage(leader.getMaxVoltage());
			setMinPosition(leader.getMinPosition());
			setMinVelocity(leader.getMinVelocity());
			setMinAcceleration(leader.getMinAcceleration());
			setMinVoltage(leader.getMinVoltage());
			setPositionP(leader.getPositionP());
			setPositionD(leader.getPositionD());
			setVelocityP(leader.getVelocityP());
			setVelocityD(leader.getVelocityD());
			setVelocityF(leader.getVelocityF());
		}
		return this;
	}

	public AWheeMotor<D> lead(AWheeMotor<D> follower) {
		follower.follow(this);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<D> positionReference = (Measure<D>) Util.anyZero();

	@SuppressWarnings("unchecked")
	public AWheeMotor<D> setPosition(Measure<D> reference) {
		reference = Util.clamp(minPos, reference, maxPos);

		this.positionReference = reference;
		this.velocityReference = (Measure<Velocity<D>>) Util.anyZero();
		overridePositionHysteresis(false);

		setPosition_downstream(invertIfNeeded(outputToEncoder.pos.apply(reference)));
		for (var follower : followers)
			follower.setPosition(reference);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<Velocity<D>> velocityReference = (Measure<Velocity<D>>) Util.anyZero();

	public AWheeMotor<D> setVelocity(Measure<Velocity<D>> reference) {
		reference = Util.clamp(minVel, reference, maxVel);

		if (Util.isPositive(reference) && getPosition().gte(getMaxPosition()))
			return setPosition(getMaxPosition());
		if (Util.isNegative(reference) && getPosition().lte(getMinPosition()))
			return setPosition(getMinPosition());
		this.velocityReference = reference;
		overridePositionHysteresis(true);

		setVelocity_downstream(invertIfNeeded(outputToEncoder.vel.apply(reference)));
		for (var follower : followers)
			follower.setVelocity(reference);
		return this;
	}

	public AWheeMotor<D> setVoltage(Measure<Voltage> voltage) {
		voltage = Util.clamp(minV, voltage, maxV);
		setVoltage_downstream(invertIfNeeded(voltage));
		for (var follower : followers)
			follower.setVoltage(voltage);
		return this;
	}

	@SuppressWarnings("unchecked")
	private Measure<D> maxPos = (Measure<D>) Util.anyInf();

	public AWheeMotor<D> setMaxPosition(Measure<D> maxPos) {
		this.maxPos = maxPos;
		for (var follower : followers)
			follower.setMaxPosition(maxPos);
		return this;
	}

	@SuppressWarnings("unchecked")
	private Measure<Velocity<D>> maxVel = (Measure<Velocity<D>>) Util.anyInf();

	public AWheeMotor<D> setMaxVelocity(Measure<Velocity<D>> maxVel) {
		this.maxVel = maxVel;
		for (var follower : followers)
			follower.setMaxVelocity(maxVel);
		return this;
	}

	@SuppressWarnings("unchecked")
	private Measure<Velocity<Velocity<D>>> maxAcc = (Measure<Velocity<Velocity<D>>>) Util.anyInf();

	public AWheeMotor<D> setMaxAcceleration(Measure<Velocity<Velocity<D>>> maxAcc) {
		this.maxAcc = maxAcc;
		for (var follower : followers)
			follower.setMaxAcceleration(maxAcc);
		return this;
	}

	private Measure<Voltage> maxV = Units.Volts.of(Double.POSITIVE_INFINITY);

	public AWheeMotor<D> setMaxVoltage(Measure<Voltage> maxV) {
		this.maxV = maxV;
		for (var follower : followers)
			follower.setMaxVoltage(maxV);
		return this;
	}

	@SuppressWarnings("unchecked")
	private Measure<D> minPos = (Measure<D>) Util.anyInf().negate();

	public AWheeMotor<D> setMinPosition(Measure<D> minPos) {
		this.minPos = minPos;
		for (var follower : followers)
			follower.setMinPosition(minPos);
		return this;
	}

	@SuppressWarnings("unchecked")
	private Measure<Velocity<D>> minVel = (Measure<Velocity<D>>) Util.anyInf().negate();

	public AWheeMotor<D> setMinVelocity(Measure<Velocity<D>> minVel) {
		this.minVel = minVel;
		for (var follower : followers)
			follower.setMinVelocity(minVel);
		return this;
	}

	@SuppressWarnings("unchecked")
	private Measure<Velocity<Velocity<D>>> minAcc = (Measure<Velocity<Velocity<D>>>) Util.anyInf().negate();

	public AWheeMotor<D> setMinAcceleration(Measure<Velocity<Velocity<D>>> minAcc) {
		this.minAcc = minAcc;
		for (var follower : followers)
			follower.setMinAcceleration(minAcc);
		return this;
	}

	private Measure<Voltage> minV = Units.Volts.of(Double.NEGATIVE_INFINITY);

	public AWheeMotor<D> setMinVoltage(Measure<Voltage> minV) {
		this.minV = minV;
		for (var follower : followers)
			follower.setMinVoltage(minV);
		return this;
	}

	public AWheeMotor<D> setSymmetricalPosition(Measure<D> peakPos) {
		setMaxPosition(Util.abs(peakPos));
		setMinPosition(Util.abs(peakPos).negate());
		return this;
	}

	public AWheeMotor<D> setSymmetricalVelocity(Measure<Velocity<D>> peakVel) {
		setMaxVelocity(Util.abs(peakVel));
		setMinVelocity(Util.abs(peakVel).negate());
		return this;
	}

	public AWheeMotor<D> setSymmetricalAcceleration(Measure<Velocity<Velocity<D>>> peakAcc) {
		setMaxAcceleration(Util.abs(peakAcc));
		setMinAcceleration(Util.abs(peakAcc).negate());
		return this;
	}

	public AWheeMotor<D> setSymmetricalVoltage(Measure<Voltage> peakV) {
		setMaxVoltage(Util.abs(peakV));
		setMinVoltage(Util.abs(peakV).negate());
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<Per<Voltage, D>> pP = (Measure<Per<Voltage, D>>) Util.anyZero();

	public AWheeMotor<D> setPositionP(Measure<Per<Voltage, D>> pP) {
		this.pP = pP;
		for (var follower : followers)
			follower.setPositionP(pP);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<Per<Voltage, Velocity<D>>> pD = (Measure<Per<Voltage, Velocity<D>>>) Util.anyZero();

	public AWheeMotor<D> setPositionD(Measure<Per<Voltage, Velocity<D>>> pD) {
		this.pD = pD;
		for (var follower : followers)
			follower.setPositionD(pD);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<D> pH = (Measure<D>) Util.anyInf();

	public AWheeMotor<D> setPositionHysteresis(Measure<D> pH) {
		this.pH = pH;
		for (var follower : followers)
			follower.setPositionHysteresis(pH);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<Per<Voltage, Velocity<D>>> vP = (Measure<Per<Voltage, Velocity<D>>>) Util.anyZero();

	public AWheeMotor<D> setVelocityP(Measure<Per<Voltage, Velocity<D>>> vP) {
		this.vP = vP;
		for (var follower : followers)
			follower.setVelocityP(vP);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<Per<Voltage, Velocity<Velocity<D>>>> vD = (Measure<Per<Voltage, Velocity<Velocity<D>>>>) Util.anyZero();

	public AWheeMotor<D> setVelocityD(Measure<Per<Voltage, Velocity<Velocity<D>>>> vD) {
		this.vD = vD;
		for (var follower : followers)
			follower.setVelocityD(vD);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<Per<Voltage, Velocity<D>>> vF = (Measure<Per<Voltage, Velocity<D>>>) Util.anyZero();

	public AWheeMotor<D> setVelocityF(Measure<Per<Voltage, Velocity<D>>> vF) {
		this.vF = vF;
		for (var follower : followers)
			follower.setVelocityF(vF);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Measure<Velocity<D>> vH = (Measure<Velocity<D>>) Util.anyInf();

	public AWheeMotor<D> setVelocityHysteresis(Measure<Velocity<D>> vH) {
		this.vH = vH;
		for (var follower : followers)
			follower.setVelocityHysteresis(vH);
		return this;
	}

	protected boolean overridePositionHysteresis = false;

	public AWheeMotor<D> overridePositionHysteresis(boolean override) {
		this.overridePositionHysteresis = override;
		return this;
	}

	public Boolean getInverted() {
		return isInverted;
	}

	public ArrayList<AWheeMotor<D>> getFollowers() {
		return followers;
	}

	public Measure<D> getPositionReference() {
		return positionReference;
	}

	public Measure<Velocity<D>> getVelocityReference() {
		return velocityReference;
	}

	public Measure<D> getMaxPosition() {
		return maxPos;
	}

	public Measure<Velocity<D>> getMaxVelocity() {
		return maxVel;
	}

	public Measure<Velocity<Velocity<D>>> getMaxAcceleration() {
		return maxAcc;
	}

	public Measure<Voltage> getMaxVoltage() {
		return maxV;
	}

	public Measure<D> getMinPosition() {
		return minPos;
	}

	public Measure<Velocity<D>> getMinVelocity() {
		return minVel;
	}

	public Measure<Velocity<Velocity<D>>> getMinAcceleration() {
		return minAcc;
	}

	public Measure<Voltage> getMinVoltage() {
		return minV;
	}

	public Measure<Per<Voltage, D>> getPositionP() {
		return pP;
	}

	public Measure<Per<Voltage, Velocity<D>>> getPositionD() {
		return pD;
	}

	@SuppressWarnings("unchecked")
	public Measure<D> getPositionHysteresis() {
		return overridePositionHysteresis ? (Measure<D>) Util.anyInf() : pH;
	}

	public Measure<Per<Voltage, Velocity<D>>> getVelocityP() {
		return vP;
	}

	public Measure<Per<Voltage, Velocity<Velocity<D>>>> getVelocityD() {
		return vD;
	}

	public Measure<Per<Voltage, Velocity<D>>> getVelocityF() {
		return vF;
	}

	public Measure<Velocity<D>> getVelocityHysteresis() {
		return vH;
	}

	public <T extends Unit<T>> Measure<T> invertIfNeeded(Measure<T> value) {
		if (getInverted())
			return value.negate();
		return value;
	}

	public boolean checkHysteresis() {
		return Util.checkHysteresis(getPositionError(), getPositionHysteresis())
				&& Util.checkHysteresis(getVelocityError(), getVelocityHysteresis());
	}

	public Measure<D> getPositionError() {
		return getPositionReference().minus(getPosition());
	}

	public Measure<Velocity<D>> getVelocityError() {
		return getVelocityReference().minus(getVelocity());
	}
}
