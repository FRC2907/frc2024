package frc.robot.subsystems;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.FeedbackMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MotorControllers;
import frc.robot.util.Util;

public class Arm implements ISubsystem {
    private Measure<Angle> setPoint_position;
    private Measure<Velocity<Angle>> setPoint_velocity;
    public final FeedbackMotor motor;

    private Arm(FeedbackMotor motor) {
        this.motor = motor;
    }

    private static Arm instance;

    public static Arm getInstance() {
        if (instance == null) {
            instance = new Arm(MotorControllers.arm());
        }
        return instance;
    }

    public void setSetPoint_position(Measure<Angle> _setPoint) {
        setPoint_position = Util.clamp(MechanismConstraints.arm.kMinPosition, _setPoint,
                MechanismConstraints.arm.kMaxPosition);
    }

    public Measure<Angle> getSetPoint_position() {
        return setPoint_position;
    }

    public Measure<Angle> getPosition() {
        return Units.Degrees.of(motor.getPosition());
    }

    public Measure<Angle> getPositionError() {
        return getSetPoint_position().minus(getPosition());
    }

    public void setSetPoint_velocity(Measure<Velocity<Angle>> _setPoint) {
        setPoint_velocity = Util.clampSymmetrical(_setPoint, MechanismConstraints.arm.kMaxVelocity);
    }

    public Measure<Velocity<Angle>> getSetPoint_velocity() {
        return setPoint_velocity;
    }

    public Measure<Velocity<Angle>> getVelocity() {
        return Units.DegreesPerSecond.of(motor.getVelocity());
    }

    public Measure<Velocity<Angle>> getVelocityError() {
        return getSetPoint_velocity().minus(getVelocity());
    }

    public boolean reachedSetPoint() {
        return motor.atSetpoint();
    }

    public void up() {
        setSetPoint_velocity(GameInteractions.arm.kManualControlSpeed);
    }

    public void down() {
        setSetPoint_velocity(GameInteractions.arm.kManualControlSpeed.negate());
    }

    public void hold() {
        setSetPoint_velocity(Units.DegreesPerSecond.of(0));
    }

    public void startPosition() {
        setSetPoint_position(GameInteractions.arm.kStartPosition);
    }

    public void floorPosition() {
        setSetPoint_position(GameInteractions.arm.kFloorPosition);
    }

    public void holdingPosition() {
        setSetPoint_position(GameInteractions.arm.kHoldingPosition);
    }

    public void ampPosition() {
        setSetPoint_position(GameInteractions.arm.kAmpPosition);
    }

    public void speakerPosition() {
        setSetPoint_position(GameInteractions.arm.kSpeakerPosition);
    }

    public void climbReadyPosition() {
        setSetPoint_position(GameInteractions.arm.kClimbReadyPosition);
    }

    public void clumbPosition() {
        setSetPoint_position(GameInteractions.arm.kClimbClumbPosition);
    }

    public void selfRightingPosition() {
        setSetPoint_position(GameInteractions.arm.kSelfRightingPosition);
    }

    @Override
    public void onLoop() {
        // TODO work on motion profiling this / using velocity -> position control
        motor.setPosition(setPoint_position.in(Units.Degrees));
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("arm.ref.position", getSetPoint_position().in(Units.Degrees));
        SmartDashboard.putNumber("arm.state.position", getPosition().in(Units.Degrees));
        SmartDashboard.putNumber("arm.ref.position.set", getSetPoint_position().in(Units.Degrees));
        SmartDashboard.putNumber("arm.err", getPositionError().in(Units.Degrees));
        SmartDashboard.putBoolean("arm.up", false);
        SmartDashboard.putBoolean("arm.down", false);
    }

    @Override
    public void receiveOptions() {
        setSetPoint_position(
                Units.Degrees.of(
                        SmartDashboard.getNumber("arm.ref.position.set", getSetPoint_position().in(Units.Degrees))));
        if (SmartDashboard.getBoolean("arm.up", false))
            setSetPoint_position(getPosition().plus(Units.Degrees.of(2)));
        if (SmartDashboard.getBoolean("arm.down", false))
            setSetPoint_position(getPosition().minus(Units.Degrees.of(2)));
    }
}