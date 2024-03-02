package frc.robot.subsystems;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MotorControllers;

public class Arm implements ISubsystem {
    public final DimensionalFeedbackMotor<Angle> motor;

    private Arm(DimensionalFeedbackMotor<Angle> motor) {
        this.motor = motor;
    }

    private static Arm instance;
    public static Arm getInstance() {
        if (instance == null) {
            instance = new Arm(MotorControllers.arm());
        }
        return instance;
    }

    public void setPosition(Measure<Angle> reference) {
        motor.setPosition(reference);
    }

    public void setVelocity(Measure<Velocity<Angle>> reference) {
        motor.setVelocity(reference);
    }

    public boolean reachedSetPoint() {
        return motor.getPositionController().converged();
    }

    public void up() {
        setVelocity(GameInteractions.arm.kManualControlSpeed);
    }

    public void down() {
        setVelocity(GameInteractions.arm.kManualControlSpeed.negate());
    }

    public void hold() {
        setVelocity(Units.DegreesPerSecond.of(0));
    }

    public void startPosition() {
        setPosition(GameInteractions.arm.kStartPosition);
    }

    public void floorPosition() {
        setPosition(GameInteractions.arm.kFloorPosition);
    }

    public void holdingPosition() {
        setPosition(GameInteractions.arm.kHoldingPosition);
    }

    public void ampPosition() {
        setPosition(GameInteractions.arm.kAmpPosition);
    }

    public void speakerPosition() {
        setPosition(GameInteractions.arm.kSpeakerPosition);
    }

    public void climbReadyPosition() {
        setPosition(GameInteractions.arm.kClimbReadyPosition);
    }

    public void clumbPosition() {
        setPosition(GameInteractions.arm.kClimbClumbPosition);
    }

    public void selfRightingPosition() {
        setPosition(GameInteractions.arm.kSelfRightingPosition);
    }

    @Override
    public void onLoop() {
        motor.onLoop();
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putBoolean("arm.up", false);
        SmartDashboard.putBoolean("arm.down", false);
    }

    @Override
    public void receiveOptions() {
        if (SmartDashboard.getBoolean("arm.up", false))
            setPosition(motor.getPosition().plus(Units.Degrees.of(2)));
        if (SmartDashboard.getBoolean("arm.down", false))
            setPosition(motor.getPosition().minus(Units.Degrees.of(2)));
    }
}