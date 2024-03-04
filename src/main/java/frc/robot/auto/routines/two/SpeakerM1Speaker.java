package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM1Speaker extends MultiNoteAuto {

    public SpeakerM1Speaker() {
      super("SpeakerM1Speaker"
      , List.of(ScoreSpeaker::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(1-1)
        )
      );
    }
  
}
