package frc.robot.constants;

import frc.robot.subsystems.Drivetrain.DriveMode;

public class Misc {
    public enum Robots { COMP, FLAT, DEBUG }
    public static final Robots kActiveRobot = Robots.COMP;
    public static boolean debug = true;
    public static final DriveMode kDefaultDriveModeWithoutNote = DriveMode.LOCAL_FORWARD;
    public static final DriveMode kDefaultDriveModeWithNote = DriveMode.LOCAL_FORWARD;
    public static boolean kEnableMotionMagic = false;
    public static boolean kEnableStateLimiting = true;
    public static boolean kEnableVelocityLimiting = false;
    public static boolean kEnableAccelerationLimiting = false;
}