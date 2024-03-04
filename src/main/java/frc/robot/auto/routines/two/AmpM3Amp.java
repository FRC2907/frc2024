package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpM3Amp extends MultiNoteAuto {

    public AmpM3Amp() {
      super("AmpM3Amp"
      , List.of(ScoreAmp::new, ScoreAmp::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(3-1)
        )
      );
    }
  
}
