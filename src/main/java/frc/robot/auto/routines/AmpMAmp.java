package frc.robot.auto.routines;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.auto.actions.DriveToward;
import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.ScoreAmp;

import frc.robot.auto.routines.templates.Routine;
import frc.robot.constants.game_elements.FieldElements;

public class AmpMAmp extends Routine {
    public AmpMAmp() {
        super("AmpMAmp", 
              new ScoreAmp(),
              new DriveToward(new Pose2d(FieldElements.points.kMidfieldNotes.get(4),
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(),
              new ScoreAmp());
    }
}