package frc.robot.constants;

public class Control {
    public static final double kDrivetrainTickPerMeter = 0; // TODO calculate
    public static final double kShooterTickPerRotation = 0; // TODO calculate
    public static final double kIntakeTicksToRotations = 0; // TODO calculate
    public static final double kHatTicksToDiameter = 0; // TODO calculate
    public static final double kHatTicksToDepth = 0; // TODO calculate

    public static class arm {
        public static final double TICK_PER_DEGREE = 0; // TODO calculate
        public static final double kP = 1; // TODO
        public static final double kD = 1; // TODO
        public static final double kStartPosition = 90; // TODO
        public static final double kFloorPosition = 0; // TODO
        public static final double kAmpPosition = 91; // TODO
        public static final double kSpeakerPosition = 30; // TODO
        public static final double kHoldingPosition = 0; // TODO
        public static final double kClimbReadyPosition = 0; // TODO
        public static final double kClimbClumbPosition = 0; // TODO
    }

    public static class drivetrain {

    }

    public static class intake {
        public static final double ENCODER_RPM_PER_WHEEL_RPM = 0; // TODO calculate
        public static final double kP = 1; // TODO
        public static final double kD = 1; // TODO
        public static final double kIntakingRpm = 0; // TODO guess
        public static final double kOff = 0; // TODO be sure
    }

    public static class shooter {

        public static final double ENCODER_RPM_PER_WHEEL_RPM = 0;
        public static final double kAmpRPM = 0; //TODO calculate
        public static final double kSpeakerRPM = 0; //TODO calculate
    }
}
