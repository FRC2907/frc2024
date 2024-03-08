package frc.robot.constants;

import java.util.function.BiFunction;

import frc.robot.io.GameController;
import frc.robot.subsystems.Drivetrain.DriveMode;
import frc.robot.subsystems.controls.*;

public class Misc {
    public enum Robots { COMP, FLAT, DEBUG }
    public static final Robots kActiveRobot = Robots.COMP;
    public static final BiFunction<GameController, GameController, IControls> controllerSetup = ManualControls::new;
    public static boolean debug = true;
    public static final DriveMode kDefaultDriveModeWithoutNote = DriveMode.FIELD_FORWARD;
    public static final DriveMode kDefaultDriveModeWithNote = DriveMode.FIELD_REVERSED;
    public static boolean kEnableMotionMagic = false;
    public static boolean kEnableStateLimiting = true;
    public static boolean kEnableVelocityLimiting = false;
    public static boolean kEnableAccelerationLimiting = false;
}