package frc.robot.auto.routines;

import frc.robot.auto.actions.DriveDistanceWithHeading;
import frc.robot.auto.routines.templates.Routine;
import frc.robot.game_elements.FieldElements;

public class DriveAcrossTheLine extends Routine {
    public DriveAcrossTheLine() {
        super("DriveAcrossTheLine"
                , () -> new DriveDistanceWithHeading(
                    FieldElements.lengths.x.blue.kStartingArea
                    , FieldElements.directions.towardOtherWall()
                    , true
                    )
            );
    }
}