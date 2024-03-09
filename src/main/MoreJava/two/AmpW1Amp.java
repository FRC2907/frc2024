package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpW1Amp extends MultiNoteAuto {

    public AmpW1Amp() {
      super("AmpW1Amp"
      , List.of(ScoreAmp::new, ScoreAmp::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(1-1)
        )
      );
    }
  
}
