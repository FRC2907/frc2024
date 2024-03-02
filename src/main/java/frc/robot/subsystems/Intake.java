package frc.robot.subsystems;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MotorControllers;

public class Intake implements ISubsystem {
    private DimensionalFeedbackMotor<Distance> motor;

    private Intake(DimensionalFeedbackMotor<Distance> motor) {
        this.motor = motor;
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

    public void intake() {
        setVelocity(GameInteractions.intake.kIntakingSpeed);
    }
    public void outake() {
        setVelocity(GameInteractions.intake.kOutakingSpeed);
    }
    public void off() {
        setVelocity(GameInteractions.intake.kOff);
    }


    /** Return whether the intake has a Note in it. */
    public boolean hasNote() {
        // TODO read sensor to determine whether there's a Note in the intake
        return false;
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