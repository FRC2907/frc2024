package frc.robot.subsystems;

import java.util.Optional;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.game_elements.FieldElements;
import frc.robot.util.NoteTargetingHelpers;


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
        Optional<Translation2d> maybe_there = NoteTargetingHelpers.getFieldPoint();
        if (maybe_there.isEmpty())
            return new TrajectoryFollower(Drivetrain.getInstance().getPose());
        return new TrajectoryFollower(maybe_there.get());
    }

    public TrajectoryFollower findPathToSpeaker() {
        return new TrajectoryFollower(FieldElements.getScoringRegions().kSpeaker);
    }

    public TrajectoryFollower findPathToAmp() {
        return new TrajectoryFollower(FieldElements.getScoringRegions().kAmp);
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