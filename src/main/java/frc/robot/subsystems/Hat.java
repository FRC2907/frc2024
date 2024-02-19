package frc.robot.subsystems;

import edu.wpi.first.math.trajectory.Trajectory;

/**
 * The Hat generates trajectories in real time based on
 * localization feedback from the Limelight, object tracking
 * from the simple camera, and strategic directives.
 */
public class Hat implements ISubsystem {

    private static Hat instance;
    public static Hat getInstance() {
        if (instance == null)
            instance = new Hat();
        return instance;
    }


    public TrajectoryFollower findPathToNote() {
        // TODO implement
        return null;
    }

    public TrajectoryFollower findPathToSpeaker() {
        // TODO implement
        return null;
    }

    public TrajectoryFollower findPathToAmp() {
        // TODO implement
        return null;
    }


    @Override
    public void onLoop() {
    }

    @Override
    public void submitTelemetry() {
    }

    @Override
    public void receiveOptions() {
    }
}
