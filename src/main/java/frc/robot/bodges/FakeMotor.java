package frc.robot.bodges;

public class FakeMotor extends FeedbackMotor {

    private double speed;
    private boolean isInverted;

    @Override
    public void set(double speed) { this.speed = speed; }

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
    protected FeedbackMotor setFactor_downstream(double factor) { return this; }

    @Override
    public double getPosition() { return 0; }

    @Override
    public double getVelocity() { return 0; }
}
