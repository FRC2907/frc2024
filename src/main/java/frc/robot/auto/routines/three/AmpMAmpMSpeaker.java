package frc.robot.auto.routines.three;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.auto.actions.DriveToward;
import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.ScoreSpeaker;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.game_elements.FieldElements;

public class AmpMAmpMSpeaker extends Routine {
    public AmpMAmpMSpeaker() {
        super("AmpMAmpMSpeaker", 
              new ScoreSpeaker(),
              new DriveToward(new Pose2d(FieldElements.points.kMidfieldNotes.get(4),
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(), 
              new ScoreSpeaker(),
              new DriveToward(new Pose2d(FieldElements.points.kMidfieldNotes.get(3),
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(),
              new ScoreSpeaker());
    }
}