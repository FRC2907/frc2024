package frc.robot.constants;

import frc.robot.bodges.SmartMotorController;
import frc.robot.bodges.SmartMotorController.DownstreamControllerType;

public class MotorControllers {
    public static final SmartMotorController shooter = new SmartMotorController(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.shooter.MOTORS
        , Control.shooter.MOTORS_REVERSED
        , Control.shooter.kMotorConf
    );
    public static final SmartMotorController intake = new SmartMotorController(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.intake.MOTORS
        , Control.shooter.kMotorConf
    );
    public static final SmartMotorController drivetrainLeft = new SmartMotorController(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.drivetrain.LEFTS
        , Control.drivetrain.kLeftMotorConf
    );
    public static final SmartMotorController drivetrainRight = new SmartMotorController(
        DownstreamControllerType.SPARK_MAX_BRUSHLESS
        , Ports.can.drivetrain.RIGHTS
        , Control.drivetrain.kRightMotorConf
    );
}
