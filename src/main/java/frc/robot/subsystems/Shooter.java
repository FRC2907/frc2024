package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

// TODO consider converging the Shooter and Intake classes into a single Flywheel class 
// TODO further consider generalizing flywheels, arms, and elevators into generic classes
// and then extending them for whatever (summer stuff probs)
public class Shooter implements ISubsystem {
    private double setPoint; // wheel m/s
    private CANSparkMax motor;

    private Shooter(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setVelocityConversionFactor(Control.shooter.METER_PER_SECOND_PER_ENC_VEL_UNIT);
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

    public void off() {
        this.setSetPoint(Control.shooter.kOff);
    }
    public void amp() {
        this.setSetPoint(Control.shooter.kAmpSpeed);
    }
    public void shooter() {
        this.setSetPoint(Control.shooter.kSpeakerSpeed);
    }

    public boolean noteScored() {
        return false;
        // TODO add code to see if note has scored
    }


    /** Set the desired speed of the shooter in wheel m/s. */
    public void setSetPoint(double _setPoint) {
        this.setPoint = _setPoint;
    }
    public double getSetPoint() {
        return this.setPoint;
    }
    /** Return shooter speed in wheel m/s. */
    public double getVelocity() {
        return this.motor.getEncoder().getVelocity();
    }
    public double getError() {
        return getSetPoint() - getVelocity();
    }


    /** Update motor speed every cycle. */
    @Override
    public void onLoop() {
        this.motor.getPIDController().setReference(this.setPoint, CANSparkMax.ControlType.kVelocity);

    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("shooter.velocity", getVelocity());
        SmartDashboard.putNumber("shooter.setpoint", getSetPoint());
        SmartDashboard.putNumber("shooter.setpoint.set", getSetPoint());
        SmartDashboard.putNumber("shooter.error", getError());
    }

    @Override
    public void receiveOptions() {
        setSetPoint(SmartDashboard.getNumber("shooter.setpoint.set", getSetPoint()));
    }

}
