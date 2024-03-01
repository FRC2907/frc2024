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
        public static final Measure<Angle> kMinPosition        = Units.Degrees.of(  0); /// TODO empirical
        public static final Measure<Angle> kMaxPosition        = Units.Degrees.of(120); /// TODO empirical
        public static final Measure<Velocity<Angle>> kMaxVelocity = Units.DegreesPerSecond.of(60); /// TODO empirical
    }

    public class drivetrain {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(1); /// TODO empirical
        public static final Measure<Velocity<Velocity<Distance>>> kMaxAcceleration = Units.MetersPerSecondPerSecond.of(1); /// TODO empirical
        public static final Measure<Velocity<Angle>> kMaxAngularVelocity = Units.RotationsPerSecond.of(0.5); /// TODO empirical
        public static final Measure<Velocity<Velocity<Angle>>> kMaxAngularAcceleration = Units.RotationsPerSecond.per(Units.Second).of(0.5); /// TODO empirical
        public final static TrajectoryConfig config = 
        new TrajectoryConfig(MechanismConstraints.drivetrain.kMaxVelocity, 
                         MechanismConstraints.drivetrain.kMaxAcceleration);
    }

    public class intake {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(20); /// TODO empirical
    }

    public class shooter {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(20); /// TODO empirical
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
    }
}
