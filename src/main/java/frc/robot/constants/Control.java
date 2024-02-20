package frc.robot.constants;

import org.opencv.core.Scalar;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.units.*;
import frc.robot.subsystems.Drivetrain.DriveMode;

/**
 * Style guidance:
 * - SCREAMING_SNAKE_CASE for properties of the robot hardware, things that are not configurable as code
 * - kPascalCase for configurable values, such as setpoints and gains
 * - comment the unit on any value where it makes sense (gains have units but they're too hard to think about)
 */

public class Control {

    public static class arm {
        public static final double GEAR_RATIO = 1; /// TODO calculate
        public static final Measure<Per<Angle, Angle>> ARM_POS_PER_ENC_POS_UNIT
            = Units.Revolutions.of(GEAR_RATIO).per(Units.Revolutions);
        // FIXME this might be wrong
        public static final Measure<Per<Velocity<Angle>, Velocity<Angle>>> ARM_VEL_PER_ENC_VEL_UNIT
            = Units.RPM.of(GEAR_RATIO).per(Units.RPM);

        public static final Measure<Per<Voltage,          Angle>>  kP_pos
            = Units.Volts.of(1).per(Units.Rotations); /// TODO
        public static final Measure<Per<Voltage, Velocity<Angle>>> kD_pos
            = Units.Volts.of(1).per(Units.RotationsPerSecond); /// TODO

        public static final Measure<         Angle>  kPositionHysteresis
            =  Units.Degrees         .of( 2);   /// TODO
        public static final Measure<Velocity<Angle>> kVelocityHysteresis
            =  Units.DegreesPerSecond.of( 2);  /// TODO

        public static final Measure<Angle> kStartPosition      = Units.Degrees.of( 90);   /// TODO
        public static final Measure<Angle> kFloorPosition      = Units.Degrees.of(  0);   /// TODO
        public static final Measure<Angle> kAmpPosition        = Units.Degrees.of(100);   /// TODO
        public static final Measure<Angle> kSpeakerPosition    = Units.Degrees.of( 30);   /// TODO
        public static final Measure<Angle> kHoldingPosition    = Units.Degrees.of( 20);   /// TODO
        public static final Measure<Angle> kClimbReadyPosition = Units.Degrees.of( 90);   /// TODO
        public static final Measure<Angle> kClimbClumbPosition = Units.Degrees.of( 30);   /// TODO
        public static final Measure<Angle> kMinPosition        = Units.Degrees.of(  0);   /// TODO
        public static final Measure<Angle> kMaxPosition        = Units.Degrees.of(120);   /// TODO

        /**
         * manual control works by incrementing the arm's position setpoint every cycle.
         * this might be a bad idea. TODO consider complexity of moving to a velocity
         * model?
         */
        public static final Measure<Angle> kManualControlDiff  = Units.Degrees.of(  2);   /// TODO
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
