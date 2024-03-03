package frc.robot.auto.routines;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.auto.actions.DriveToward;
import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.ScoreAmp;
import frc.robot.auto.actions.ScoreSpeaker;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.game_elements.FieldElements;

public class SpeakerWAmp extends Routine {
    public SpeakerWAmp() {
        super("SpeakerWAmp", 
              new ScoreSpeaker(),
              new DriveToward(new Pose2d(FieldElements.getFieldPoints().kWingNotes.get(2),
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(), 
              new ScoreAmp());
    }
}