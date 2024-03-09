package frc.robot.auto.routines;

import edu.wpi.first.units.Units;
import frc.robot.auto.actions.JustSomewhere;
import frc.robot.auto.routines.templates.Routine;

public class DriveAcrossTheLineDance extends Routine {
    public DriveAcrossTheLineDance() {
        super("DriveAcrossTheLineDance"
                , () -> new JustSomewhere(Units.MetersPerSecond.of(0.5),
                                          Units.MetersPerSecond.of(0.5), 
                                          Units.Seconds.of(8))
                , () -> new JustSomewhere(Units.MetersPerSecond.of(0.8), 
                                          Units.MetersPerSecond.of(0),
                                          Units.Seconds.of(7))
            );
    }
}