package frc.robot.auto.routines;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.auto.actions.DriveToward;
import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.ScoreSpeaker;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.constants.game_elements.FieldElements;
import frc.robot.util.Util;

public class SpeakerWSpeaker extends Routine {
    public SpeakerWSpeaker() {
        super("SpeakerWSpeaker", 
              new ScoreSpeaker(),
              new DriveToward(new Pose2d(FieldElements.getFieldPoints().kWingNotes.get(2),
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(), 
              new ScoreSpeaker());
    }
}