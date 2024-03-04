package frc.robot.subsystems;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MotorControllers;
import frc.robot.game_elements.FieldElements;

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
        // FIXME dynamic?
        // TODO refactor constants or whatever, and test numbers
        Measure<Distance> airDistance = Units.Meters.of(FieldElements.getFieldPoints().kSpeakerHole.getDistance(Arm.getInstance().getPivot()));
        Measure<Time> airTime = Units.Seconds.of(0.3);
        setVelocity(airDistance.per(airTime));
    }
    public void off() {
        setVelocity(GameInteractions.shooter.kOff);
    }

    public boolean reachedSetPoint() {
        return motor.getVelocityController().converged();
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