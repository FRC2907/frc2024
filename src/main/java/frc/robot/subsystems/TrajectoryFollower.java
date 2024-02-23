package frc.robot.subsystems;

import java.util.Map;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.actions.templates.Action;
import frc.robot.constants.MechanismDimensions;

public class TrajectoryFollower extends Action {
    private Trajectory trajectory;
    private Timer timer;
    private DifferentialDriveKinematics kinematics;

    private static RamseteController controller = new RamseteController();



    public TrajectoryFollower(Trajectory t, DifferentialDriveKinematics k){
        this.trajectory = t;
        this.kinematics = k;
        this.timer = new Timer();
    }

    public TrajectoryFollower(Trajectory t) {
        this(t, MechanismDimensions.drivetrain.DRIVE_KINEMATICS);
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