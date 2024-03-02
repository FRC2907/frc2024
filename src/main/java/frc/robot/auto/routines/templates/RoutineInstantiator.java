package frc.robot.auto.routines.templates;

import frc.robot.auto.routines.*;

public class RoutineInstantiator {
    public static void go() {
        new EmptyRoutine();
        new SampleRoutine();
        new DriveAcrossTheLine();
        new OneNoteAmp();
        new OneNoteSpeaker();
        new SpeakerWSpeaker();
        new AmpWAmp();
        new SpeakerWAmp();
        new AmpWSpeaker();
    }
}