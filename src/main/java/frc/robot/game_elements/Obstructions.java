package frc.robot.game_elements;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.constraint.EllipticalRegionConstraint;
import edu.wpi.first.math.util.Units;

public class Obstructions {
    public EllipticalRegionConstraint blueStage = 
    new EllipticalRegionConstraint(FieldElements.points.blue.kStage,
    Units.inchesToMeters(150), Units.inchesToMeters(150),
    new Rotation2d(), null);

    public EllipticalRegionConstraint redStage = 
    new EllipticalRegionConstraint(FieldElements.points.red.kStage,
    Units.inchesToMeters(150), Units.inchesToMeters(150),
    new Rotation2d(), null);
}
