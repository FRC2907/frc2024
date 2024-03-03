package frc.robot.util;

import java.util.Optional;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MechanismDimensions;
import frc.robot.subsystems.Drivetrain;

public class NoteTargetingHelpers {
  
  public static Optional<Translation2d> getFieldPoint() {
    Translation2d out = null;
    if (targetLock()) {
      double x = SmartDashboard.getNumber("note/x", 0.5);
      double y = SmartDashboard.getNumber("note/y", 0.5);
      out = imagePointToFieldPoint(x, y);
    }
    return Optional.of(out);
  }

    public static Translation2d imagePointToFieldPoint(double x, double y) {
        Translation2d robot_to_note = imagePointToTranslationFromRobot(x, y);
        Translation2d note_field_pos = robotSpaceToFieldSpace(robot_to_note);
        return note_field_pos;
    }

    /**
     * Convert a relative position in an image to a position within a quadrilateral bounded by known points.
     * @param x Percentage (0..1) from left to right across the image
     * @param y Percentage (0..1) from top to bottom down the image
     * @return Point in the space perceived by the image
     */
    public static Translation2d imagePointToTranslationFromRobot(double x, double y) {
      return MechanismDimensions.camera.fov.interpolate(x, y);
    }

    public static Translation2d robotSpaceToFieldSpace(Translation2d pointInRobotSpace) {
        Transform2d transform = new Transform2d(pointInRobotSpace, new Rotation2d());
        return Drivetrain.getInstance().getPose().transformBy(transform).getTranslation();
    }

    public static boolean targetLock() { return SmartDashboard.getNumber("targetLockFrameCount", 0) >= MechanismConstraints.camera.kTargetLockFrameCountThreshold; }
}
