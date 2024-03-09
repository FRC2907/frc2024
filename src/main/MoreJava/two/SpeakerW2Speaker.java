package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerW2Speaker extends MultiNoteAuto {

    public SpeakerW2Speaker() {
      super("SpeakerW2Speaker"
      , List.of(ScoreSpeaker::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(2-1)
        )
      );
    }
  
}
