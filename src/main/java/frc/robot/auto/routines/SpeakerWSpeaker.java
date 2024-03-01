package frc.robot.auto.routines;

import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.ScoreSpeaker;
import frc.robot.auto.routines.templates.Routine;

public class SpeakerWSpeaker extends Routine {
    public SpeakerWSpeaker() {
        super("OneNoteSpeaker", 
              new ScoreSpeaker(),
              new GetNote(), 
              new ScoreSpeaker());
    }
}