package frc.robot.auto.routines;

import edu.wpi.first.units.Units;
import frc.robot.auto.actions.JustSomewhere;
import frc.robot.auto.routines.templates.Routine;

public class DriveAcrossTheLineReal extends Routine {
    public DriveAcrossTheLineReal() {
        super("DriveAcrossTheLineReal"
                , () -> new JustSomewhere(Units.MetersPerSecond.of(0.5),
                                          Units.MetersPerSecond.of(0.5), 
                                          Units.Seconds.of(8))
            );
    }
}