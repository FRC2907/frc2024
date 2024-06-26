package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.stuff.AWheeMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MechanismDimensions;
import frc.robot.constants.MotorControllers;
import frc.robot.game_elements.FieldElements;

public class Arm implements ISubsystem {
    public final AWheeMotor<Angle> motor;

    private Arm(AWheeMotor<Angle> motor) {
        this.motor = motor;
    }

    private static Arm instance;

    public static Arm getInstance() {
        if (instance == null) {
            instance = new Arm(MotorControllers.arm());
        }
        return instance;
    }

    public Translation3d getPivot() {
        Translation2d robotPose = Drivetrain.getInstance().getPose().getTranslation();
        return MechanismDimensions.arm.PIVOT.plus(new Translation3d(robotPose.getX(), robotPose.getY(), 0));
    }

    public boolean reachedSetPoint() {
        return motor.checkHysteresis();
    }

    public void up() {
        motor.setVelocity(GameInteractions.arm.kManualControlSpeed);
    }

    public void down() {
        motor.setVelocity(GameInteractions.arm.kManualControlSpeed.negate());
    }

    public void hold() {
        motor.setVelocity(Units.DegreesPerSecond.of(0.0));
    }

    public void startPosition() {
        motor.setPosition(GameInteractions.arm.kStartPosition);
    }

    public void floor() {
        motor.setPosition(GameInteractions.arm.kFloorPosition);
    }

    public void holding() {
        motor.setPosition(GameInteractions.arm.kHoldingPosition);
    }

    public void amp() {
        motor.setPosition(GameInteractions.arm.kAmpPosition);
    }

    public void speaker() {
        double airDistance = FieldElements.getFieldPoints().kSpeakerHole.getDistance(getPivot());
        double floorDistance = FieldElements.getFieldPoints().kSpeakerHole.minus(getPivot()).toTranslation2d()
                .getNorm();
        Measure<Angle> angle = Units.Radians.of(Math.acos(floorDistance / airDistance)); // cosine is adj/hyp
        motor.setPosition(angle);
    }

    public void climbReady() {
        motor.setPosition(GameInteractions.arm.kClimbReadyPosition);
    }

    public void clumb() {
        motor.setPosition(GameInteractions.arm.kClimbClumbPosition);
    }

    public void selfRighting() {
        motor.setPosition(GameInteractions.arm.kSelfRightingPosition);
    }

    public void off() {
        motor.setVelocity(MechanismConstraints.arm.kOff);
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putBoolean("arm/up", false);
        SmartDashboard.putBoolean("arm/down", false);
    }

    @Override
    public void receiveOptions() {
        if (SmartDashboard.getBoolean("arm/up", false))
            motor.setPosition(motor.getPosition().plus(Units.Degrees.of(2.0)));
        if (SmartDashboard.getBoolean("arm/down", false))
            motor.setPosition(motor.getPosition().minus(Units.Degrees.of(2.0)));
    }
}