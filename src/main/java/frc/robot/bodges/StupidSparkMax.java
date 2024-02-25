package frc.robot.bodges;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.util.Util;

public class StupidSparkMax extends CANSparkMax implements FeedbackMotor {

    public StupidSparkMax(int deviceId, MotorType type) {
        super(deviceId, type);
    }

    @Override
    public FeedbackMotor setFactor(double factor) {
        this.factor = factor;
        this.getEncoder().setPositionConversionFactor(factor);
        this.getEncoder().setVelocityConversionFactor(factor);
        return this;
    }

    @Override
    public double get_position() {
        return this.getEncoder().getPosition();
    }

    @Override
    public double get_velocity() {
        return this.getEncoder().getVelocity();
    }

    private PIDController position_pid, velocity_pid, active_pid;
    @SuppressWarnings({"unused"})
    private ArmFeedforward arm_ff = new ArmFeedforward(0, 0, 0);
    @SuppressWarnings({"unused"})
    private ElevatorFeedforward elevator_ff = new ElevatorFeedforward(0, 0, 0);
    private double static_ff = 0.0, factor = 1.0, last_effort = 0.0;
    private DoubleSupplier active_feedback;

    @Override
    public FeedbackMotor setPositionController(PIDController pid) {
        this.position_pid = pid;
        return this;
    }

    @Override
    public FeedbackMotor setVelocityController(PIDController pid) {
        this.velocity_pid = pid;
        return this;
    }

    @Override
    public FeedbackMotor setStaticFF(double ff) {
        this.static_ff = ff;
        return this;
    }

    @Override
    public FeedbackMotor setArmFF(ArmFeedforward ff) {
        this.arm_ff = ff;
        return this;
    }

    @Override
    public FeedbackMotor setElevatorFF(ElevatorFeedforward ff) {
        this.elevator_ff = ff;
        return this;
    }

    @Override
    public FeedbackMotor set_position(double reference) {
        setReference(reference, position_pid, this::get_position);
        return this;
    }

    @Override
    public FeedbackMotor set_velocity(double reference) {
        setReference(reference, velocity_pid, this::get_velocity);
        return this;
    }

    private FeedbackMotor setReference(double reference, PIDController pid, DoubleSupplier feedback) {
        this.active_pid = pid;
        this.active_pid.setSetpoint(reference);
        this.active_feedback = feedback;
        return this;
    }

    @Override
    public double get_reference() {
        return active_pid.getSetpoint();
    }

    @Override
    public double get_error() {
        return active_pid.getPositionError();
    }

    @Override
    public double get_lastControlEffort() { return last_effort; }

    @Override
    public void onLoop() {
        double output = static_ff + active_pid.calculate(active_feedback.getAsDouble());
        output = Util.clampV(output);
        this.last_effort = output;
        this.setVoltage(output);
    }

    @Override
    public void submitTelemetry() {
    }

    @Override
    public void receiveOptions() {
    }

    @Override
    public double mechanism_to_encoder(double mechanism) {
        return mechanism / factor;
    }

    @Override
    public double encoder_to_mechanism(double encoder) {
        return encoder * factor;
    }

    @Override
    public FeedbackMotor setPositionHysteresis(double h) {
        position_pid.setTolerance(h);
        return this;
    }

    @Override
    public FeedbackMotor setPositionHysteresis(double p, double v) {
        position_pid.setTolerance(p, v);
        return this;
    }

    @Override
    public FeedbackMotor setVelocityHysteresis(double h) {
        velocity_pid.setTolerance(h);
        return this;
    }

    @Override
    public FeedbackMotor setVelocityHysteresis(double v, double a) {
        velocity_pid.setTolerance(v, a);
        return this;
    }

    @Override
    public boolean atSetpoint() {
        return active_pid.atSetpoint();
    }

}
