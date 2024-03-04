package frc.robot.auto.actions;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.*;
import frc.robot.auto.actions.templates.Action;
import frc.robot.constants.MechanismConstraints;
import frc.robot.subsystems.Drivetrain;

public class DriveDistanceWithHeading extends Action {
  protected Measure<Distance> distance;
  protected Rotation2d direction;

  public DriveDistanceWithHeading(Measure<Distance> distance, Rotation2d heading) {
    this.distance = distance;
    this.direction = heading;
  }

	@Override
	public void onStart() {
      Drivetrain.getInstance().setFieldDriveInputs(MechanismConstraints.drivetrain.kMaxVelocity, direction);
	}

	@Override
	public void whileRunning() {
	}

	@Override
	public void onCleanup() {
	}
  
}
