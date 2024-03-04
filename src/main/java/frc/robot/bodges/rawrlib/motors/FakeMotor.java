package frc.robot.bodges.rawrlib.motors;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.generics.DimensionalDcMotorSpeedCurve;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MechanismDimensions;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class FakeMotor implements MotorController, ISubsystem {

    public double inputPct = 0;
    public boolean isInverted = false;
    public double position = 0;
    public DimensionalDcMotorSpeedCurve<Angle> curve = new DimensionalDcMotorSpeedCurve<Angle>(Units.Volts.zero(), Units.RPM.of(500).per(Units.Volts));
    public Measure<Voltage> kMaxVoltage = MechanismDimensions.electrical.MAX_VOLTAGE;

    @Override
    public void setVoltage(double voltage) { this.inputPct = voltage / kMaxVoltage.in(Units.Volts); }

    @Override
    public void set(double speed) { this.inputPct = Util.clampSymmetrical(speed, 1); }

    @Override
    public double get() { return inputPct; }

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
    public double getVelocity() { return curve.getSpeed(kMaxVoltage.times(get())).in(Units.RotationsPerSecond); }

    @Override
    public void onLoop() {
        receiveOptions();
        this.position += getVelocity() * MechanismConstraints.kPeriod.in(Units.Seconds);
        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("fake position", Util.fuzz() + position);
        SmartDashboard.putNumber("fake getPosition", Util.fuzz() + getPosition());
        SmartDashboard.putNumber("fake getVelocity", Util.fuzz() + getVelocity());
    }

    @Override
    public void receiveOptions() {
    }
}
