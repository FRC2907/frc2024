package frc.robot.subsystems;

import java.util.List;
import java.util.Map;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.actions.templates.Action;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MechanismDimensions;
import frc.robot.util.Util;
import frc.robot.util.Geometry.ScoringRegion;

public class TrajectoryFollower extends Action {
    private Trajectory trajectory;
    private Timer timer;
    private DifferentialDriveKinematics kinematics;

    private static RamseteController controller = new RamseteController();
    private static Drivetrain drivetrain = Drivetrain.getInstance();



    public TrajectoryFollower(Trajectory t, DifferentialDriveKinematics k){
        this.trajectory = t;
        this.kinematics = k;
        this.timer = new Timer();
    }

    public TrajectoryFollower(Trajectory t) {
        this(t, MechanismDimensions.drivetrain.DRIVE_KINEMATICS);
    }

    public TrajectoryFollower(Pose2d destination, TrajectoryConfig config) {
        this(TrajectoryGenerator.generateTrajectory(List.of
        (drivetrain.getPose(), destination), config));
    }

    public TrajectoryFollower(Pose2d destination, boolean intaking) {
        this(TrajectoryGenerator.generateTrajectory(List.of
        (drivetrain.getPose(), destination),
        intaking
        ? MechanismConstraints.drivetrain.intaking_config
        : MechanismConstraints.drivetrain.scoring_config
        ));
    }

    public TrajectoryFollower(ScoringRegion destination, boolean intaking) {
        this(destination.getNearest(drivetrain.getPose()), intaking);
    }

    public TrajectoryFollower(Translation2d destination, boolean intaking) {
        this(Util.pointToPose(drivetrain.getPose(), destination), intaking);
    }

    public Map<String,Measure<Velocity<Distance>>> getWheelSpeeds(Pose2d current){
        Trajectory.State goal = trajectory.sample(timer.get()); 
        ChassisSpeeds adjustedSpeeds = controller.calculate(current, goal);
        DifferentialDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(adjustedSpeeds);
        Measure<Velocity<Distance>> left  = Units.MetersPerSecond.of(wheelSpeeds. leftMetersPerSecond);
        Measure<Velocity<Distance>> right = Units.MetersPerSecond.of(wheelSpeeds.rightMetersPerSecond);

        return Map.of("left", left, "right", right);
    }

    @Override
    public void onStart() {
        timer.restart();
        started = true;
        running = true;
    }

    @Override
    public void whileRunning() {
        finished = trajectory.getTotalTimeSeconds() < timer.get();
        running = !finished;
    }

    @Override
    public void onCleanup() {
    }
}