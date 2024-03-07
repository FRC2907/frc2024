package frc.robot.bodges.rawrlib.generics;

import java.util.function.Supplier;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.motors.WrappedMotorController;
import frc.robot.constants.Misc;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

/**
 * Describes a generic motor with closed-loop position and velocity control.
 */
public class DimensionalFeedbackMotor<D extends Unit<D>> implements ISubsystem {

    protected WrappedMotorController m;
    public WrappedMotorController getWrappedMotorController() { return m; }
    public DimensionalFeedbackMotor<D> setWrappedMotorController(WrappedMotorController m) {
      this.m = m;
      return this;
    }

    public enum TrackingMode { kPosition, kVelocity }
    protected TrackingMode trackingMode;
    public TrackingMode getTrackingMode() { return this.trackingMode; }
    public boolean trackingPosition() { return getTrackingMode() == TrackingMode.kPosition; }
    public boolean trackingVelocity() { return getTrackingMode() == TrackingMode.kVelocity; }
    protected DimensionalFeedbackMotor<D> setTrackingMode(TrackingMode mode) {
      this.trackingMode = mode;
      return this;
    }

    protected String name;
    public String getName() { return name; }
    public DimensionalFeedbackMotor<D> setName(String name) {
        this.name = name;
        return this;
    }

    public DimensionalFeedbackMotor<D> setInverted(boolean isInverted) {
        m.setInverted(isInverted);
        return this;
    }

    protected Measure<Per<D, Angle>> factor;
    public DimensionalFeedbackMotor<D> setFactor(Measure<Per<D, Angle>> factor) {
        this.factor = factor;
        return this;
    }


    @SuppressWarnings("unchecked")
    public Measure<D> getPosition() {
      return (Measure<D>) m.getPosition_downstream().times(factor);
    }

    @SuppressWarnings("unchecked")
    public Measure<Velocity<D>> getVelocity() {
      return (Measure<Velocity<D>>) m.getVelocity_downstream().times(factor);
    }

  protected DimensionalPIDFController<D, Voltage> positionController = new DimensionalPIDFController<D, Voltage>()
    .setStateSupplier(this::getPosition);
  public DimensionalPIDFController<D, Voltage> getPositionController() { return positionController; }
  public DimensionalFeedbackMotor<D> setPositionController(DimensionalPIDFController<D, Voltage> ctlr) {
    this.positionController = ctlr;
    getPositionController().setStateSupplier(this::getPosition);
    return this;
  }
  public DimensionalFeedbackMotor<D> configurePositionController(DimensionalPIDFGains<D, Voltage> gains) {
    getPositionController().setGains(gains);
    return this;
  }
  public DimensionalFeedbackMotor<D> configurePositionController(DimensionalPIDFGains<D, Voltage> gains, Measure<D> izone) {
    getPositionController().setGains(gains);
    getPositionController().getErrorTracker().setIZone(izone);
    return this;
  }
  public DimensionalFeedbackMotor<D> setPosition(Measure<D> reference) {
      this.setTrackingMode(TrackingMode.kPosition);
      positionController.setReference(reference);
      return this;
  }
  public DimensionalFeedbackMotor<D> setPosition(Supplier<Measure<D>> reference_supplier) {
      this.setTrackingMode(TrackingMode.kPosition);
      positionController.setReference(reference_supplier);
      return this;
  }

  protected DimensionalPIDFController<Velocity<D>, Voltage> velocityController = new DimensionalPIDFController<Velocity<D>, Voltage>()
    .setStateSupplier(this::getVelocity);
  public DimensionalFeedbackMotor<D> setVelocityController(DimensionalPIDFController<Velocity<D>, Voltage> ctlr) {
    this.velocityController = ctlr;
    getVelocityController().setStateSupplier(this::getVelocity);
    return this;
  }
  public DimensionalPIDFController<Velocity<D>, Voltage> getVelocityController() { return velocityController; }
  public DimensionalFeedbackMotor<D> configureVelocityController(DimensionalPIDFGains<Velocity<D>, Voltage> gains) {
    getVelocityController().setGains(gains);
    return this;
  }
  public DimensionalFeedbackMotor<D> configureVelocityController(DimensionalPIDFGains<Velocity<D>, Voltage> gains, Measure<Velocity<D>> izone) {
    getVelocityController().setGains(gains);
    getVelocityController().getErrorTracker().setIZone(izone);
    return this;
  }
  public DimensionalFeedbackMotor<D> setVelocity(Measure<Velocity<D>> reference) {
      this.setTrackingMode(TrackingMode.kVelocity);
      velocityController.setReference(reference);
      return this;
  }
  public DimensionalFeedbackMotor<D> setVelocity(Supplier<Measure<Velocity<D>>> reference_supplier) {
      this.setTrackingMode(TrackingMode.kVelocity);
      velocityController.setReference(reference_supplier);
      return this;
  }

  // TODO[lib,later] use this for something, like...automating/overriding velocity ff?
  protected DimensionalDcMotorSpeedCurve<D> speedCurve;
  public DimensionalFeedbackMotor<D> setSpeedCurve(DimensionalDcMotorSpeedCurve<D> speedCurve) {
      this.speedCurve = speedCurve;
      return this;
  }

    protected Measure<Voltage> last_effort = Units.Volts.of(0);
    public Measure<Voltage> getLastControlEffort() { return last_effort; }
    protected DimensionalFeedbackMotor<D> setLastControlEffort(Measure<Voltage> last_effort) {
      this.last_effort = last_effort;
      return this;
    }

