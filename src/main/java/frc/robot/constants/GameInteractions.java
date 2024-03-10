package frc.robot.constants;

import edu.wpi.first.units.*;

public class GameInteractions {
    public class arm {
        public static final Measure<Angle> kStartPosition = Units.Degrees.of(90); /// TODO[empirical]
        public static final Measure<Angle> kFloorPosition = Units.Degrees.of(0); /// TODO[empirical]
        public static final Measure<Angle> kAmpPosition = Units.Degrees.of(100); /// TODO[empirical]
        public static final Measure<Angle> kHoldingPosition = Units.Degrees.of(20); /// TODO[empirical]
        public static final Measure<Angle> kClimbReadyPosition = Units.Degrees.of(90); /// TODO[empirical]
        public static final Measure<Angle> kClimbClumbPosition = Units.Degrees.of(30); /// TODO[empirical]
        public static final Measure<Angle> kSelfRightingPosition = MechanismConstraints.arm.kMaxPosition; /// TODO[empirical]

        public static final Measure<Velocity<Angle>> kManualControlSpeed = Units.DegreesPerSecond.of(10); /// TODO[empirical]
    }

    public class intake {
        public static final Measure<Velocity<Distance>> kIntakingSpeed = Units.MetersPerSecond.of(0.5); /// TODO[empirical]
        public static final Measure<Velocity<Distance>> kManualIntakingSpeed = Units.MetersPerSecond.of(0.25); /// TODO[empirical]
        public static final Measure<Velocity<Distance>> kOutakingSpeed = Units.MetersPerSecond.of(-0.7); /// TODO[empirical]
        public static final Measure<Velocity<Distance>> kOff = Units.MetersPerSecond.zero(); /// TODO[empirical]
    }

    public class shooter {
        public static final Measure<Velocity<Distance>> kAmpSpeed = Units.MetersPerSecond.of(1); /// TODO[empirical]
        public static final Measure<Velocity<Distance>> kOff = Units.MetersPerSecond.zero(); /// TODO[empirical]
    }
}
