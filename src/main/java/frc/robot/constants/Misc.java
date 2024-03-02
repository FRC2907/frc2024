package frc.robot.constants;

import frc.robot.subsystems.Drivetrain.DriveMode;

public class Misc {
    public enum Robots { COMP, FLAT, DEBUG }
    public static final Robots kActiveRobot = Robots.FLAT;
    public static boolean debug = false;
    public static final DriveMode kDefaultDriveModeWithoutNote = DriveMode.FIELD_FORWARD;
    public static final DriveMode kDefaultDriveModeWithNote = DriveMode.FIELD_REVERSED;
}
