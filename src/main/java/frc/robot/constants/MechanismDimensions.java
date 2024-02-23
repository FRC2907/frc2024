package frc.robot.constants;

import org.opencv.core.Scalar;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.units.*;
import frc.robot.util.Util;

/**
 * Style guidance:
 * - SCREAMING_SNAKE_CASE for properties of the robot hardware, things that are
 * not configurable as code
 * - kPascalCase for configurable values, such as setpoints and gains
 * - comment the unit on any value where it makes sense (gains have units but
 * they're too hard to think about)
 */

public class MechanismDimensions {

    public static class electrical {
        private static final Measure<Voltage> MAX_VOLTAGE = Units.Volts.of(12.0);
        public static final Measure<Voltage> kMaxVoltage = Util.clampSymmetrical(Units.Volts.of(12.0), MAX_VOLTAGE);
    }

    public static class arm {
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Per<Angle, Angle>> ARM_TRAVEL_PER_ENCODER_TRAVEL = Units.Revolutions.of(GEAR_RATIO)
                .per(Units.Revolutions);
    }

    public static class drivetrain {
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(6);
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Distance> TRACK_WIDTH = Units.Meters.of(0.5823458);

        public static final Measure<Per<Distance, Angle>> LINEAR_TRAVEL_PER_ENCODER_TRAVEL = WHEEL_DIAMETER
                .times(Math.PI).times(GEAR_RATIO).per(Units.Rotations);

        /** Extra Number for padding track width to compensate for wheel scrub. */
        public static final Measure<Distance> kTrackWidthFudge = Units.Meters.of(0); // TODO
        public static final DifferentialDriveKinematics DRIVE_KINEMATICS = new DifferentialDriveKinematics(
                TRACK_WIDTH.plus(kTrackWidthFudge));

    }

    public static class intake {
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(2);
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Per<Distance, Angle>> LINEAR_TRAVEL_PER_ENCODER_TRAVEL = WHEEL_DIAMETER
                .times(Math.PI).times(GEAR_RATIO).per(Units.Rotations);

    }

    public static class shooter {
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(4);
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Per<Distance, Angle>> LINEAR_TRAVEL_PER_ENCODER_TRAVEL = WHEEL_DIAMETER
                .times(Math.PI).times(GEAR_RATIO).per(Units.Rotations);
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
