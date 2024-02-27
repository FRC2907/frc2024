package frc.robot.subsystems;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.FeedbackMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MotorControllers;

public class Intake implements ISubsystem {
    private Measure<Velocity<Distance>> setPoint;
    private FeedbackMotor motor;

    private Intake(FeedbackMotor _motor) {
        this.motor = _motor;
    }

    private static Intake instance;
    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake(MotorControllers.intake());
        }
        return instance;
    }


    public void setSetPoint(Measure<Velocity<Distance>> _setPoint) {
        setPoint = _setPoint;
    }
    public Measure<Velocity<Distance>> getSetPoint() {
        return setPoint;
    }
    public Measure<Velocity<Distance>> getVelocity() {
        return Units.MetersPerSecond.of(motor.getVelocity());
    }
    public Measure<Velocity<Distance>> getError() {
        return getSetPoint().minus(getVelocity());
    }


    public void intake() {
        setSetPoint(GameInteractions.intake.kIntakingSpeed);
    }
    public void outake() {
        setSetPoint(GameInteractions.intake.kOutakingSpeed);
    }
    public void off() {
        setSetPoint(GameInteractions.intake.kOff);
    }


    /** Return whether the intake has a Note in it. */
    public boolean hasNote() {
        // TODO read sensor to determine whether there's a Note in the intake
        return false;
    }


    /** Update motor speed every cycle. */
    @Override
    public void onLoop() {
        motor.setVelocity(setPoint.in(Units.MetersPerSecond));
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