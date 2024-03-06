package frc.robot.constants;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.units.*;

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
        public static final Measure<Voltage> MAX_VOLTAGE = Units.Volts.of(12.0);
    }

    public static class arm {
        public static final double GEAR_RATIO = 1; /// TODO[empirical]
        public static final Measure<Per<Angle, Angle>> ARM_TRAVEL_PER_ENCODER_TRAVEL = Units.Revolutions.of(GEAR_RATIO)
                .per(Units.Revolutions);
        public static final Translation3d PIVOT = new Translation3d(-8.458, 0, 10.791);
    }

    public static class drivetrain {
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(6);
        public static final double GEAR_RATIO = 1 / 8.45;
        public static final Measure<Distance> TRACK_WIDTH = Units.Meters.of(0.5823458);

        public static final Measure<Per<Distance, Angle>> LINEAR_TRAVEL_PER_ENCODER_TRAVEL = WHEEL_DIAMETER
                .times(Math.PI).times(GEAR_RATIO).per(Units.Rotations);

        /** Extra Number for padding track width to compensate for wheel scrub. */
        public static final Measure<Distance> kTrackWidthFudge = Units.Meters.of(0); // TODO[empirical]
        public static final DifferentialDriveKinematics DRIVE_KINEMATICS = new DifferentialDriveKinematics(
                TRACK_WIDTH.plus(kTrackWidthFudge));

    }

    public static class intake {
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(2);
        public static final double GEAR_RATIO = 1; /// TODO[empirical]
        public static final Measure<Per<Distance, Angle>> LINEAR_TRAVEL_PER_ENCODER_TRAVEL = WHEEL_DIAMETER
                .times(Math.PI).times(GEAR_RATIO).per(Units.Rotations);

    }

    public static class shooter {
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(4);
        public static final double GEAR_RATIO = 1; /// TODO[empirical]
        public static final Measure<Per<Distance, Angle>> LINEAR_TRAVEL_PER_ENCODER_TRAVEL = WHEEL_DIAMETER
                .times(Math.PI).times(GEAR_RATIO).per(Units.Rotations);
    }

    public static class limelight {
        // TODO anything about the physical position of the limelight can go here
    }

    public static class frame {
        public static final Measure<Distance> INNER_WIDTH = Units.Inches.of(28);
        public static final Measure<Distance> INNER_LENGTH = Units.Inches.of(28.310);
        public static final Measure<Distance> BUMPER_THICKNESS = Units.Inches.of(0.75 + 2.5);
        public static final Measure<Distance> OUTER_WIDTH = INNER_WIDTH.plus(BUMPER_THICKNESS.times(2));
        public static final Measure<Distance> OUTER_LENGTH = INNER_LENGTH.plus(BUMPER_THICKNESS.times(2));
        public static final Measure<Distance> HALF_LENGTH = OUTER_LENGTH.divide(2);
        public static final Measure<Distance> HALF_WIDTH = OUTER_WIDTH.divide(2);
    }
}
