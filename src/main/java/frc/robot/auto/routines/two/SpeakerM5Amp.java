package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM5Amp extends MultiNoteAuto {

    public SpeakerM5Amp() {
      super("SpeakerM5Amp"
      , List.of(ScoreSpeaker::new, ScoreAmp::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(5-1)
        )
      );
    }
  
}
