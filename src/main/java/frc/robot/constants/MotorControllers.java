package frc.robot.constants;

import frc.robot.bodges.SmartMotorController_Linear;
import frc.robot.bodges.SmartMotorController_Linear.DownstreamControllerType;

public class MotorControllers {
    public static final SmartMotorController_Linear shooter = new SmartMotorController_Linear(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.shooter.MOTORS
        , Control.shooter.MOTORS_REVERSED
        , Control.shooter.kMotorConf
    );
    public static final SmartMotorController_Linear intake = new SmartMotorController_Linear(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.intake.MOTORS
        , Control.shooter.kMotorConf
    );
    public static final SmartMotorController_Linear drivetrainLeft = new SmartMotorController_Linear(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.drivetrain.LEFTS
        , Control.drivetrain.kLeftMotorConf
    );
    public static final SmartMotorController_Linear drivetrainRight = new SmartMotorController_Linear(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.drivetrain.RIGHTS
        , Control.drivetrain.kRightMotorConf
    );
}
