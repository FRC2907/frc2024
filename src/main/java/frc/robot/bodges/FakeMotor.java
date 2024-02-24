package frc.robot.bodges;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;

public class FakeMotor implements FeedbackMotor {

    private boolean isInverted = false;
    private double speed = 0.0;
    private double reference = 0.0;

    @Override
    public void set(double speed) {
        this.speed = speed;
    }

    @Override
    public double get() {
        return speed;
    }

    @Override
    public void setInverted(boolean isInverted) {
        this.isInverted = isInverted;
    }

    @Override
    public boolean getInverted() {
        return isInverted;
    }

    @Override
    public void disable() {
    }

    @Override
    public void stopMotor() {
    }

    @Override
    public FeedbackMotor setFactor(double factor) {
        return this;
    }

    @Override
    public double get_position() {
        return 0.0;
    }

    @Override
    public double get_velocity() {
        return 0.0;
    }

    @Override
    public FeedbackMotor setPositionController(PIDController pid) { return this; }

    @Override
    public FeedbackMotor setVelocityController(PIDController pid) { return this; }

    @Override
    public FeedbackMotor setStaticFF(double ff) { return this; }

    @Override
    public FeedbackMotor setArmFF(ArmFeedforward ff) { return this; }

    @Override
    public FeedbackMotor setElevatorFF(ElevatorFeedforward ff) { return this; }

    @Override
    public FeedbackMotor set_position(double reference) { this.reference = reference; return this; }

    @Override
    public FeedbackMotor set_velocity(double reference) { this.reference = reference; return this; }

    @Override
    public double get_reference() { return reference; }

    @Override
    public void onLoop() { }

    @Override
    public void submitTelemetry() { }

    @Override
    public void receiveOptions() { }

    @Override
    public double mechanism_to_encoder(double mechanism) { return 0.0; }

    @Override
    public double encoder_to_mechanism(double encoder) { return 0.0; }

    @Override
    public FeedbackMotor setPositionHysteresis(double h) { return this; }

    @Override
    public FeedbackMotor setPositionHysteresis(double p, double v) { return this; }

    @Override
    public FeedbackMotor setVelocityHysteresis(double h) { return this; }

    @Override
    public FeedbackMotor setVelocityHysteresis(double v, double a) { return this; }

    @Override
    public boolean atSetpoint() { return true; }

}
