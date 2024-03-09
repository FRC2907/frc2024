package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerW3Speaker extends MultiNoteAuto {

    public SpeakerW3Speaker() {
      super("SpeakerW3Speaker"
      , List.of(ScoreSpeaker::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(3-1)
        )
      );
    }
  
}
