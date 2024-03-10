package frc.robot.subsystems;

import java.util.function.Supplier;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MotorControllers;
import frc.robot.constants.Ports;

public class Intake implements ISubsystem {
    public final DimensionalFeedbackMotor<Distance> motor;
    public final ColorSensorV3 presenceSensor;
    public boolean running;

    private Intake(DimensionalFeedbackMotor<Distance> motor) {
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


    public void setVelocity(Measure<Velocity<Distance>> reference) {
        motor.setVelocity(reference);
    }
    public void matchVelocity(Supplier<Measure<Velocity<Distance>>> reference) {
        motor.setVelocity(reference);
    }

    public void intake() {
        setVelocity(GameInteractions.intake.kIntakingSpeed);
        running = true;
    }
    public void outake() {
        setVelocity(GameInteractions.intake.kOutakingSpeed);
        running = true;
    }
    public void off() {
        setVelocity(GameInteractions.intake.kOff);
        running = false;
    }
    public void shoot() {
        matchVelocity(Shooter.getInstance().motor.getVelocityController().getReferenceSupplier());
        running = true;
    }


    /** Return whether the intake has a Note in it. */
    public boolean hasNote() {
        return presenceSensor.getProximity() > MechanismConstraints.intake.kPresenceSensorTriggerProximity;
    }


    /** Update motor speed every cycle. */
    @Override
    public void onLoop() {
        motor.onLoop();
    }

    @Override
    public void submitTelemetry() {
    }

    @Override
    public void receiveOptions() {
    }


}