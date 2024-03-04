package frc.robot.auto.actions;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.TrajectoryFollower;

public class DriveToward extends FollowTrajectory {

    public DriveToward(Translation2d position, boolean forward) {
        super(new TrajectoryFollower(position, forward));
    }
    public DriveToward(Pose2d position, boolean forward){
        super(new TrajectoryFollower(position, forward));
    }
    
}
