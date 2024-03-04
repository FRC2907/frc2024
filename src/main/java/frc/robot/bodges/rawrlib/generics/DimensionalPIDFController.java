package frc.robot.bodges.rawrlib.generics;

import java.util.Optional;
import java.util.function.Supplier;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.Misc;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class DimensionalPIDFController<StateDimension extends Unit<StateDimension>, InputDimension extends Unit<InputDimension>> implements ISubsystem {

  public DimensionalPIDFController() {
    timer.restart();
  }

  protected Supplier<Measure<StateDimension>> referenceSupplier;
  public Measure<StateDimension> getReference_unbounded() { return this.referenceSupplier.get(); }
  // bam! motion magic
  @SuppressWarnings({ "unchecked" })
  public Measure<StateDimension> getReference() {
    Measure<StateDimension> out = getReference_unbounded();
    if (!Misc.kEnableMotionMagic) return out;

    Optional<Measure<StateDimension>> minX = getGains().getMinRef(), maxX = getGains().getMaxRef();
    Optional<Measure<Velocity<StateDimension>>> minDX = getGains().getMinVel(), maxDX = getGains().getMaxVel();
    Optional<Measure<Velocity<Velocity<StateDimension>>>> minDDX = getGains().getMinAccel(), maxDDX = getGains().getMaxAccel();

    Measure<StateDimension> lastState = errorTracker.getState();
    Measure<Velocity<StateDimension>> lastVelocity = errorTracker.getVelocity();

    Measure<Velocity<StateDimension>> minVelocityStep, minNextVelocity = null;
    Measure<StateDimension> minStateStep, minNextState = null;
    if (Misc.kEnableAccelerationLimiting && minDDX.isPresent()) {
      // we care about acceleration control, so we can put a lower bound on our next velocity
      minVelocityStep = (Measure<Velocity<StateDimension>>) minDDX.get().times(MechanismConstraints.kPeriod);
      minNextVelocity = lastVelocity.plus(minVelocityStep);
    }
    if (Misc.kEnableVelocityLimiting && minDX.isPresent()) {
      // we care about velocity control, so we can put a lower bound on our next velocity
      minNextVelocity = Util.max(minDX.get(), minNextVelocity);
    }
    if (minNextVelocity != null) {
      // we have a lower bound for our next velocity, which means we have a lower bound on our next state
      minStateStep = (Measure<StateDimension>) minNextVelocity.times(MechanismConstraints.kPeriod);
      minNextState = lastState.plus(minStateStep);
    }
    if (Misc.kEnableStateLimiting && minX.isPresent()) {
      // we care about position control, so we can put a lower bound on our next state
      minNextState = Util.max(minX.get(), minNextState);
    }
    // if we have any of the above motion constraints, we have reduced them to a state constraint, so apply that
    out = Util.max(out, minNextState);


    Measure<Velocity<StateDimension>> maxVelocityStep, maxNextVelocity = null;
    Measure<StateDimension> maxStateStep, maxNextState = null;
    if (Misc.kEnableAccelerationLimiting && maxDDX.isPresent()) {
      // we care about acceleration control, so we can put an upper bound on our next velocity
      maxVelocityStep = (Measure<Velocity<StateDimension>>) maxDDX.get().times(MechanismConstraints.kPeriod);
      maxNextVelocity = lastVelocity.plus(maxVelocityStep);
    }
    if (Misc.kEnableVelocityLimiting && maxDX.isPresent()) {
      // we care about velocity control, so we can put an upper bound on our next velocity
      maxNextVelocity = Util.min(maxDX.get(), maxNextVelocity);
    }
    if (maxNextVelocity != null) {
      // we have an upper bound for our next velocity, which means we have an upper bound on our next state
      maxStateStep = (Measure<StateDimension>) maxNextVelocity.times(MechanismConstraints.kPeriod);
      maxNextState = lastState.plus(maxStateStep);
    }
    if (Misc.kEnableStateLimiting && maxX.isPresent()) {
      // we care about position control, so we can put an upper bound on our next state
      maxNextState = Util.min(maxX.get(), maxNextState);
    }
    // if we have any of the above motion constraints, we have reduced them to a state constraint, so apply that
    out = Util.min(out, maxNextState);

    return out;
  }

  // if we're tracking a static reference, this supplier will continually produce
  // that reference. this means that we can limit the output of getReference()
  // however we want, and the system will still attempt to approach what the
  // caller asked for.
  public DimensionalPIDFController<StateDimension, InputDimension> setReference(Measure<StateDimension> reference) {
    return setReference(() -> reference);
  }
  public DimensionalPIDFController<StateDimension, InputDimension> setReference(Supplier<Measure<StateDimension>> referenceSupplier) {
    this.referenceSupplier = referenceSupplier;
    return this;
  }

  protected Supplier<Measure<StateDimension>> stateSupplier;
  public Measure<StateDimension> getState() { return stateSupplier.get(); }
  public Supplier<Measure<StateDimension>> getStateSupplier() { return stateSupplier; }
  public DimensionalPIDFController<StateDimension, InputDimension> setStateSupplier(Supplier<Measure<StateDimension>> stateSupplier) {
    this.stateSupplier = stateSupplier;
    return this;
  }

  public Measure<StateDimension> getError() { return getReference().minus(getState()); }
  protected DimensionalErrorTracker<StateDimension> errorTracker = new DimensionalErrorTracker<StateDimension>();
  public DimensionalErrorTracker<StateDimension> getErrorTracker() { return errorTracker; }
  public DimensionalPIDFController<StateDimension, InputDimension> setErrorTracker(DimensionalErrorTracker<StateDimension> errorTracker) {
    this.errorTracker = errorTracker;
    return this;
  }

  @SuppressWarnings("unchecked")
  protected Measure<StateDimension> hysteresis = (Measure<StateDimension>) Util.anyZero();
  public Measure<StateDimension> getHysteresis() { return hysteresis; }
  public DimensionalPIDFController<StateDimension, InputDimension> setHysteresis(Measure<StateDimension> hysteresis) {
    this.hysteresis = hysteresis;
    return this;
  }


  protected Timer timer = new Timer();
  protected Supplier<Measure<Time>> deltaTimeSupplier = () -> {
    Measure<Time> out = Units.Seconds.of(timer.get());
    timer.restart();
    return out;
  };
  public Measure<Time> getDeltaTime() { return deltaTimeSupplier.get(); }
  public Supplier<Measure<Time>> getDeltaTimeSupplier() { return deltaTimeSupplier; }
  public DimensionalPIDFController<StateDimension, InputDimension> setDeltaTimeSupplier(Supplier<Measure<Time>> deltaTimeSupplier) {
    this.deltaTimeSupplier = deltaTimeSupplier;
    return this;
  }

  protected DimensionalPIDFGains<StateDimension, InputDimension> gains = new DimensionalPIDFGains<StateDimension, InputDimension>();
  public DimensionalPIDFGains<StateDimension, InputDimension> getGains() { return gains; }
  public DimensionalPIDFController<StateDimension, InputDimension> setGains(DimensionalPIDFGains<StateDimension, InputDimension> gains) {
    this.gains = gains;
    return this;
  }

  protected Measure<InputDimension> inputLimit;
  public Measure<InputDimension> getInputLimit() { return inputLimit; }
  
  /**
   * Set a limit on the amount of input this controller can command. This should
   * be used as a safety measure, not a motion control mechanism. Use
   * DimensionalPIDFGains::set(Min|Max)* to control motion parameters, or
   * DimensionalFeedbackMotor::set(Min|Max|Symmetrical)* if driving a motor.
   * 
   * @param inputLimit The maximum input this controller is permitted to command
   *                   (e.g., 12 V)
   * @return This instance of a controller, for chaining
   */
  public DimensionalPIDFController<StateDimension, InputDimension> setInputLimit(Measure<InputDimension> inputLimit) {
    this.inputLimit = Util.abs(inputLimit);
    return this;
  }

  public void updateErrors() {
    errorTracker.update(getState(), getError(), getDeltaTime());
  }

  /**
   * Override this function in specialized classes, e.g. to include arm control,
   * which is not used here. In theory, the output of this function should provide
   * open-loop control.
   * 
   * @return Sum of feedforwards to apply based on current errors and gains.
   */
  public Measure<InputDimension> getFeedforwards() {
    Measure<InputDimension> out = gains.getF(getReference());
    out = out.plus(gains.getG());
    // this might still be wrong for velocity control
      if (Util.isPositive(getError()))
        out = out.plus(gains.getS());
      else if (Util.isNegative(getError()))
        out = out.minus(gains.getS());
    return out;
  }

  @SuppressWarnings({"unchecked"})
  public Measure<InputDimension> getFeedback() {
    Measure<InputDimension> out = (Measure<InputDimension>) errorTracker.getError().times(gains.getP());
    out = out.plus((Measure<InputDimension>) errorTracker.getErrorSum().times(gains.getI()));
    out = out.plus((Measure<InputDimension>) errorTracker.getErrorSpeed().times(gains.getD()));
    return out;
  }

  public Measure<InputDimension> calculate() {
    return calculate(true);
  }

  public Measure<InputDimension> openLoop() {
    return calculate(false);
  }

  public Measure<InputDimension> closedLoop() {
    return calculate(true);
  }

  public Measure<InputDimension> calculate(boolean enableFeedback) {
    Measure<InputDimension> out = calculate_unbounded(enableFeedback);
    if (inputLimit != null)
      return Util.clampSymmetrical(out, inputLimit);
    return out;
  }

  public Measure<InputDimension> calculate_unbounded(boolean enableFeedback) {
    updateErrors();
    if (enableFeedback)
      return getFeedforwards().plus(getFeedback());
    return getFeedforwards();
  }

  // FIXME support derivative test
  public boolean converged() {
    return Util.checkHysteresis(getError(), getHysteresis());
  }

  @Override
  public void onLoop() {
    receiveOptions();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
  }

  @Override
  public void receiveOptions() {
  }

}
