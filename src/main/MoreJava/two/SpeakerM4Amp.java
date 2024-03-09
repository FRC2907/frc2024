package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM4Amp extends MultiNoteAuto {

    public SpeakerM4Amp() {
      super("SpeakerM4Amp"
      , List.of(ScoreSpeaker::new, ScoreAmp::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(4-1)
        )
      );
    }
  
}
