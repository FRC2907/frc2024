package frc.robot.subsystems;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;

public class TrajectoryFollower {
    private Trajectory trajectory;
    private Timer timer;
    private DifferentialDriveKinematics kinematics;

    private static RamseteController controller = new RamseteController();



    public TrajectoryFollower(Trajectory t, DifferentialDriveKinematics k){
        
    }


    public void startTrajectory(Trajectory t){
        timer.reset();
        this.trajectory = t;
        timer.start();
    }

    public double[] getWheelSpeeds(Pose2d current){
        Trajectory.State goal = trajectory.sample(timer.get()); 
        ChassisSpeeds adjustedSpeeds = controller.calculate(current, goal);
        DifferentialDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(adjustedSpeeds);
        double left = wheelSpeeds.leftMetersPerSecond;
        double right = wheelSpeeds.rightMetersPerSecond;

        return new double[] {left, right};
    }

    public boolean isDone(){
        return trajectory.getTotalTimeSeconds() < timer.get();
    }
}
