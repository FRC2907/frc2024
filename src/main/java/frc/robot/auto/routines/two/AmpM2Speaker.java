package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpM2Speaker extends MultiNoteAuto {

    public AmpM2Speaker() {
      super("AmpM2Speaker"
      , List.of(ScoreAmp::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(2-1)
        )
      );
    }
  
}
