package frc.robot.subsystems;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.actions.templates.Action;
import frc.robot.constants.Control;

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
        this(t, Control.drivetrain.DRIVE_KINEMATICS);
    }


    public double[] getWheelSpeeds(Pose2d current){
        Trajectory.State goal = trajectory.sample(timer.get()); 
        ChassisSpeeds adjustedSpeeds = controller.calculate(current, goal);
        DifferentialDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(adjustedSpeeds);
        double left = wheelSpeeds.leftMetersPerSecond;
        double right = wheelSpeeds.rightMetersPerSecond;

        return new double[] {left, right};
    }

    @Override
    public void onStart() {
        timer.restart();
        this.started = true;
        this.running = true;
    }

    @Override
    public void whileRunning() {
        this.finished = trajectory.getTotalTimeSeconds() < timer.get();
        this.running = !this.finished;
    }

    @Override
    public void onCleanup() {
    }
}
