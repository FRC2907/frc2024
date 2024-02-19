package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Intake implements ISubsystem {
    private double setPoint; // m/s
    private CANSparkMax motor;

    private Intake(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setVelocityConversionFactor(Control.intake.METER_PER_SECOND_PER_ENC_VEL_UNIT);
    }

    private static Intake instance;
    public static Intake getInstance() {
        if (instance == null) {
            CANSparkMax motor = Util.createSparkGroup(Ports.can.intake.MOTORS);
            instance = new Intake(motor);
        }
        return instance;
    }


    /** Set the desired speed of the intake in m/s. */
    public void setSetPoint(double _setPoint) {
        this.setPoint = _setPoint;
    }
    public double getSetPoint() {
        return this.setPoint;
    }
    /** Return intake speed in m/s. */
    public double getVelocity() {
        return this.motor.getEncoder().getVelocity();
    }
    public double getError() {
        return getSetPoint() - getVelocity();
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
        this.motor.getPIDController().setReference(this.setPoint, CANSparkMax.ControlType.kVelocity);
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("intake.velocity", getVelocity());
        SmartDashboard.putNumber("intake.setpoint", getSetPoint());
        SmartDashboard.putNumber("intake.setpoint.set", getSetPoint());
        SmartDashboard.putNumber("intake.error", getError());
    }

    @Override
    public void receiveOptions() {
        setSetPoint(SmartDashboard.getNumber("intake.setpoint.set", getSetPoint()));
    }

}
