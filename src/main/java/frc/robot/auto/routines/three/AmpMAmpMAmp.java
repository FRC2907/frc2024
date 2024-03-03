package frc.robot.auto.routines.three;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.auto.actions.DriveToward;
import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.ScoreAmp;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.game_elements.FieldElements;

public class AmpMAmpMAmp extends Routine {
    public AmpMAmpMAmp() {
        super("AmpMAmpMAmp", 
              new ScoreAmp(),
              new DriveToward(new Pose2d(FieldElements.points.kMidfieldNotes.get(4),
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(), 
              new ScoreAmp(),
              new DriveToward(new Pose2d(FieldElements.points.kMidfieldNotes.get(3),
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(),
              new ScoreAmp());
    }
}