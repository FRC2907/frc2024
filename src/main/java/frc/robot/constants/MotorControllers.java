package frc.robot.constants;

import frc.robot.bodges.sillycontroller.DownstreamControllerType;
import frc.robot.bodges.sillycontroller.SmartMotorController_Angular;
import frc.robot.bodges.sillycontroller.SmartMotorController_Linear;

public class MotorControllers {
    public static final SmartMotorController_Angular arm = new SmartMotorController_Angular(
        Misc.isCompBot ? DownstreamControllerType.SPARK_MAX_BRUSHLESS : DownstreamControllerType.NONE
        , Ports.can.arm.MOTORS
        , Control.arm.MOTORS_REVERSED
        , Control.arm.kMotorConf
    );
    public static final SmartMotorController_Linear shooter = new SmartMotorController_Linear(
        Misc.isCompBot ? DownstreamControllerType.SPARK_MAX_BRUSHLESS : DownstreamControllerType.NONE
        , Ports.can.shooter.MOTORS
        , Control.shooter.MOTORS_REVERSED
        , Control.shooter.kMotorConf
    );
    public static final SmartMotorController_Linear intake = new SmartMotorController_Linear(
        Misc.isCompBot ? DownstreamControllerType.SPARK_MAX_BRUSHLESS : DownstreamControllerType.NONE
        , Ports.can.intake.MOTORS
        , Control.shooter.kMotorConf
    );
    public static final SmartMotorController_Linear drivetrainLeft = new SmartMotorController_Linear(
        Misc.isCompBot ? DownstreamControllerType.SPARK_MAX_BRUSHLESS : DownstreamControllerType.TALON_FX
        , Ports.can.drivetrain.LEFTS
        , Control.drivetrain.kLeftMotorConf
    );
    public static final SmartMotorController_Linear drivetrainRight = new SmartMotorController_Linear(
        Misc.isCompBot ? DownstreamControllerType.SPARK_MAX_BRUSHLESS : DownstreamControllerType.NONE
        , Ports.can.drivetrain.RIGHTS
        , Control.drivetrain.kRightMotorConf
    );
}
