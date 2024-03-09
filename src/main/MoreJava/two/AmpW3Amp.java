package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class AmpW3Amp extends MultiNoteAuto {

    public AmpW3Amp() {
      super("AmpW3Amp"
      , List.of(ScoreAmp::new, ScoreAmp::new)
      , List.of(
          FieldElements.getFieldPoints().kWingNotes.get(3-1)
        )
      );
    }
  
}
