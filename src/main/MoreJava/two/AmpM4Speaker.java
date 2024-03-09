package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpM4Speaker extends MultiNoteAuto {

    public AmpM4Speaker() {
      super("AmpM4Speaker"
      , List.of(ScoreAmp::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(4-1)
        )
      );
    }
  
}
