package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.junk.AWheeMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MotorControllers;
import frc.robot.constants.Ports;

public class Intake implements ISubsystem {
    public final AWheeMotor<Distance> motor;
    public final ColorSensorV3 presenceSensor;
    public boolean running;

    private Intake(AWheeMotor<Distance> motor) {
        this.motor = motor;
        this.presenceSensor = new ColorSensorV3(Ports.I2C.kIntakeSensor);
    }

    private static Intake instance;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake(MotorControllers.intake());
        }
        return instance;
    }

    public void intake() {
        motor.setVelocity(GameInteractions.intake.kIntakingSpeed);
        running = true;
    }

    public void outake() {
        motor.setVelocity(GameInteractions.intake.kOutakingSpeed);
        running = true;
    }

    public void off() {
        motor.setVelocity(GameInteractions.intake.kOff);
        running = false;
    }

    public void shoot() {
        motor.setVelocity(Shooter.getInstance().motor.getVelocityReference());
        running = true;
    }

    /** Return whether the intake has a Note in it. */
    public boolean hasNote() {
        return presenceSensor.getProximity() > MechanismConstraints.intake.kPresenceSensorTriggerProximity;
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putBoolean("intake/hasNote", hasNote());
    }

}