package frc.robot.subsystems;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import frc.robot.util.Geometry;
import frc.robot.constants.FieldElements;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MechanismConstraints.drivetrain;

/**
 * The Hat generates trajectories in real time based on
 * localization feedback from the Limelight, object tracking
 * from the simple camera, and strategic directives.
 */
public class Hat implements ISubsystem {

    private static Hat instance;
    private static Drivetrain drivetrain = Drivetrain.getInstance();
    private static TrajectoryConfig config = 
    new TrajectoryConfig(MechanismConstraints.drivetrain.kMaxVelocity, 
                         MechanismConstraints.drivetrain.kMaxAcceleration);

    public static Hat getInstance() {
        if (instance == null)
            instance = new Hat();
        return instance;
    }

    public TrajectoryFollower findPathToNote() {
        // TODO implement
        // basically we need to be able to map a pixel (X,Y) in the camera feed to a
        // translation on the field, then feed that to the trajectory generator
        // the pixel translation will be written in NoteTargetingPipeline someday
        return null;
    }

    public TrajectoryFollower findPathToSpeaker() {
        Pose2d here = drivetrain.getPose();
        Pose2d there = FieldElements.scoring_regions.blue.kSpeaker.getNearest(here);
        return new TrajectoryFollower(TrajectoryGenerator.generateTrajectory(List.of(here, there), config));
    }

    public TrajectoryFollower findPathToAmp() {
        Pose2d here = drivetrain.getPose();
        Pose2d there = FieldElements.scoring_regions.blue.kAmp.getNearest(here);
        return new TrajectoryFollower(TrajectoryGenerator.generateTrajectory(List.of(here, there), config));
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