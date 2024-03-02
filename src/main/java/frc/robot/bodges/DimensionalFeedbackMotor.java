package frc.robot.bodges;

import edu.wpi.first.units.*;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

/**
 * Describes a generic motor with closed-loop position and velocity control.
 */
public abstract class DimensionalFeedbackMotor<D extends Unit<D>> implements ISubsystem {

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


    // FIXME may have to degeneric
    @SuppressWarnings("unchecked")
    public Measure<D> getPosition() {
      return (Measure<D>) m.getPosition_downstream().times(factor);
    }

    // FIXME may have to degeneric
    @SuppressWarnings("unchecked")
    public Measure<Velocity<D>> getVelocity() {
      return (Measure<Velocity<D>>) m.getVelocity_downstream().times(factor);
    }

  protected DimensionalPIDFController<D, Voltage> positionController = new DimensionalPIDFController<D, Voltage>()
    .setStateSupplier(this::getPosition);
  public DimensionalPIDFController<D, Voltage> getPositionController() { return positionController; }
  public abstract DimensionalFeedbackMotor<D> setPositionController(DimensionalPIDFController<D, Voltage> ctlr);
  public abstract DimensionalFeedbackMotor<D> configurePositionController(DimensionalPIDFGains<D, Voltage> gains);
  public abstract DimensionalFeedbackMotor<D> configurePositionController(DimensionalPIDFGains<D, Voltage> gains, Measure<D> izone);
  public DimensionalFeedbackMotor<D> setPosition(Measure<D> reference) {
      this.setTrackingMode(TrackingMode.kPosition);
      positionController.setReference(reference);
      return this;
  }

  protected DimensionalPIDFController<Velocity<D>, Voltage> velocityController = new DimensionalPIDFController<Velocity<D>, Voltage>()
    .setStateSupplier(this::getVelocity);
  public abstract DimensionalFeedbackMotor<D> setVelocityController(DimensionalPIDFController<Velocity<D>, Voltage> ctlr);
  public DimensionalPIDFController<Velocity<D>, Voltage> getVelocityController() { return velocityController; }
  public abstract DimensionalFeedbackMotor<D> configureVelocityController(DimensionalPIDFGains<Velocity<D>, Voltage> gains);
  public abstract DimensionalFeedbackMotor<D> configureVelocityController(DimensionalPIDFGains<Velocity<D>, Voltage> gains, Measure<Velocity<D>> izone);
  public DimensionalFeedbackMotor<D> setVelocity(Measure<Velocity<D>> reference) {
      this.setTrackingMode(TrackingMode.kVelocity);
      velocityController.setReference(reference);
      return this;
  }

  // TODO use this for something, like...automating/overriding velocity ff?
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

    // TODO reimplement
    //public Measure<Angle> mechanismToEncoder(Measure<D> mechanism) { return mechanism.divide(factor); }
    @SuppressWarnings({"unchecked"})
    public Measure<D> encoderToMechanism(Measure<Angle> encoder) { return (Measure<D>)encoder.times(factor); }

    private void track() {
      Measure<Voltage> input = Units.Volts.zero();
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

    /*
    private void runProfile(MotionProfile profile) {
        setProfile(profile);
        this.profile.start();
        runProfile();
    }

    private void runProfile() {
        if (profile == null) return;
        if (profile.isDone()) {
            setPosition(profile.get().position);
            System.out.println("== End of profile ==");
            System.out.println(profile.now() + "s of " + profile.length() + " s");
            profile = null;
            return;
        }
        setVelocity(profile.get().velocity);
    }
    */

    @Override
    public void onLoop() {
        receiveOptions();

        track();

        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
        if (name != null) {
            //SmartDashboard.putNumberArray(name + "/refstate", new double[] {velocityController.getReference().in(D.per(Units.Second)), getState()});
            //SmartDashboard.putNumberArray(name + "/errinput", new double[] {getError(), getLastControlEffort()});
            ////SmartDashboard.putNumber(name + "/p.set", active_pid.getP());
            ////SmartDashboard.putNumber(name + "/d.set", active_pid.getD());
            //SmartDashboard.putNumber(name + "/r.set", getReference());
            //SmartDashboard.putNumber(name + "/voltPerVel", getLastControlEffort() / getVelocity());
        }
    }

    @Override
    public void receiveOptions() {
        if (name != null) {
            ////active_pid.setP(SmartDashboard.getNumber(name + "/p.set", active_pid.getP()));
            ////active_pid.setD(SmartDashboard.getNumber(name + "/d.set", active_pid.getD()));
            //double newSetpoint = SmartDashboard.getNumber(name + "/r.set", getReference());
            //if (getReference() != newSetpoint)
            //    setVelocity(newSetpoint);
        }
    }

}
