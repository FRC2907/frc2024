package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpW2Speaker extends MultiNoteAuto {

    public AmpW2Speaker() {
      super("AmpW2Speaker"
      , List.of(ScoreAmp::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(2-1)
        )
      );
    }
  
}
