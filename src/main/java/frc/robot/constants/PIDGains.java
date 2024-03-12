package frc.robot.constants;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.stuff.GainContainer;

public class PIDGains {
    public class arm {
        public static GainContainer<Angle, Voltage> position = new GainContainer<Angle, Voltage>()
            .setP(Units.Volts.of(0.001).per(Units.Rotations))
            .setD(Units.Volts.of(1).per(Units.RotationsPerSecond))
            ;
        public static GainContainer<Velocity<Angle>, Voltage> velocity = new GainContainer<Velocity<Angle>, Voltage>()
            .setP(Units.Volts.of(0.001).per(Units.RotationsPerSecond))
            .setD(Units.Volts.of(1).per(Units.RotationsPerSecond.per(Units.Second)))
            ;
    }

    public class drivetrain {
        public static GainContainer<Velocity<Distance>, Voltage> velocity = new GainContainer<Velocity<Distance>, Voltage>()
            .setF(Units.Volts.of(2).per(Units.MetersPerSecond))
            .setP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
            ;

        public static GainContainer<Angle, Velocity<Angle>> heading = new GainContainer<Angle, Velocity<Angle>>()
            .setP(Units.DegreesPerSecond.of(2).per(Units.Degrees))
            ;
    }

    public class intake {
        public static GainContainer<Velocity<Distance>, Voltage> velocity = new GainContainer<Velocity<Distance>, Voltage>()
            .setF(Units.Volts.of(0.115).per(Units.MetersPerSecond))
            .setP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
            ;
    }

    public class shooter {
        public static GainContainer<Velocity<Distance>, Voltage> velocity = new GainContainer<Velocity<Distance>, Voltage>()
            .setF(Units.Volts.of(0.115).per(Units.MetersPerSecond))
            .setP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
            ;
    }
}
