package frc.robot.auto.actions;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.TrajectoryFollower;

public class DriveToward extends FollowTrajectory {

    private DriveToward(TrajectoryFollower path) {
        super(path);
    }
    public DriveToward(Translation2d position) {
        this(new TrajectoryFollower(position));
    }
    public DriveToward(Pose2d position){
        this(new TrajectoryFollower(position));
    }
    
}
