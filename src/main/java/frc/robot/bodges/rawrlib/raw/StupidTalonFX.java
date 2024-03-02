package frc.robot.bodges.rawrlib.raw;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.util.Util;

/**
 * @deprecated
 */
public class StupidTalonFX extends FeedbackMotor {

    private TalonFX m;

    public StupidTalonFX(int deviceId) {
        this.m = new TalonFX(deviceId);
        m.getConfigurator().apply(new TalonFXConfiguration()
        );
    }

    @Override
    protected FeedbackMotor setFactor_downstream(double factor) {
        m.getConfigurator().apply(new FeedbackConfigs().withSensorToMechanismRatio(1 / factor));
        return this;
    }

    public void setControl(Follower follower) {
        m.setControl(follower);
    }

    @Override
    public double getPosition() { return m.getPosition().getValueAsDouble(); }

    @Override
    public double getVelocity() { return m.getVelocity().getValueAsDouble(); }

    /* everything below this line is boilerplate; you may copy-paste into new files */

    @Override
    public void set(double speed) { m.set(Util.clampSymmetrical(speed, 1)); }

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
