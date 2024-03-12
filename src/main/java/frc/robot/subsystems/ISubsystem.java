package frc.robot.subsystems;

public interface ISubsystem {
    public default void onLoop() {
        receiveOptions();
        submitTelemetry();
    }
    public default void submitTelemetry() {} // publish readings to NT
    public default void receiveOptions() {} // fetch updated settings from NT
}