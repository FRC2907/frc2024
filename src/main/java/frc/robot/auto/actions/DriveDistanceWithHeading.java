package frc.robot.auto.actions;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.TrajectoryFollower;

public class DriveDistanceWithHeading extends FollowTrajectory {

  public DriveDistanceWithHeading(Measure<Distance> distance, Rotation2d heading, boolean forward) {
		super(new TrajectoryFollower(
			Drivetrain
				.getInstance()
				.getPose()
				.getTranslation()
				.plus(new Translation2d(distance, Units.Meters.zero())
					.rotateBy(heading)
				)
			, forward)
		);
  }
  
}
