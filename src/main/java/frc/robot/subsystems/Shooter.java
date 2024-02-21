package frc.robot.subsystems;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.sillycontroller.SmartMotorController_Linear;
import frc.robot.constants.Control;
import frc.robot.constants.MotorControllers;

public class Shooter implements ISubsystem {
    private Measure<Velocity<Distance>> setPoint;
    private SmartMotorController_Linear motor;

    private Shooter(SmartMotorController_Linear _motor) {
        this.motor = _motor;
    }

    private static Shooter instance;
    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter(MotorControllers.shooter);
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
        return motor.getVelocity();
    }
    public Measure<Velocity<Distance>> getError() {
        return getSetPoint().minus(getVelocity());
    }


    public void amp() {
        setSetPoint(Control.shooter.kAmpSpeed);
    }
    public void speaker() {
        setSetPoint(Control.shooter.kSpeakerSpeed);
    }
    public void off() {
        setSetPoint(Control.shooter.kOff);
    }


    public boolean noteScored() {
        return false;
        // TODO add code to see if note has scored
        // probably look for a falling edge on Intake::hasNote while in a scoring state
    }


    /** Update motor speed every cycle. */
    @Override
    public void onLoop() {
        motor.setVelocity(setPoint);
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