package frc.robot.auto.routines;

import frc.robot.auto.actions.DriveDistanceWithHeading;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.game_elements.FieldElements;

public class DriveAcrossTheLine extends Routine {
    public DriveAcrossTheLine() {
        super("DriveAcrossTheLine"
                //, () -> new DriveToward(new Pose2d(StrategicStructures.acrossTheLineTarget, FieldElements.directions.towardOtherWall()), true)
                // TODO verify whether this is far enough
                , () -> new DriveDistanceWithHeading(FieldElements.lengths.x.blue.kStartingArea, null)
            );
    }
}