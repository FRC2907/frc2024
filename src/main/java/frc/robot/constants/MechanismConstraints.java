package frc.robot.constants;

import edu.wpi.first.units.*;

public class MechanismConstraints {
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
    }

    public class intake {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(20); /// TODO empirical
    }

    public class shooter {
        public static final Measure<Velocity<Distance>> kMaxVelocity = Units.MetersPerSecond.of(20); /// TODO empirical
    }
}
