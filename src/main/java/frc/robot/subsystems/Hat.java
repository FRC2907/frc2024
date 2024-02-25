package frc.robot.subsystems;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import frc.robot.util.Geometry;
import frc.robot.constants.FieldElements;

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
        // we have a region we want to be in
        // check out util.Geometry.ScoringRegion and
        // constants.FieldElements.scoring_regions
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
        receiveOptions();
        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
    }

    @Override
    public void receiveOptions() {
    }
}