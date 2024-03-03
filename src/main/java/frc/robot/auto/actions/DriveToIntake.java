package frc.robot.auto.actions;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.NoteTargetingPipeline;
import frc.robot.subsystems.TrajectoryFollower;

public class DriveToIntake extends FollowTrajectory {

	private DriveToIntake(TrajectoryFollower path) {
		super(path);
	}

  public DriveToIntake(Translation2d position) {
    super(new TrajectoryFollower(position), NoteTargetingPipeline::targetLock);
  }

}
