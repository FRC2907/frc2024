package frc.robot.auto.routines;

import frc.robot.auto.actions.SampleAction;
import frc.robot.auto.actions.templates.ParallelAction;
import frc.robot.auto.routines.templates.Routine;

public class SampleRoutine extends Routine {

    public SampleRoutine() {
        super(
            new SampleAction("A")
            , new ParallelAction(
                new SampleAction("B")
                , new SampleAction("C")
            )
            , new SampleAction("D")
        );
    }
    
}
