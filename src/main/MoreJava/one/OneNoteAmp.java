package frc.robot.auto.routines.one;

import java.util.List;

import frc.robot.auto.actions.ScoreAmp;
import frc.robot.auto.routines.templates.MultiNoteAuto;

public class OneNoteAmp extends MultiNoteAuto {
    public OneNoteAmp() {
        super("OneNoteAmp"
        , List.of(ScoreAmp::new)
        , List.of()
        );
    }
}