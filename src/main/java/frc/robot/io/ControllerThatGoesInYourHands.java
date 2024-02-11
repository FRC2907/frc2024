package frc.robot.io;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.ISubsystem;

public class ControllerThatGoesInYourHands extends PS4Controller implements ISubsystem {
    /* TODO
     * - integrate this into Robot and Superstructure classes
     */

    private Timer rumbleTimer;
    private double rumbleDuration;

    private ControllerThatGoesInYourHands(int port) {
        super(port);
        this.rumbleTimer = new Timer();
        this.rumbleDuration = 0;
    }

    private static Map<Integer, ControllerThatGoesInYourHands> instances
        = new HashMap<Integer, ControllerThatGoesInYourHands>();
    public static ControllerThatGoesInYourHands getInstance(int id) {
        if (!instances.containsKey(id))
            instances.put(id, new ControllerThatGoesInYourHands(id));
        return instances.get(id);
    }

    public void rumble(double duration, double power) {
        this.rumbleTimer.reset();
        this.rumbleDuration = duration;
        this.setRumble(RumbleType.kBothRumble, power);
        this.rumbleTimer.start();
    }

    public void rumble(double duration) {
        this.rumble(duration, 1.0);
    }

   public Rotation2d getLeftAngle() {
    return Rotation2d.fromRadians(Math.atan2(getLeftY(), getLeftX()));
   } 
   public double getLeftMagnitude() {
    return Math.sqrt(Math.pow(getLeftX(), 2) + Math.pow(getLeftY(), 2));
   } 
   public Rotation2d getRightAngle() {
    return Rotation2d.fromRadians(Math.atan2(getRightY(), getRightX()));
   } 
   public double getRightMagnitude() {
    return Math.sqrt(Math.pow(getRightX(), 2) + Math.pow(getRightY(), 2));
   } 

    @Override
    public void onLoop() {
        if (this.rumbleTimer.hasElapsed(rumbleDuration))
            this.setRumble(RumbleType.kBothRumble, 0);
    }

    @Override
    public void submitTelemetry() {}

    @Override
    public void receiveOptions() {}
}
