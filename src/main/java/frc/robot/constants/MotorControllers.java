package frc.robot.constants;

import edu.wpi.first.units.*;
import frc.robot.bodges.FakeMotor;
import frc.robot.bodges.FeedbackMotor;
import frc.robot.util.Motors;

public class MotorControllers {
	public static final FeedbackMotor arm = Misc.isCompBot
			? Motors.sparkmax.createOpposedPair(false, Ports.can.arm.MOTORS)
					.setFactor(MechanismDimensions.arm.ARM_TRAVEL_PER_ENCODER_TRAVEL
							.in(Units.Degrees.per(Units.Rotations)))
					.setPositionController(PIDControllers.arm.kPosition)
					.setVelocityController(PIDControllers.arm.kVelocity)
					.setPositionHysteresis(PIDGains.arm.position.kH.in(Units.Degrees),
							PIDGains.arm.velocity.kH.in(Units.DegreesPerSecond))
					.setVelocityHysteresis(PIDGains.arm.velocity.kH.in(Units.DegreesPerSecond))
			: new FakeMotor();

	public static final FeedbackMotor drivetrainLeft = Misc.isCompBot
			? Motors.sparkmax.createGroup(false, Ports.can.drivetrain.LEFTS)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL
							.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.drivetrain.kVelocity)
			: Motors.talonfx.createGroup(Ports.can.drivetrain.LEFTS)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL
							.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.drivetrain.kVelocity);

	public static final FeedbackMotor drivetrainRight = Misc.isCompBot
			? Motors.sparkmax.createGroup(true, Ports.can.drivetrain.RIGHTS)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL
							.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.drivetrain.kVelocity)
			: new FakeMotor();
			//: Motors.talonfx.createGroup(Ports.can.drivetrain.RIGHTS)
			//		.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL
			//				.in(Units.Meters.per(Units.Rotations)))
			//		.setVelocityController(PIDControllers.drivetrain.kVelocity);

	public static final FeedbackMotor intake = Misc.isCompBot
			? Motors.sparkmax.createGroup(false, Ports.can.intake.MOTORS)
					.setFactor(MechanismDimensions.intake.LINEAR_TRAVEL_PER_ENCODER_TRAVEL.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.intake.kVelocity)
			: new FakeMotor();

	public static final FeedbackMotor shooter = Misc.isCompBot
			? Motors.sparkmax.createOpposedPair(false, Ports.can.shooter.MOTORS)
					.setFactor(MechanismDimensions.shooter.LINEAR_TRAVEL_PER_ENCODER_TRAVEL.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.shooter.kVelocity)
			: new FakeMotor();


	public static final FeedbackMotor[] list = {arm, drivetrainLeft, drivetrainRight, intake, shooter};

}
