package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM3Amp extends MultiNoteAuto {

    public SpeakerM3Amp() {
      super("SpeakerM3Amp"
      , List.of(ScoreSpeaker::new, ScoreAmp::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(3-1)
        )
      );
    }
  
}
