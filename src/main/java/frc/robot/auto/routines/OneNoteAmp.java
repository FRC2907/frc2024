package frc.robot.auto.routines;

import frc.robot.auto.actions.ScoreAmp;
import frc.robot.auto.routines.templates.Routine;

public class OneNoteAmp {
    public class OneNoteSpeaker extends Routine {
    public OneNoteSpeaker() {
        super("OneNoteAmp", 
              new ScoreAmp());
    }
}

}
