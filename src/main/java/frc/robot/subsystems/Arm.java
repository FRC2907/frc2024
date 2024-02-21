package frc.robot.subsystems;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.sillycontroller.SmartMotorController_Angular;
import frc.robot.constants.Control;
import frc.robot.constants.MotorControllers;
import frc.robot.util.Util;

public class Arm implements ISubsystem {
    private Measure<Angle> setPoint_position;
    private Measure<Velocity<Angle>> setPoint_velocity;
    private SmartMotorController_Angular motor;

    private Arm(SmartMotorController_Angular _motor) {
        this.motor = _motor;
    }

    private static Arm instance;
    public static Arm getInstance() {
        if (instance == null) {
            instance = new Arm(MotorControllers.arm);
        }
        return instance;
    }

    public void setSetPoint_position(Measure<Angle> _setPoint) {
        setPoint_position = Util.clamp(Control.arm.kMinPosition, _setPoint, Control.arm.kMaxPosition);
    }
    public Measure<Angle> getSetPoint_position() {
        return setPoint_position;
    }
    public Measure<Angle> getPosition() {
        return motor.getPosition();
    }
    public Measure<Angle> getPositionError() {
        return getSetPoint_position().minus(getPosition());
    }

    public void setSetPoint_velocity(Measure<Velocity<Angle>> _setPoint) {
        setPoint_velocity = Util.clampSymmetrical(_setPoint, Control.arm.kMaxVelocity);
    }
    public Measure<Velocity<Angle>> getSetPoint_velocity() {
        return setPoint_velocity;
    }
    public Measure<Velocity<Angle>> getVelocity() {
        return motor.getVelocity();
    }
    public Measure<Velocity<Angle>> getVelocityError() {
        return getSetPoint_velocity().minus(getVelocity());
    }

    public boolean reachedSetPoint() {
        return Util.checkHysteresis(getPositionError(), Control.arm.kPositionHysteresis)
            && Util.checkHysteresis(getVelocity(), Control.arm.kVelocityHysteresis)
        ;
    }


    // TODO look at moving these to velocity instead of incremental position
    public void up() {
        setSetPoint_position(setPoint_position.plus(Control.arm.kManualControlDiff));
    }

    public void down() {
        setSetPoint_position(setPoint_position.minus(Control.arm.kManualControlDiff));
    }

    public void hold() {
        setSetPoint_velocity(Units.DegreesPerSecond.of(0));
    }


    public void startPosition() {
        setSetPoint_position(Control.arm.kStartPosition);
    }
    public void floorPosition() {
        setSetPoint_position(Control.arm.kFloorPosition);
    }
    public void holdingPosition() {
        setSetPoint_position(Control.arm.kHoldingPosition);
    }
    public void ampPosition() {
        setSetPoint_position(Control.arm.kAmpPosition);
    }
    public void speakerPosition() {
        setSetPoint_position(Control.arm.kSpeakerPosition);
    }
    public void climbReadyPosition() {
        setSetPoint_position(Control.arm.kClimbReadyPosition);
    }
    public void clumbPosition() {
        setSetPoint_position(Control.arm.kClimbClumbPosition);
    }
    public void selfRightingPosition() {
        setSetPoint_position(Control.arm.kMaxPosition);
    }


    @Override
    public void onLoop() {
        // TODO work on motion profiling this / using velocity -> position control
        motor.setPosition(setPoint_position);
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber ("arm.ref.position"    , getSetPoint_position()     .in(Units.Degrees));
        SmartDashboard.putNumber ("arm.state.position"  , getPosition()     .in(Units.Degrees));
        SmartDashboard.putNumber ("arm.ref.position.set", getSetPoint_position()     .in(Units.Degrees));
        SmartDashboard.putNumber ("arm.err"             , getPositionError().in(Units.Degrees));
        SmartDashboard.putBoolean("arm.up"  , false);
        SmartDashboard.putBoolean("arm.down", false);
    }

    @Override
    public void receiveOptions() {
        setSetPoint_position(
            Units.Degrees.of(
                SmartDashboard.getNumber("arm.ref.position.set"
                    , getSetPoint_position().in(Units.Degrees)
                )
            )
        );
        if (SmartDashboard.getBoolean("arm.up", false))
            up();
        if (SmartDashboard.getBoolean("arm.down", false))
            down();
    }
}