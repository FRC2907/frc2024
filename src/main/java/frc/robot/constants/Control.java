package frc.robot.constants;

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
        public static final double ENCODER_POS_UNIT_PER_DEGREE = 0; // tick/deg   /// TODO measure
        public static final double ENCODER_VEL_UNIT_PER_DEGREE_PER_SECOND = 0; // tick/deg/s  /// TODO measure

        public static final double kP_pos = 1;                        /// TODO
        public static final double kD_pos = 1;                        /// TODO

        public static final double kStartPosition      = 90; // deg   /// TODO
        public static final double kFloorPosition      =  0; // deg   /// TODO
        public static final double kAmpPosition        = 91; // deg   /// TODO
        public static final double kSpeakerPosition    = 30; // deg   /// TODO
        public static final double kHoldingPosition    =  0; // deg   /// TODO
        public static final double kClimbReadyPosition =  0; // deg   /// TODO
        public static final double kClimbClumbPosition =  0; // deg   /// TODO
        public static final double kMinPosition        =  0; // deg   /// TODO
        public static final double kMaxPosition        =  0; // deg   /// TODO

        /**
         * manual control works by incrementing the arm's position setpoint every cycle.
         * this might be a bad idea. TODO consider complexity of moving to a velocity
         * model?
         */
        public static final double kManualControlDiff  =  0; // deg   /// TODO
        public static final double kPositionHysteresis =  2; // deg   /// TODO
        public static final double kVelocityHysteresis =  2; // deg/s /// TODO
    }

    public static class drivetrain {
        public static final DriveMode kDefaultDriveMode = DriveMode.FIELD_FORWARD;
        public static final double TRACK_WIDTH = 0; // m  /// TODO
        public static final double kTrackWidthFudge = 0; // m  /// TODO this is just Extra Number to account for wheel scrub
        public static final DifferentialDriveKinematics DRIVE_KINEMATICS = new DifferentialDriveKinematics(TRACK_WIDTH + kTrackWidthFudge);
    }

    // TODO consider defining intake speed as a linear unit (m/s)
    // and then translating that to RPM for the motors
    public static class intake {
        public static final double ENCODER_VEL_UNIT_PER_INTAKE_MPS = 0; // m/s /// TODO calculate // use wheel dims to find linear speed

        public static final double kP = 1;                            /// TODO
        public static final double kD = 1;                            /// TODO

        public static final double kIntakingSpeed = 0; // m/s         /// TODO guess
        public static final double kOutakingSpeed = 0; // m/s         /// TODO 
        public static final double kOff           = 0; // m/s         /// TODO be sure
    }

    public static class shooter {
        public static final double ENCODER_VEL_UNIT_PER_SHOOTER_MPS = 0; // m/s  /// TODO

        public static final double kAmpSpeed     = 0; // m/s          /// TODO calculate
        public static final double kSpeakerSpeed = 0; // m/s          /// TODO calculate
        public static final double kOff          = 0; // m/s          /// TODO verify
    }
}
