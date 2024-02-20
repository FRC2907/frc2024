package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Arm implements ISubsystem {
    private Measure<Angle> setPoint;
    private CANSparkMax motor;

    private Arm(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setPositionConversionFactor(
            Control.arm.ARM_POS_PER_ENC_POS_UNIT.in(Units.Degrees.per(Units.Revolutions))
        );
        this.motor.getEncoder().setVelocityConversionFactor(
            Control.arm.ARM_VEL_PER_ENC_VEL_UNIT.in(Units.DegreesPerSecond.per(Units.RPM))
        );
        this.setPDGains(
            Control.arm.kP_pos.in(Units.Volts.per(Units.Degrees))
            , Control.arm.kD_pos.in(Units.Volts.per(Units.DegreesPerSecond))
        );
    }

    private static Arm instance;
    public static Arm getInstance() {
        if (instance == null) {
            CANSparkMax leftMotor = Util.createSparkGroup(Ports.can.arm.LEFTS);
            CANSparkMax rightMotor = Util.createSparkGroup(Ports.can.arm.RIGHTS);
            rightMotor.follow(leftMotor, true);
            instance = new Arm(leftMotor);
        }
        return instance;
    }

    public void setSetPoint(Measure<Angle> _setPoint) {
        this.setPoint = Util.clamp(Control.arm.kMinPosition, _setPoint, Control.arm.kMaxPosition);
    }
    public Measure<Angle> getSetPoint() {
        return this.setPoint;
    }
    public Measure<Angle> getPosition() {
        return Units.Degrees.of(this.motor.getEncoder().getPosition());
    }
    public Measure<Velocity<Angle>> getVelocity() {
        return Units.DegreesPerSecond.of(this.motor.getEncoder().getVelocity());
    }
    public Measure<Angle> getPositionError() {
        return getSetPoint().minus(getPosition());
    }
    public boolean reachedSetPoint() {
        return Util.checkHysteresis(getPositionError(), Control.arm.kPositionHysteresis)
            && Util.checkHysteresis(getVelocity(), Control.arm.kVelocityHysteresis)
        ;
    }


    public void up() {
        this.setSetPoint(setPoint.plus(Control.arm.kManualControlDiff));
    }

    public void down() {
        this.setSetPoint(setPoint.minus(Control.arm.kManualControlDiff));
    }


    // FIXME if we use this for position and velocity PD control
    // we might need a way to differentiate controllers or something, idk
    public void setPDGains(double P, double D) {
        this.motor.getPIDController().setP(P);
        this.motor.getPIDController().setD(D);
    }


    public void startPosition() {
        this.setSetPoint(Control.arm.kStartPosition);
    }
    public void floorPosition() {
        this.setSetPoint(Control.arm.kFloorPosition);
    }
    public void holdingPosition() {
        this.setSetPoint(Control.arm.kHoldingPosition);
    }
    public void ampPosition() {
        this.setSetPoint(Control.arm.kAmpPosition);
    }
    public void speakerPosition() {
        this.setSetPoint(Control.arm.kSpeakerPosition);
    }
    public void climbReadyPosition() {
        this.setSetPoint(Control.arm.kClimbReadyPosition);
    }
    public void clumbPosition() {
        this.setSetPoint(Control.arm.kClimbClumbPosition);
    }
    public void selfRightingPosition() {
        this.setSetPoint(Control.arm.kMaxPosition);
    }


    @Override
    public void onLoop() {
        // TODO let's look at adding a FF here if we can
        // maybe improve system step response
        // TODO work on motion profiling this / using velocity -> position control
        this.motor.getPIDController().setReference(this.setPoint.in(Units.Degrees), CANSparkMax.ControlType.kPosition);
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("arm.ref.position"    , getSetPoint()     .in(Units.Degrees));
        SmartDashboard.putNumber("arm.state.position"  , getPosition()     .in(Units.Degrees));
        SmartDashboard.putNumber("arm.ref.position.set", getSetPoint()     .in(Units.Degrees));
        SmartDashboard.putNumber("arm.err"             , getPositionError().in(Units.Degrees));
        SmartDashboard.putBoolean("arm.up"  , false);
        SmartDashboard.putBoolean("arm.down", false);
    }

    @Override
    public void receiveOptions() {
        setSetPoint(
            Units.Degrees.of(
                SmartDashboard.getNumber("arm.ref.position.set"
                    , getSetPoint().in(Units.Degrees)
                )
            )
        );
        if (SmartDashboard.getBoolean("arm.up", false))
            this.up();
        if (SmartDashboard.getBoolean("arm.down", false))
            this.down();
    }
}