package frc.robot.auto.actions;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.TrajectoryFollower;

public class DriveToward extends FollowTrajectory{

    public DriveToward(TrajectoryFollower GetWheelSpeeds) {
        super(GetWheelSpeeds);
    }
    public DriveToward(Pose2d position){
        this(new TrajectoryFollower(position));
    }
    
}
