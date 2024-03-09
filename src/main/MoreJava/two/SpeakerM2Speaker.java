package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM2Speaker extends MultiNoteAuto {

    public SpeakerM2Speaker() {
      super("SpeakerM2Speaker"
      , List.of(ScoreSpeaker::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(2-1)
        )
      );
    }
  
}
