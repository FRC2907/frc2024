package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Intake implements ISubsystem {
    private double setPoint; // wheel rpm

    private CANSparkMax motor;

    private Intake(CANSparkMax _motor) {
        this.motor = _motor;
    }

    private static Intake instance;

    public static Intake getInstance() {
        if (instance == null) {
            CANSparkMax motor = Util.createSparkGroup(Ports.can.intake.MOTORS);
            instance = new Intake(motor);
        }
        return instance;
    }

    public enum IntakeState {
        EMPTY_OFF, EMPTY_RUNNING, FULL_RUNNING, FULL_OFF
    }

    /** Return intake speed in wheel RPM. */
    public double getSpeed() {
        return motor.getEncoder().getVelocity() / Control.intake.ENCODER_RPM_PER_WHEEL_RPM;
    }

    /** Set the desired speed of the intake in wheel RPM. */
    public void setSetPoint(double _setPoint) {
        this.setPoint = _setPoint;
    }

    /** Update motor speed every cycle. */
    public void onLoop() {
        this.onLoopTasks();
        this.motor
                .getPIDController()
                .setReference(
                        this.setPoint * Control.intake.ENCODER_RPM_PER_WHEEL_RPM, CANSparkMax.ControlType.kVelocity);
    }

}
