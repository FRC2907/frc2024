package frc.robot.subsystems;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MotorControllers;

public class Shooter implements ISubsystem {
    private DimensionalFeedbackMotor<Distance> motor;

    private Shooter(DimensionalFeedbackMotor<Distance> motor) {
        this.motor = motor;
    }

    private static Shooter instance;
    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter(MotorControllers.shooter());
        }
        return instance;
    }


    public void setVelocity(Measure<Velocity<Distance>> reference) {
        motor.setVelocity(reference);
    }
    public void amp() {
        setVelocity(GameInteractions.shooter.kAmpSpeed);
    }
    public void speaker() {
        setVelocity(GameInteractions.shooter.kSpeakerSpeed);
    }
    public void off() {
        setVelocity(GameInteractions.shooter.kOff);
    }


    public boolean noteScored() {
        return false;
        // TODO add code to see if note has scored
        // probably look for a falling edge on Intake::hasNote while in a scoring state
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