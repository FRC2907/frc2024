package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class SpeakerM1Amp extends MultiNoteAuto {

    public SpeakerM1Amp() {
      super("SpeakerM1Amp"
      , List.of(ScoreSpeaker::new, ScoreAmp::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(1-1)
        )
      );
    }
  
}
