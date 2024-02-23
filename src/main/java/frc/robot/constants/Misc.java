package frc.robot.constants;

import edu.wpi.first.units.*;
import frc.robot.subsystems.Drivetrain.DriveMode;

public class Misc {
    public static final boolean isCompBot = false;
    public static final boolean debug = false;
    public static final Measure<Time> kPeriod = Units.Milliseconds.of(20);
    public static final DriveMode kDefaultDriveModeWithoutNote = DriveMode.FIELD_FORWARD;
    public static final DriveMode kDefaultDriveModeWithNote = DriveMode.FIELD_REVERSED;
}
