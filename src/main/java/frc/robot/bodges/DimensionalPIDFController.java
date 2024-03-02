package frc.robot.bodges;

import java.util.function.Supplier;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.Util;

public class DimensionalPIDFController<StateDimension extends Unit<StateDimension>, InputDimension extends Unit<InputDimension>> {

  public DimensionalPIDFController() {
    timer.restart();
  }

  protected Supplier<Measure<StateDimension>> referenceSupplier;
  public Measure<StateDimension> getReference() { return this.referenceSupplier.get(); }
  public DimensionalPIDFController<StateDimension, InputDimension> setReference(Measure<StateDimension> reference) {
    this.referenceSupplier = () -> reference;
    return this;
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
  protected DimensionalErrorTracker<StateDimension> errorTracker;
  public DimensionalErrorTracker<StateDimension> getErrorTracker() { return errorTracker; }
  public DimensionalPIDFController<StateDimension, InputDimension> setErrorTracker(DimensionalErrorTracker<StateDimension> errorTracker) {
    this.errorTracker = errorTracker;
    return this;
  }

  protected Measure<StateDimension> hysteresis;
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

  protected DimensionalPIDFGains<StateDimension, InputDimension> gains; // FIXME this needs init
  public DimensionalPIDFGains<StateDimension, InputDimension> getGains() { return gains; }
  public DimensionalPIDFController<StateDimension, InputDimension> setGains(DimensionalPIDFGains<StateDimension, InputDimension> gains) {
    this.gains = gains;
    return this;
  }

  protected Measure<InputDimension> inputLimit;
  public Measure<InputDimension> getInputLimit() { return inputLimit; }
  public DimensionalPIDFController<StateDimension, InputDimension> setInputLimit(Measure<InputDimension> inputLimit) {
    this.inputLimit = inputLimit;
    return this;
  }

  public void updateErrors() {
    Measure<StateDimension> newError = getError();
    errorTracker.update(newError, getDeltaTime());
  }

  /**
   * Override this function in specialized classes, e.g. to include arm control, which is not used here.
   * In theory, the output of this function should provide open-loop control.
   * 
   * @return Sum of feedforwards to apply based on current errors and gains.
   */
  public Measure<InputDimension> getFeedforwards() {
    Measure<InputDimension> out = gains.getF(getReference());
    out = out.plus(gains.getS());
    out = out.plus(gains.getG());
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

}
