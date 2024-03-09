package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpM1Amp extends MultiNoteAuto {

    public AmpM1Amp() {
      super("AmpM1Amp"
      , List.of(ScoreAmp::new, ScoreAmp::new)
      , List.of(
          FieldElements.points.kMidfieldNotes.get(1-1)
        )
      );
    }
  
}
