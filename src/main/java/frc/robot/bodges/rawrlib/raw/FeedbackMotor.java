package frc.robot.bodges.rawrlib.raw;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.MechanismConstraints;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

/**
 * Describes a generic motor with closed-loop position and velocity control.
 */
public abstract class FeedbackMotor implements MotorController, ISubsystem {

    protected PIDController position_pid, velocity_pid, active_pid;
    protected ArmFeedforward arm_ff = new ArmFeedforward(0, 0, 0);
    protected ElevatorFeedforward elevator_ff = new ElevatorFeedforward(0, 0, 0);
    protected double static_ff = 0.0, factor = 1.0, last_effort = 0.0;
    protected DoubleSupplier active_feedback;
    protected String name;
    protected double inputLimit_lower = MechanismConstraints.electrical.kMaxVoltage.negate().in(Units.Volts);
    protected double inputLimit_upper = MechanismConstraints.electrical.kMaxVoltage.in(Units.Volts);
    protected DcMotorSpeedCurve speedCurve = new DcMotorSpeedCurve(0, 1);

    public FeedbackMotor setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() { return name; }

    public FeedbackMotor setInverted_(boolean isInverted) {
        this.setInverted(isInverted);
        return this;
    }

    public FeedbackMotor setFactor(double factor) {
        this.factor = factor;
        return setFactor_downstream(factor);
    }
    protected abstract FeedbackMotor setFactor_downstream(double factor);

    public abstract double getPosition();
    public abstract double getVelocity();

    public FeedbackMotor setPosition(double reference) {
        setReference(reference, position_pid, this::getPosition);
        return this;
    }

    public FeedbackMotor setVelocity(double reference) {
        setReference(reference, velocity_pid, this::getVelocity);
        return this;
    }

    private FeedbackMotor setReference(double reference, PIDController pid, DoubleSupplier feedback) {
        if (active_pid != pid)
            this.active_pid = pid;
        if (active_feedback != feedback)
            this.active_feedback = feedback;
        setReference(reference);
        return this;
    }

    /**
     * Update the setpoint of the active PID controller profile without reassigning the controller.
     * @param reference New setpoint
     * @return FeedbackMotor on which this method was called, for chaining
     */
    private FeedbackMotor setReference(double reference) {
        this.active_pid.setSetpoint(reference);
        return this;
    }

    public FeedbackMotor setPositionController(PIDController pid) {
        this.position_pid = pid;
        return this;
    }

    public FeedbackMotor setVelocityController(PIDController pid) {
        this.velocity_pid = pid;
        return this;
    }

    public FeedbackMotor setStaticFF(double ff) {
        this.static_ff = ff;
        return this;
    }

    public FeedbackMotor setArmFF(ArmFeedforward ff) {
        this.arm_ff = ff;
        return this;
    }

    public FeedbackMotor setElevatorFF(ElevatorFeedforward ff) {
        this.elevator_ff = ff;
        return this;
    }

    public FeedbackMotor setPositionHysteresis(double h) {
        position_pid.setTolerance(h);
        return this;
    }

    public FeedbackMotor setPositionHysteresis(double p, double v) {
        position_pid.setTolerance(p, v);
        return this;
    }

    public FeedbackMotor setVelocityHysteresis(double h) {
        velocity_pid.setTolerance(h);
        return this;
    }

    public FeedbackMotor setVelocityHysteresis(double v, double a) {
        velocity_pid.setTolerance(v, a);
        return this;
    }

    public FeedbackMotor setControlEffortLimit(double limit) {
        return setControlEffortLimit(-limit, limit);
    }

    public FeedbackMotor setControlEffortLimit(double limit_lower, double limit_upper) {
        if (limit_upper > limit_lower) return setControlEffortLimit(limit_upper, limit_lower);
        this.inputLimit_lower = Util.clampV(limit_lower);
        this.inputLimit_upper = Util.clampV(limit_upper);
        return this;
    }

    public FeedbackMotor setSpeedCurve(DcMotorSpeedCurve speedCurve) {
        this.speedCurve = speedCurve;
        return this;
    }

    public double getReference() { return active_pid.getSetpoint(); }

    public double getState() { return active_feedback.getAsDouble(); }

    public double getError() { return getReference() - getState(); }

    public double getLastControlEffort() { return last_effort; }

    public boolean atSetpoint() { return active_pid.atSetpoint(); }

    public boolean trackingPosition() { return active_pid == position_pid; }
    public boolean trackingVelocity() { return active_pid == velocity_pid; }

    public double mechanismToEncoder(double mechanism) { return mechanism / factor; }

    public double encoderToMechanism(double encoder) { return encoder * factor; }

    private void runPID() {
        double output = static_ff;
        if (trackingVelocity()) output += speedCurve.getVoltage(getReference());
        output += active_pid.calculate(getState());
        output = Util.clamp(inputLimit_lower, output, inputLimit_upper);
        this.last_effort = output;
        this.setVoltage(output);
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

        runPID();
        //this.setVoltage(active_pid.getSetpoint());

        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
        if (name != null) {
            SmartDashboard.putNumberArray(name + "/refstate", new double[] {getReference(), getState()});
            SmartDashboard.putNumberArray(name + "/errinput", new double[] {getError(), getLastControlEffort()});
            //SmartDashboard.putNumber(name + "/p.set", active_pid.getP());
            //SmartDashboard.putNumber(name + "/d.set", active_pid.getD());
            SmartDashboard.putNumber(name + "/r.set", getReference());
            SmartDashboard.putNumber(name + "/voltPerVel", getLastControlEffort() / getVelocity());
        }
    }

    @Override
    public void receiveOptions() {
        if (name != null) {
            //active_pid.setP(SmartDashboard.getNumber(name + "/p.set", active_pid.getP()));
            //active_pid.setD(SmartDashboard.getNumber(name + "/d.set", active_pid.getD()));
            double newSetpoint = SmartDashboard.getNumber(name + "/r.set", getReference());
            if (getReference() != newSetpoint)
                setVelocity(newSetpoint);
        }
    }

}
