package frc.robot.subsystems;

public interface ISubsystem {
    public void onLoop();
    public void submitTelemetry(); // publish readings to NT
    public void receiveOptions(); // fetch updated settings from NT
}