    // TODO[lib,later] reimplement
    //public Measure<Angle> mechanismToEncoder(Measure<D> mechanism) { return mechanism.divide(factor); }
    @SuppressWarnings({"unchecked"})
    public Measure<D> encoderToMechanism(Measure<Angle> encoder) { return (Measure<D>)encoder.times(factor); }

    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param minPosition
     * @return
     */
    public DimensionalFeedbackMotor<D> setMinPosition(Measure<D> minPosition) {
      getPositionController().getGains().setMinRef(minPosition);
      return this;
    }
    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param maxPosition
     * @return
     */
    public DimensionalFeedbackMotor<D> setMaxPosition(Measure<D> maxPosition) {
      getPositionController().getGains().setMaxRef(maxPosition);
      return this;
    }
    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param minVelocity
     * @return
     */
    public DimensionalFeedbackMotor<D> setMinVelocity(Measure<Velocity<D>> minVelocity) {
      getPositionController().getGains().setMinVel(minVelocity);
      getVelocityController().getGains().setMinRef(minVelocity);
      return this;
    }
    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param maxVelocity
     * @return
     */
    public DimensionalFeedbackMotor<D> setMaxVelocity(Measure<Velocity<D>> maxVelocity) {
      getPositionController().getGains().setMaxVel(maxVelocity);
      getVelocityController().getGains().setMaxRef(maxVelocity);
      return this;
    }
    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param peakVelocity
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public DimensionalFeedbackMotor<D> setSymmetricalVelocity(Measure<Velocity<D>> peakVelocity) {
      if (peakVelocity.lt((Measure<Velocity<D>>) Util.anyZero()))
        return setSymmetricalVelocity(peakVelocity.negate());
      setMinVelocity(peakVelocity.negate());
      setMaxVelocity(peakVelocity);
      return this;
    }
    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param minAcceleration
     * @return
     */
    public DimensionalFeedbackMotor<D> setMinAcceleration(Measure<Velocity<Velocity<D>>> minAcceleration) {
      getVelocityController().getGains().setMinVel(minAcceleration);
      return this;
    }
    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param maxAcceleration
     * @return
     */
    public DimensionalFeedbackMotor<D> setMaxAcceleration(Measure<Velocity<Velocity<D>>> maxAcceleration) {
      getVelocityController().getGains().setMaxVel(maxAcceleration);
      return this;
    }
    /**
     * This function must be called after using *FeedbackMotor::configure*Controller or DimensionalPIDFController::setGains.
     * Those functions will overwrite these changes.
     * @param peakAcceleration
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public DimensionalFeedbackMotor<D> setSymmetricalAcceleration(Measure<Velocity<Velocity<D>>> peakAcceleration) {
      if (peakAcceleration.lt((Measure<Velocity<Velocity<D>>>) Util.anyZero()))
        return setSymmetricalAcceleration(peakAcceleration.negate());
      setMinAcceleration(peakAcceleration.negate());
      setMaxAcceleration(peakAcceleration);
      return this;
    }

    private void track() {
      Measure<Voltage> input = Units.Volts.zero();
      if (getTrackingMode() != null)
      switch (getTrackingMode()) {
        case kPosition:
          input = positionController.calculate();
          break;
        case kVelocity:
          input = velocityController.calculate();
          break;
        default:
          break;
      }
      input = Util.clampV(input);
      setLastControlEffort(input);
      m.setVoltage(input.in(Units.Volts));
    }


    @Override
    public void onLoop() {
        receiveOptions();

        track();
        getWrappedMotorController().onLoop();

        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
      if (Misc.debug && name != null) {
        DimensionalPIDFController<?, Voltage> ctlr = null;
        if (getTrackingMode() != null)
        switch (getTrackingMode()) {
          case kPosition:
            ctlr = positionController;
            break;
          case kVelocity:
            ctlr = velocityController;
            break;
          default:
            ctlr = null;
            break;
        }
        if (ctlr != null) {
          double reference     = Util.fuzz() + ctlr.getReference().baseUnitMagnitude();
          double state         = Util.fuzz() + ctlr.getState().baseUnitMagnitude();
          double error         = Util.fuzz() + ctlr.getError().baseUnitMagnitude();
          double input         = Util.fuzz() + ctlr.calculate().baseUnitMagnitude();
          double inputPerState = Util.fuzz() + (input / state);
          SmartDashboard.putNumberArray(name + " all", new double[] { reference, state, error, input });
          SmartDashboard.putNumberArray(name + " rx", new double[] { reference, state });
          SmartDashboard.putNumberArray(name + " eu", new double[] { error, input });
          SmartDashboard.putNumber(name + " r", reference);
          SmartDashboard.putNumber(name + " x", state);
          SmartDashboard.putNumber(name + " e", error);
          SmartDashboard.putNumber(name + " u", input);
          SmartDashboard.putNumber(name + " uPerX", inputPerState);
        }
        }
    }

    @Override
    public void receiveOptions() {
        if (Misc.debug && name != null) {
            ////active_pid.setP(SmartDashboard.getNumber(name + "/p.set", active_pid.getP()));
            ////active_pid.setD(SmartDashboard.getNumber(name + "/d.set", active_pid.getD()));
            //double newSetpoint = SmartDashboard.getNumber(name + "/r.set", getReference());
            //if (getReference() != newSetpoint)
            //    setVelocity(newSetpoint);
        }
    }

}
