package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpW2Amp extends MultiNoteAuto {

    public AmpW2Amp() {
      super("AmpW2Amp"
      , List.of(ScoreAmp::new, ScoreAmp::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(2-1)
        )
      );
    }
  
}
