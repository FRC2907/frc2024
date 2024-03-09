package frc.robot.auto.routines.one;

import java.util.List;

import frc.robot.auto.actions.ScoreSpeaker;
import frc.robot.auto.routines.templates.MultiNoteAuto;

public class OneNoteSpeaker extends MultiNoteAuto {
    public OneNoteSpeaker() {
        super("OneNoteSpeaker"
        , List.of(ScoreSpeaker::new)
        , List.of()
        );
    }
}