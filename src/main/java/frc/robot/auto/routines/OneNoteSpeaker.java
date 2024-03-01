package frc.robot.auto.routines;

import frc.robot.auto.actions.ScoreSpeaker;
import frc.robot.auto.routines.templates.Routine;

public class OneNoteSpeaker extends Routine {
    public OneNoteSpeaker() {
        super("OneNote", 
              new ScoreSpeaker());
    }
}
