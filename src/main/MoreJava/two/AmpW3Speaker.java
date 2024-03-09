package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpW3Speaker extends MultiNoteAuto {

    public AmpW3Speaker() {
      super("AmpW3Speaker"
      , List.of(ScoreAmp::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(3-1)
        )
      );
    }
  
}
