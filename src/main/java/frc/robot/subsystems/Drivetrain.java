package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.util.Util;


public class Drivetrain extends DifferentialDrive{
    private Drivetrain(CANSparkMax left, CANSparkMax right) {
        super(left, right);
    }
    private static Drivetrain instance;
public static Drivetrain getInstance() {
     CANSparkMax left, right;
    if (instance == null) {
        left = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.LEFTS);
        left.setInverted(true);

        right = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.RIGHTS);
        right.setInverted(false);

        instance = new Drivetrain(left, right);

    }
    return instance;
  
}
public void onLoop(double xSpeed, double zRotation) {
    this.curvatureDrive((xSpeed) * Math.abs(xSpeed), zRotation, Math.abs(xSpeed) < 0.1);{

    }
}
}