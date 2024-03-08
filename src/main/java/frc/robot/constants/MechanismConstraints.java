package frc.robot.constants;

import org.opencv.core.Scalar;

import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.units.*;
import frc.robot.util.Util;

public class MechanismConstraints {

    public static class electrical {
        public static final Measure<Voltage> kMaxVoltage = Util.clampSymmetrical(Units.Volts.of(12.0), MechanismDimensions.electrical.MAX_VOLTAGE);
    }

    public class arm {
        public static final Measure<Angle> kMinPosition        = Units.Degrees.of(  0); /// TODO[empirical]
        public static final Measure<Angle> kMaxPosition        = Units.Degrees.of(120); /// TODO[empirical]
        public static final Measure<Velocity<Angle>> kMaxVelocity = Units.DegreesPerSecond.of(60); /// TODO[empirical]
        public static final Measure<Angle> kPositionHysteresis = Units.Degrees.of(2);
    }

    public class drivetrain {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(0.7); /// TODO[empirical]
        public static final Measure<Velocity<Velocity<Distance>>> kMaxAcceleration = Units.MetersPerSecondPerSecond.of(1); /// TODO[empirical]
        public static final Measure<Velocity<Angle>> kMaxAngularVelocity = Units.RotationsPerSecond.of(0.125); /// TODO[empirical]
        public static final Measure<Velocity<Velocity<Angle>>> kMaxAngularAcceleration = Units.RotationsPerSecond.per(Units.Second).of(0.5); /// TODO[empirical]
        public final static TrajectoryConfig forward_config = 
        new TrajectoryConfig(MechanismConstraints.drivetrain.kMaxVelocity, 
                         MechanismConstraints.drivetrain.kMaxAcceleration)
                         .setReversed(false);
        public final static TrajectoryConfig reverse_config = 
        new TrajectoryConfig(MechanismConstraints.drivetrain.kMaxVelocity, 
                         MechanismConstraints.drivetrain.kMaxAcceleration)
                         .setReversed(true);
        public static final TrajectoryConfig intaking_config = forward_config;
        public static final TrajectoryConfig scoring_config = reverse_config;

        public static final Measure<Distance> kIntakingDistanceInner = MechanismDimensions.frame.OUTER_LENGTH.plus(Units.Inches.of(3)); // TODO[empirical]
        public static final Measure<Distance> kIntakingDistanceOuter = kIntakingDistanceInner.plus(Units.Inches.of(2)); // TODO[empirical]
        public static final Measure<Distance> kIntakingBallparkInner = kIntakingDistanceOuter.plus(Units.Feet.of(2)); // TODO[empirical]
        public static final Measure<Distance> kIntakingBallparkOuter = kIntakingBallparkInner.plus(Units.Feet.of(3)); // TODO[empirical]

        public static final double kDriverDeadband = 0.1;
        public static final boolean kSquareInputs = true;

        public static final Measure<Velocity<Distance>> kSlowVelocity = Units.MetersPerSecond.of(0.5); // TODO[empirical]
    }

    public class intake {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(20); /// TODO[empirical]
        public static final int kPresenceSensorTriggerProximity = 1800; // TODO[empirical]
    }

    public class shooter {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(20); /// TODO[empirical]
        public static final Measure<Velocity<Distance>> kVelocityHysteresis = Units.MetersPerSecond.of(0.1);
    }

    public class camera {
        public static final int kWidth = 320;
        public static final int kHeight = 240;
        public static final Scalar kOrangeLow = new Scalar(6, 66, 75);
        public static final Scalar kOrangeHigh = new Scalar(32, 255, 255);
        public static final double kAreaFilterFactor = 0.075;
        public static final boolean kEnabled = false;
        public static final boolean kNoteTrackingEnabled = true;
        public static final boolean kBlackoutNoteFeed = false;
        public static final boolean kEnableUndistort = true;
        public static final int kTargetLockFrameCountThreshold = 5;
    }

    public static final Measure<Time> kPeriod = Units.Milliseconds.of(20);
}
