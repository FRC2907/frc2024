package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerW2Amp extends MultiNoteAuto {

    public SpeakerW2Amp() {
      super("SpeakerW2Amp"
      , List.of(ScoreSpeaker::new, ScoreAmp::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(2-1)
        )
      );
    }
  
}
