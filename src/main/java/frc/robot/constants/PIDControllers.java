package frc.robot.constants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.Units;

public class PIDControllers {
    public class arm {
        public static final PIDController kPosition = new PIDController(
                PIDGains.arm.position.kP.in(Units.Volts.per(Units.Rotations)), 0.0,
                PIDGains.arm.position.kD.in(Units.Volts.per(Units.RotationsPerSecond)),
                Misc.kPeriod.in(Units.Seconds));
        public static final PIDController kVelocity = new PIDController(
                PIDGains.arm.velocity.kP.in(Units.Volts.per(Units.RotationsPerSecond)), 0.0,
                PIDGains.arm.velocity.kD.in(Units.Volts.per(Units.RotationsPerSecond.per(Units.Second))),
                Misc.kPeriod.in(Units.Seconds));
    }

    public class drivetrain {
        public static final PIDController kVelocity = new PIDController(
                PIDGains.drivetrain.velocity.kP.in(Units.Volts.per(Units.MetersPerSecond)), 0,
                PIDGains.drivetrain.velocity.kD.in(Units.Volts.per(Units.MetersPerSecondPerSecond)),
                Misc.kPeriod.in(Units.Seconds));
        public static final PIDController kHeading = new PIDController(
                PIDGains.drivetrain.heading.kP.in(Units.DegreesPerSecond.per(Units.Degrees)), 0, 0,
                Misc.kPeriod.in(Units.Seconds));
    }

    public class intake {
        public static final PIDController kVelocity = new PIDController(
                PIDGains.intake.velocity.kP.in(Units.Volts.per(Units.MetersPerSecond)), 0,
                PIDGains.intake.velocity.kD.in(Units.Volts.per(Units.MetersPerSecondPerSecond)),
                Misc.kPeriod.in(Units.Seconds));
    }

    public class shooter {
        public static final PIDController kVelocity = new PIDController(
                PIDGains.shooter.velocity.kP.in(Units.Volts.per(Units.MetersPerSecond)), 0,
                PIDGains.shooter.velocity.kD.in(Units.Volts.per(Units.MetersPerSecondPerSecond)),
                Misc.kPeriod.in(Units.Seconds));
    }
}
