package frc.robot.auto.routines.templates;

import frc.robot.auto.routines.*;
import frc.robot.auto.routines.three.*;

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
        //all editable 3-Note templates ->
        new SpeakerMSpeakerMSpeaker();
        new AmpMAmpMAmp();
        new AmpMAmpMSpeaker();
        new SpeakerMSpeakerMAmp();
        new AmpMSpeakerMSpeaker();
        new SpeakerMAmpMAmp();
        new SpeakerMAmpMSpeaker();
        new AmpMSpeakerMAmp();
    }
}