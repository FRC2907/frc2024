package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import frc.robot.subsystems.Shooter;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Shooter extends Subsystem {
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

    public enum ShooterState {
        OFF, RUNNING
    }

    /** Return intake speed in wheel RPM */
    public double getSpeed() {
        return motor.getEncoder().getVelocity();
    }

    /** Set the desired speed of shooter into wheel RPM */
    public void setSetPoint(double _setPoint) {
        this.setPoint = _setPoint;
    }

    /** Update motor speed every cycle. */
    public void onLoop() {
        this.onLoopTasks();
        this.motor
                .getPIDController()
                .setReference(this.setPoint, CANSparkMax.ControlType.kVelocity);

    }

}
