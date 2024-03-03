package frc.robot.constants;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalPIDFGains;

public class PIDGains {
    public class arm {
        public static DimensionalPIDFGains<Angle, Voltage> position = new DimensionalPIDFGains<Angle, Voltage>()
            .setP(Units.Volts.of(0.001).per(Units.Rotations))
            .setD(Units.Volts.of(1).per(Units.RotationsPerSecond))
            ;
        public static DimensionalPIDFGains<Velocity<Angle>, Voltage> velocity = new DimensionalPIDFGains<Velocity<Angle>, Voltage>()
            .setP(Units.Volts.of(0.001).per(Units.RotationsPerSecond))
            .setD(Units.Volts.of(1).per(Units.RotationsPerSecond.per(Units.Second)))
            ;
    }

    public class drivetrain {
        public static DimensionalPIDFGains<Velocity<Distance>, Voltage> velocity = new DimensionalPIDFGains<Velocity<Distance>, Voltage>()
            .setF(Units.Volts.of(2).per(Units.MetersPerSecond))
            .setP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
            ;

        public static DimensionalPIDFGains<Angle, Velocity<Angle>> heading = new DimensionalPIDFGains<Angle, Velocity<Angle>>()
            .setP(Units.DegreesPerSecond.of(2).per(Units.Degrees))
            .setMaxVel(MechanismConstraints.drivetrain.kMaxAngularVelocity)
            .setMaxAccel(MechanismConstraints.drivetrain.kMaxAngularAcceleration)
            ;
    }

    public class intake {
        public static DimensionalPIDFGains<Velocity<Distance>, Voltage> velocity = new DimensionalPIDFGains<Velocity<Distance>, Voltage>()
            .setF(Units.Volts.of(0.115).per(Units.MetersPerSecond))
            .setP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
            ;
    }

    public class shooter {
        public static DimensionalPIDFGains<Velocity<Distance>, Voltage> velocity = new DimensionalPIDFGains<Velocity<Distance>, Voltage>()
            .setF(Units.Volts.of(0.115).per(Units.MetersPerSecond))
            .setP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
            ;
    }
}
