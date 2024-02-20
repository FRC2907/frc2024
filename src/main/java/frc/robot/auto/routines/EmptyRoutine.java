package frc.robot.auto.routines;

import frc.robot.auto.actions.LogAction;
import frc.robot.auto.routines.templates.Routine;

public class EmptyRoutine extends Routine {

    public EmptyRoutine() {
        super("Empty"
        , new LogAction("Executing empty routine"));
    }
    
}