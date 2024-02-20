package frc.robot.constants;

import org.opencv.core.Scalar;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import frc.robot.subsystems.Drivetrain.DriveMode;

/**
 * Style guidance:
 * - SCREAMING_SNAKE_CASE for properties of the robot hardware, things that are not configurable as code
 * - kPascalCase for configurable values, such as setpoints and gains
 * - comment the unit on any value where it makes sense (gains have units but they're too hard to think about)
 */

public class Control {

    public static class arm {
        public static final double ARM_REV_PER_ENC_REV = 1; /// TODO based on gear ratio
        /**
         * enc_rev   arm_rev   arm_rev
         * ------- * ------- = -------
         *    1      enc_rev      1   
         */
        public static final double ARM_REV_PER_ENC_POS_UNIT = ARM_REV_PER_ENC_REV;
        /**
         * enc_rev   1 min   60   arm_rev   arm_rev
         * ------- * ----- * -- * ------- = -------
         *  1 min    60 s    1    enc_rev      s   
         */
        public static final double ARM_REV_PER_SEC_PER_ENC_VEL_UNIT = ARM_REV_PER_ENC_REV * 60;

        public static final double kP_pos = 1;                        /// TODO
        public static final double kD_pos = 1;                        /// TODO
        public static final Rotation2d kPositionHysteresis =  Rotation2d.fromDegrees( 2);   /// TODO
        public static final Rotation2d kVelocityHysteresis =  Rotation2d.fromDegrees( 2); // rot/s /// TODO

        public static final Rotation2d kStartPosition      = Rotation2d.fromDegrees( 90);   /// TODO
        public static final Rotation2d kFloorPosition      = Rotation2d.fromDegrees(  0);   /// TODO
        public static final Rotation2d kAmpPosition        = Rotation2d.fromDegrees(100);   /// TODO
        public static final Rotation2d kSpeakerPosition    = Rotation2d.fromDegrees( 30);   /// TODO
        public static final Rotation2d kHoldingPosition    = Rotation2d.fromDegrees( 20);   /// TODO
        public static final Rotation2d kClimbReadyPosition = Rotation2d.fromDegrees( 90);   /// TODO
        public static final Rotation2d kClimbClumbPosition = Rotation2d.fromDegrees( 30);   /// TODO
        public static final Rotation2d kMinPosition        = Rotation2d.fromDegrees(  0);   /// TODO
        public static final Rotation2d kMaxPosition        = Rotation2d.fromDegrees(120);   /// TODO

        /**
         * manual control works by incrementing the arm's position setpoint every cycle.
         * this might be a bad idea. TODO consider complexity of moving to a velocity
         * model?
         */
        public static final Rotation2d kManualControlDiff  =  Rotation2d.fromDegrees( 2);   /// TODO
    }

    public static class drivetrain {

        public static final double TRACK_WIDTH = 0.5823458; // m 
        public static final double WHEEL_DIAMETER = 0; // m  /// TODO
        public static final double WHEEL_CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER; // m
        public static final double WHEEL_REV_PER_ENC_REV = 1; // unitless  /// TODO based on gear ratio
        /**
         * enc_rev   whl_rev      m      m
         * ------- * ------- * ------- = -
         *    1      enc_rev   whl_rev   1
         */
        public static final double METER_PER_ENC_POS_UNIT = WHEEL_REV_PER_ENC_REV * WHEEL_CIRCUMFERENCE; // m / enc_rev
        /**
         * enc_rev   1 min   60   whl_rev      m      m
         * ------- * ----- * -- * ------- * ------- = -
         *  1 min    60 s    1    enc_rev   whl_rev   s
         */
        public static final double METER_PER_SEC_PER_ENC_VEL_UNIT
            = 60.0 * WHEEL_REV_PER_ENC_REV * WHEEL_CIRCUMFERENCE; // m/s


        public static final double kTrackWidthFudge = 0; // m  /// TODO this is just Extra Number to account for wheel scrub
        public static final DifferentialDriveKinematics DRIVE_KINEMATICS
            = new DifferentialDriveKinematics(TRACK_WIDTH + kTrackWidthFudge);

        public static final double kP_fieldRelativeHeading = 0; // TODO

        public static final DriveMode kDefaultDriveModeWithoutNote = DriveMode.FIELD_FORWARD;
        public static final DriveMode kDefaultDriveModeWithNote = DriveMode.FIELD_REVERSED;
    }

    public static class intake {
        public static final double WHEEL_DIAMETER = 0; // m  /// TODO
        public static final double WHEEL_CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER; // m
        public static final double WHEEL_REV_PER_ENC_REV = 1; /// TODO based on gear ratio
        /**
         * enc_rev   1 min   60   whl_rev      m      m
         * ------- * ----- * -- * ------- * ------- = -
         *  1 min    60 s    1    enc_rev   whl_rev   s
         */
        public static final double METER_PER_SECOND_PER_ENC_VEL_UNIT
            = 60.0 * WHEEL_REV_PER_ENC_REV * WHEEL_CIRCUMFERENCE; // m/s

        public static final double kP = 1;                            /// TODO
        public static final double kD = 1;                            /// TODO

        public static final double kIntakingSpeed = 0; // m/s         /// TODO guess
        public static final double kOutakingSpeed = 0; // m/s         /// TODO 
        public static final double kOff           = 0; // m/s         /// TODO be sure
    }

    public static class shooter {
        public static final double WHEEL_DIAMETER = 0; // m  /// TODO
        public static final double WHEEL_CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER; // m
        public static final double WHEEL_REV_PER_ENC_REV = 1; /// TODO based on gear ratio
        /**
         * enc_rev   1 min   60   whl_rev      m      m
         * ------- * ----- * -- * ------- * ------- = -
         *  1 min    60 s    1    enc_rev   whl_rev   s
         */
        public static final double METER_PER_SECOND_PER_ENC_VEL_UNIT
            = 60.0 * WHEEL_REV_PER_ENC_REV * WHEEL_CIRCUMFERENCE; // m/s

        public static final double kP = 1;                            /// TODO
        public static final double kD = 1;                            /// TODO

        public static final double kAmpSpeed     = 0; // m/s          /// TODO calculate
        public static final double kSpeakerSpeed = 0; // m/s          /// TODO calculate
        public static final double kOff          = 0; // m/s          /// TODO verify
    }

    public static class camera {
        public static final int WIDTH = 320;
        public static final int HEIGHT = 240;
        public static final Scalar kOrangeLow = new Scalar(4, 127, 127);
        public static final Scalar kOrangeHigh = new Scalar(32, 255, 255);
        public static final double kAreaFilterFactor = 0.075;
        public static final boolean kEnabled = true;
        public static final boolean kNoteTrackingEnabled = false;
    }
}
