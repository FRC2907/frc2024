package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Intake implements ISubsystem {
    private Measure<Velocity<Distance>> setPoint;
    private CANSparkMax motor;

    private Intake(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setVelocityConversionFactor(
            Control.intake.LINEAR_VEL_PER_ENC_VEL_UNIT.in(Units.MetersPerSecond.per(Units.RPM))
        );
    }

    private static Intake instance;
    public static Intake getInstance() {
        if (instance == null) {
            CANSparkMax motor = Util.createSparkGroup(Ports.can.intake.MOTORS);
            instance = new Intake(motor);
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


    public void intake() {
        this.setSetPoint(Control.intake.kIntakingSpeed);
    }
    public void outake() {
        this.setSetPoint(Control.intake.kOutakingSpeed);
    }
    public void off() {
        this.setSetPoint(Control.intake.kOff);
    }


    /** Return whether the intake has a Note in it. */
    public boolean hasNote() {
        // TODO read sensor to determine whether there's a Note in the intake
        return false;
    }


    /** Update motor speed every cycle. */
    @Override
    public void onLoop() {
        this.motor.getPIDController().setReference(this.setPoint.in(Units.MetersPerSecond), CANSparkMax.ControlType.kVelocity);
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("intake.velocity"    , getVelocity().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("intake.setpoint"    , getSetPoint().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("intake.setpoint.set", getSetPoint().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("intake.error"       , getError()   .in(Units.MetersPerSecond));
    }

    @Override
    public void receiveOptions() {
        setSetPoint(
            Units.MetersPerSecond.of(
                SmartDashboard.getNumber("intake.setpoint.set"
                , getSetPoint().in(Units.MetersPerSecond))
            )
        );
    }

}
