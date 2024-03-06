package frc.robot.util;

import java.util.Optional;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;

public class NoteTargetingHelpers {

  public static Optional<Translation2d> getFieldPoint() {
    Translation2d out = null;
    if (targetLock()) {
      double x = SmartDashboard.getNumber("note/x", 0);
      double y = SmartDashboard.getNumber("note/y", 0);
      out = robotSpaceToFieldSpace(new Translation2d(x, y));
    }
    return Optional.of(out);
  }

  public static Translation2d robotSpaceToFieldSpace(Translation2d pointInRobotSpace) {
    Transform2d transform = new Transform2d(pointInRobotSpace, new Rotation2d());
    return Drivetrain.getInstance().getPose().transformBy(transform).getTranslation();
  }

  public static boolean targetLock() {
    return SmartDashboard.getBoolean("note/targetLock", false);
  }
}
