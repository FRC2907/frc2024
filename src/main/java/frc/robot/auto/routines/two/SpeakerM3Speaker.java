package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM3Speaker extends MultiNoteAuto {

    public SpeakerM3Speaker() {
      super("SpeakerM3Speaker"
      , List.of(ScoreSpeaker::new, ScoreSpeaker::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(3-1)
        )
      );
    }
  
}
