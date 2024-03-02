package frc.robot.auto.routines.templates;

import frc.robot.auto.routines.*;
import frc.robot.auto.routines.three.AmpMAmpMAmp;
import frc.robot.auto.routines.three.AmpMAmpMSpeaker;
import frc.robot.auto.routines.three.AmpMSpeakerMAmp;
import frc.robot.auto.routines.three.AmpMSpeakerMSpeaker;
import frc.robot.auto.routines.three.SpeakerMAmpMAmp;
import frc.robot.auto.routines.three.SpeakerMAmpMSpeaker;
import frc.robot.auto.routines.three.SpeakerMSpeakerMAmp;
import frc.robot.auto.routines.three.SpeakerMSpeakerMSpeaker;

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