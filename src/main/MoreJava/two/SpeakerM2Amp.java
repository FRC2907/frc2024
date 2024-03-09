package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM2Amp extends MultiNoteAuto {

    public SpeakerM2Amp() {
      super("SpeakerM2Amp"
      , List.of(ScoreSpeaker::new, ScoreAmp::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(2-1)
        )
      );
    }
  
}
