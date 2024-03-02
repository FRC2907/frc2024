package frc.robot.bodges.rawrlib.raw;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class StupidSparkMax extends FeedbackMotor {

    private CANSparkMax m;

    public StupidSparkMax(int deviceId, MotorType type) {
        this.m = new CANSparkMax(deviceId, type);
    }

    @Override
    protected FeedbackMotor setFactor_downstream(double factor) {
        this.factor = factor;
        m.getEncoder().setPositionConversionFactor(factor);
        m.getEncoder().setVelocityConversionFactor(factor);
        return this;
    }

    public void follow(StupidSparkMax leader) {
        m.follow(leader.m);
    }

    @Override
    public double getPosition() { return m.getEncoder().getPosition(); }

    @Override
    public double getVelocity() { return m.getEncoder().getVelocity(); }

    /* everything below this line is boilerplate; you may copy-paste into new files */

    @Override
    public void set(double speed) { m.set(speed); }

    @Override
    public double get() { return m.get(); }

    @Override
    public void setInverted(boolean isInverted) { m.setInverted(isInverted); }

    @Override
    public boolean getInverted() { return m.getInverted(); }

    @Override
    public void disable() { m.disable(); }

    @Override
    public void stopMotor() { m.stopMotor(); }
}
