package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerW3Amp extends MultiNoteAuto {

    public SpeakerW3Amp() {
      super("SpeakerW3Amp"
      , List.of(ScoreSpeaker::new, ScoreAmp::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(3-1)
        )
      );
    }
  
}
