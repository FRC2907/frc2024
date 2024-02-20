package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Shooter implements ISubsystem {
    private Measure<Velocity<Distance>> setPoint;
    private CANSparkMax motor;

    private Shooter(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setVelocityConversionFactor(
            Control.shooter.LINEAR_VEL_PER_ENC_VEL_UNIT.in(Units.MetersPerSecond.per(Units.RPM))
        );
    }

    private static Shooter instance;
    public static Shooter getInstance() {
        if (instance == null) {
            CANSparkMax leftMotor = Util.createSparkGroup(Ports.can.shooter.LEFTS);
            CANSparkMax rightMotor = Util.createSparkGroup(Ports.can.shooter.RIGHTS);
            rightMotor.follow(leftMotor, true);
            instance = new Shooter(leftMotor);
        }
        return instance;
    }


    public void setSetPoint(Measure<Velocity<Distance>> _setPoint) {
        this.setPoint = _setPoint;
    }
    public Measure<Velocity<Distance>> getSetPoint() {
        return this.setPoint;
    }
    public Measure<Velocity<Distance>> getVelocity() {
        return Units.MetersPerSecond.of(this.motor.getEncoder().getVelocity());
    }
    public Measure<Velocity<Distance>> getError() {
        return getSetPoint().minus(getVelocity());
    }


    public void amp() {
        this.setSetPoint(Control.shooter.kAmpSpeed);
    }
    public void shooter() {
        this.setSetPoint(Control.shooter.kSpeakerSpeed);
    }
    public void off() {
        this.setSetPoint(Control.shooter.kOff);
    }


    public boolean noteScored() {
        return false;
        // TODO add code to see if note has scored
        // probably look for a falling edge on Intake::hasNote while in a scoring state
    }


    /** Update motor speed every cycle. */
    @Override
    public void onLoop() {
        this.motor.getPIDController().setReference(this.setPoint.in(Units.MetersPerSecond), CANSparkMax.ControlType.kVelocity);

    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("shooter.velocity"    , getVelocity().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("shooter.setpoint"    , getSetPoint().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("shooter.setpoint.set", getSetPoint().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("shooter.error"       , getError()   .in(Units.MetersPerSecond));
    }

    @Override
    public void receiveOptions() {
        setSetPoint(
            Units.MetersPerSecond.of(
                SmartDashboard.getNumber("shooter.setpoint.set"
                    , getSetPoint().in(Units.MetersPerSecond))
            )
        );
    }

}
