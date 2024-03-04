package frc.robot.auto.actions;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.game_elements.FieldElements;
import frc.robot.subsystems.TrajectoryFollower;
import frc.robot.util.NoteTargetingHelpers;

public class DriveToIntake extends FollowTrajectory {

  public DriveToIntake(Translation2d position) {
    super(new TrajectoryFollower(FieldElements.noteBallpark(position), true), NoteTargetingHelpers::targetLock);
  }

}
