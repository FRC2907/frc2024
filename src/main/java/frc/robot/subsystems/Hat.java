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
        // basically we need to be able to map a pixel (X,Y) in the camera feed to a
        // translation on the field, then feed that to the trajectory generator
        return null;
    }

    public TrajectoryFollower findPathToSpeaker() {
        // TODO implement
        // this one is a challenge: we have a region we want to be in
        return null;
    }

    public TrajectoryFollower findPathToAmp() {
        // TODO implement
        // this one's pretty simple, we know where the amp is, we know where we are, we
        // just ask for a path to connect them
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