package frc.robot.auto.routines.templates;

import frc.robot.auto.routines.EmptyRoutine;
import frc.robot.auto.routines.SampleRoutine;

public class RoutineInstantiator {
    public static void go() {
        new EmptyRoutine();
        new SampleRoutine();
    }
}
