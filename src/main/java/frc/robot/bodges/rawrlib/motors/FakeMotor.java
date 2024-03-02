package frc.robot.bodges.rawrlib.motors;

import edu.wpi.first.units.Units;
import frc.robot.bodges.rawrlib.raw.FeedbackMotor;
import frc.robot.constants.Misc;
import frc.robot.util.Util;

public class FakeMotor extends FeedbackMotor {

    private double speed = 0;
    private boolean isInverted = false;
    private double factor = 1;
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

    @Override
    protected FeedbackMotor setFactor_downstream(double factor) { this.factor = factor; return this; }

    @Override
    public double getPosition() { return factor * position; }

    @Override
    public double getVelocity() { return factor * get(); }

    @Override
    public void onLoop() {
        this.position += get() * Misc.kPeriod.in(Units.Seconds);
        super.onLoop();
    }
}
