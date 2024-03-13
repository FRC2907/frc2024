package frc.robot.subsystems;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.stuff.AWheeMotor;
import frc.robot.constants.GameInteractions;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MotorControllers;
import frc.robot.game_elements.FieldElements;

public class Shooter implements ISubsystem {
    public final AWheeMotor<Distance> motor;
    protected final Timer noteGoneTimer = new Timer();

    private Shooter(AWheeMotor<Distance> motor) {
        this.motor = motor;
    }

    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter(MotorControllers.shooter());
        }
        return instance;
    }

    public void amp() {
        motor.setVelocity(GameInteractions.shooter.kAmpSpeed);
    }

    public void speaker() {
        // TODO refactor constants or whatever, and test numbers
        Measure<Distance> airDistance = Units.Meters
                .of(FieldElements.getFieldPoints().kSpeakerHole.getDistance(Arm.getInstance().getPivot()));
        Measure<Time> airTime = Units.Seconds.of(0.3);
        motor.setVelocity(airDistance.per(airTime));
    }

    public void manualShoot() {
        motor.setVelocity(MechanismConstraints.shooter.kMaxVelocity);
    }

    public void off() {
        motor.setVelocity(GameInteractions.shooter.kOff);
    }

    public boolean reachedSetPoint() {
        return motor.checkHysteresis();
    }

    public boolean noteGone() {
        return noteGoneTimer.get() >= GameInteractions.shooter.kNoteGoneThresholdTime.in(Units.Seconds);
    }

    @Override
    public void onLoop() {
        receiveOptions();
        if (Intake.getInstance().hasNote()) noteGoneTimer.restart();
        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putBoolean("shooter/noteGone", noteGone());
    }

}