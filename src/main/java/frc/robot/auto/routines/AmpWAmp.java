package frc.robot.auto.routines;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.auto.actions.DriveToward;
import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.ScoreAmp;

import frc.robot.auto.routines.templates.Routine;
import frc.robot.constants.game_elements.FieldElements;
import frc.robot.constants.game_elements.FieldPoints;
import frc.robot.constants.game_elements.StrategicStructures;
import frc.robot.util.Util;

public class AmpWAmp extends Routine {
    public AmpWAmp() {
        super("AmpWAmp", 
              new ScoreAmp(),
              new DriveToward(new Pose2d(Util.isBlue() ? FieldElements.points.blue.kWingNotes : 
                                                         FieldElements.points.red.kWingNotes,
                                         FieldElements.directions.towardOtherWall())),
              new GetNote(),
              new ScoreAmp());
    }
}