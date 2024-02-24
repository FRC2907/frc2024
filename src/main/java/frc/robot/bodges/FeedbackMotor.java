package frc.robot.bodges;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.subsystems.ISubsystem;

public interface FeedbackMotor extends MotorController, ISubsystem {

    public FeedbackMotor setFactor(double factor);
    public double get_position();
    public double get_velocity();

    /*
    private PIDController position_pid, velocity_pid, active_pid;
    private ArmFeedforward arm_ff;
    private ElevatorFeedforward elevator_ff;
    private double static_ff;
    private DoubleSupplier active_feedback;

    @Override
    public void setPosition(double reference) {
        setReference(reference, position_pid, this::position);
    }

    @Override
    public void setVelocity(double reference) {
        setReference(reference, velocity_pid, this::velocity);
    }

    private void setReference(double reference, PIDController pid, DoubleSupplier feedback) {
        this.reference = reference;
        this.active_pid = pid;
        this.active_feedback = feedback;
    }
    */

    public FeedbackMotor setPositionController(PIDController pid);
    public FeedbackMotor setVelocityController(PIDController pid);
    public FeedbackMotor setStaticFF(double ff);
    public FeedbackMotor setArmFF(ArmFeedforward ff);
    public FeedbackMotor setElevatorFF(ElevatorFeedforward ff);
    public FeedbackMotor setPositionHysteresis(double h);
    public FeedbackMotor setPositionHysteresis(double p, double v);
    public FeedbackMotor setVelocityHysteresis(double h);
    public FeedbackMotor setVelocityHysteresis(double v, double a);

    public FeedbackMotor set_position(double reference);
    public FeedbackMotor set_velocity(double reference);
    public double get_reference();
    public boolean atSetpoint();

    public double mechanism_to_encoder(double mechanism);
    public double encoder_to_mechanism(double encoder);
}
