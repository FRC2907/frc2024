package frc.robot.auto.routines;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.auto.actions.DriveToward;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.constants.game_elements.FieldElements;
import frc.robot.constants.game_elements.StrategicStructures;

public class DriveAcrossTheLine extends Routine {
    public DriveAcrossTheLine() {
        super("DriveAcrossTheLine",
            new DriveToward(new Pose2d(StrategicStructures.acrossTheLineTarget, 
                                       FieldElements.directions.towardOtherWall())));
    }
}

