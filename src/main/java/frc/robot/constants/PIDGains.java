package frc.robot.constants;

import edu.wpi.first.units.*;

public class PIDGains {
    public class arm {
        public class position {
            public static final Measure<Per<Dimensionless, Angle>> kP = Units.Value.of(1)
                    .per(Units.Rotations);
            public static final Measure<Per<Dimensionless, Velocity<Angle>>> kD = Units.Value.of(1)
                    .per(Units.RotationsPerSecond);
            /** Hysteresis */
            public static final Measure<Angle> kH = Units.Degrees.of(2);
        }

        public class velocity {
            public static final Measure<Per<Dimensionless, Velocity<Angle>>> kP = Units.Value.of(1)
                    .per(Units.RotationsPerSecond);
            public static final Measure<Per<Dimensionless, Velocity<Velocity<Angle>>>> kD = Units.Value.of(1)
                    .per(Units.RotationsPerSecond.per(Units.Second));
            /** Hysteresis */
            public static final Measure<Velocity<Angle>> kH = Units.DegreesPerSecond.of(2);
        }
    }

    public class drivetrain {
        public class position {
        }

        public class velocity {
            public static final Measure<Per<Dimensionless, Velocity<Distance>>> kP = Units.Value.of(1)
                    .per(Units.MetersPerSecond);
            public static final Measure<Per<Dimensionless, Velocity<Velocity<Distance>>>> kD = Units.Value.of(1)
                    .per(Units.MetersPerSecondPerSecond);
        }

        public class heading {
            public static final Measure<Per<Velocity<Angle>, Angle>> kP = Units.DegreesPerSecond
                    .of(2).per(Units.Degrees);
        }
    }

    public class intake {
        public class velocity {
            public static final Measure<Per<Dimensionless, Velocity<Distance>>> kP = Units.Value.of(1)
                    .per(Units.MetersPerSecond);
            public static final Measure<Per<Dimensionless, Velocity<Velocity<Distance>>>> kD = Units.Value.of(1)
                    .per(Units.MetersPerSecondPerSecond);
        }
    }

    public class shooter {
        public class velocity {
            public static final Measure<Per<Dimensionless, Velocity<Distance>>> kP = Units.Value.of(1)
                    .per(Units.MetersPerSecond);
            public static final Measure<Per<Dimensionless, Velocity<Velocity<Distance>>>> kD = Units.Value.of(1)
                    .per(Units.MetersPerSecondPerSecond);
        }
    }
}
