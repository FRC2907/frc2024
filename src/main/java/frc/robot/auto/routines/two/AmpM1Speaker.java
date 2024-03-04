package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpM1Speaker extends MultiNoteAuto {

    public AmpM1Speaker() {
      super("AmpM1Speaker"
      , List.of(ScoreAmp::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(1-1)
        )
      );
    }
  
}
