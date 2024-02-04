package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

// TODO consider converging the Shooter and Intake classes into a single Flywheel class
// TODO further consider generalizing flywheels, arms, and elevators into generic classes
// and then extending them for whatever
public class Shooter implements ISubsystem {
    private double setPoint; // wheel rpm

    private CANSparkMax motor;

    private Shooter(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setVelocityConversionFactor(1 / Control.shooter.ENCODER_RPM_PER_WHEEL_RPM);
    }

    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) {
            CANSparkMax motor = Util.createSparkGroup(Ports.can.intake.MOTORS);
            instance = new Shooter(motor);
        }
        return instance;
    }

    /** Return shooter speed in wheel RPM. */
    public double getSpeed() {
        return motor.getEncoder().getVelocity();
    }

    /** Set the desired speed of the shooter in wheel RPM. */
    public void setSetPoint(double _setPoint) {
        this.setPoint = _setPoint;
    }

    /** Update motor speed every cycle. */
    public void onLoop() {
        this.motor.getPIDController().setReference(this.setPoint, CANSparkMax.ControlType.kVelocity);

    }

}
