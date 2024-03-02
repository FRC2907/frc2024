package frc.robot.bodges.rawrlib.motors;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.constants.MechanismConstraints;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class FakeMotor implements MotorController, ISubsystem {

    private double speed = 0;
    private boolean isInverted = false;
    private double position = 0;

    @Override
    public void set(double speed) { this.speed = Util.clampSymmetrical(speed, 1); }

    @Override
    public double get() { return speed; }

    @Override
    public void setInverted(boolean isInverted) { this.isInverted = isInverted; }

    @Override
    public boolean getInverted() { return isInverted; }

    @Override
    public void disable() { stopMotor(); }

    @Override
    public void stopMotor() { set(0); }

    /**
     * @return position in rotations
     */
    public double getPosition() { return position; }

    /**
     * @return velocity in rotations per second
     */
    public double getVelocity() { return get(); }

    public void onLoop() {
        receiveOptions();
        this.position += get() * MechanismConstraints.kPeriod.in(Units.Seconds);
        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
    }

    @Override
    public void receiveOptions() {
    }
}
