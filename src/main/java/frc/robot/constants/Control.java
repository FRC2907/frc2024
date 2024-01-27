package frc.robot.constants;

// TODO(hojus000) let's refactor these into per-subsystem classes

public class Control {
    public static final double kDrivetrainTickPerMeter = 0; //TODO calculate
    public static final double kShooterTickPerRotation = 0; //TODO calculate
    public static final double kIntakeTicksToRotations = 0; //TODO calculate
    public static final double kHatTicksToDiameter = 0; //TODO calculate
    public static final double kHatTicksToDepth = 0; //TODO calculate

    public static class arm {
        public static final double TICK_PER_DEGREE = 0; //TODO calculate
        public static final double kP = 1; // TODO
        public static final double kD = 1; // TODO
    }

    public static class drivetrain {

        
    }

    public static class intake {
        public static final double ENCODER_RPM_PER_WHEEL_RPM = 0; // TODO calculate
        public static final double kP = 1; // TODO
        public static final double kD = 1; // TODO
    }

    public static class shooter {

        public static final double ENCODER_RPM_PER_WHEEL_RPM = 0;

        
    }
}
