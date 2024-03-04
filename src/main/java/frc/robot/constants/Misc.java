package frc.robot.constants;

import frc.robot.subsystems.Drivetrain.DriveMode;

public class Misc {
    public enum Robots { COMP, FLAT, DEBUG }
    public static final Robots kActiveRobot = Robots.DEBUG;
    public static boolean debug = true;
    public static final DriveMode kDefaultDriveModeWithoutNote = DriveMode.FIELD_FORWARD;
    public static final DriveMode kDefaultDriveModeWithNote = DriveMode.FIELD_REVERSED;
    public static boolean kEnableMotionMagic = true;
    public static boolean kEnableStateLimiting = true;
    public static boolean kEnableVelocityLimiting = false;
    public static boolean kEnableAccelerationLimiting = false;
}