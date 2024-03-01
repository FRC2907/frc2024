package frc.robot.subsystems;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import frc.robot.constants.game_elements.FieldElements;
import frc.robot.constants.MechanismConstraints;


/**
 * The Hat generates trajectories in real time based on
 * localization feedback from the Limelight, object tracking
 * from the simple camera, and strategic directives.
 */
public class Hat implements ISubsystem {

    private static Hat instance;
    private static Drivetrain drivetrain = Drivetrain.getInstance();


    public static Hat getInstance() {
        if (instance == null)
            instance = new Hat();
        return instance;
    }

    public TrajectoryFollower findPathToNote() {
        double x = SmartDashboard.getNumber("note/x", 0);
        double y = SmartDashboard.getNumber("note/y", 0);
        double r = SmartDashboard.getNumber("note/r", 0);
        Transform2d transform = new Transform2d(x, y, Rotation2d.fromDegrees(r));

        Pose2d here = drivetrain.getPose();
        Pose2d there = here.transformBy(transform);
        return new TrajectoryFollower(TrajectoryGenerator.generateTrajectory(List.of
        (here, there), MechanismConstraints.drivetrain.config));
    }

    public TrajectoryFollower findPathToSpeaker() {
        Pose2d here = drivetrain.getPose();
        Pose2d there = FieldElements.getScoringRegions().kSpeaker.getNearest(here);
        return new TrajectoryFollower(TrajectoryGenerator.generateTrajectory(List.of
        (here, there), MechanismConstraints.drivetrain.config));
    }

    public TrajectoryFollower findPathToAmp() {
        Pose2d here = drivetrain.getPose();
        Pose2d there = FieldElements.getScoringRegions().kAmp.getNearest(here);
        return new TrajectoryFollower(TrajectoryGenerator.generateTrajectory(List.of
        (here, there), MechanismConstraints.drivetrain.config));
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