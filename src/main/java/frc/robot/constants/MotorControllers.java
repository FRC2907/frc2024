package frc.robot.constants;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.motors.FakeMotor;
import frc.robot.bodges.rawrlib.raw.FeedbackMotor;
import frc.robot.util.Motors;

public class MotorControllers {
	private static FeedbackMotor _arm, _drivetrainLeft, _drivetrainRight, _intake, _shooter;
	public static final FeedbackMotor[] list = {
		arm()
		, drivetrainLeft()
		, drivetrainRight()
		, intake()
		, shooter()
	};

	public static final FeedbackMotor arm() {
		if (_arm == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_arm = Motors.sparkmax.createOpposedPair(Ports.can.arm.MOTORS);
					break;
				case FLAT:
				case DEBUG:
				default:
					_arm = new FakeMotor();
					break;
			}
			_arm
					.setName("arm")
					.setInverted_(false)
					.setFactor(MechanismDimensions.arm.ARM_TRAVEL_PER_ENCODER_TRAVEL
							.in(Units.Degrees.per(Units.Rotations)))
					.setPositionController(PIDControllers.arm.kPosition)
					.setVelocityController(PIDControllers.arm.kVelocity)
					.setPositionHysteresis(PIDGains.arm.position.kH.in(Units.Degrees),
							PIDGains.arm.velocity.kH.in(Units.DegreesPerSecond))
					.setVelocityHysteresis(PIDGains.arm.velocity.kH.in(Units.DegreesPerSecond));
		}
		return _arm;
	}

	public static final FeedbackMotor drivetrainLeft() {
		if (_drivetrainLeft == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainLeft = Motors.sparkmax.createGroup(Ports.can.drivetrain.LEFTS);
					break;
				case FLAT:
					_drivetrainLeft = Motors.talonfx.createGroup(Ports.can.drivetrain.LEFTS);
					break;
				case DEBUG:
				default:
					_drivetrainLeft = new FakeMotor();
					break;
			}
			_drivetrainLeft
					.setName("dt_L")
					.setInverted_(false)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL
							.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.drivetrain.kVelocity);
		}
		return _drivetrainLeft;
	}

	public static final FeedbackMotor drivetrainRight() {
		if (_drivetrainRight == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainRight = Motors.sparkmax.createGroup(Ports.can.drivetrain.RIGHTS);
					break;
				case FLAT:
					_drivetrainLeft = Motors.talonfx.createGroup(Ports.can.drivetrain.RIGHTS);
					break;
				case DEBUG:
				default:
					_drivetrainRight = new FakeMotor();
					break;
			}
			_drivetrainRight
					.setName("dt_R")
					.setInverted_(true)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL
							.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.drivetrain.kVelocity);
		}
		return _drivetrainRight;
	}

	public static final FeedbackMotor intake() {
		if (_intake == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_intake = Motors.sparkmax.createGroup(Ports.can.intake.MOTORS);
					break;
				case FLAT:
				case DEBUG:
				default:
					_intake = new FakeMotor();
					break;
			}
			_intake
					.setName("intake")
					.setFactor(MechanismDimensions.intake.LINEAR_TRAVEL_PER_ENCODER_TRAVEL.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.intake.kVelocity);
		}
		return _intake;
	}

	public static final FeedbackMotor shooter() {
		if (_shooter == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_shooter = Motors.sparkmax.createGroup(Ports.can.shooter.MOTORS);
					break;
				case FLAT:
				case DEBUG:
				default:
					_shooter = new FakeMotor();
					break;
			}
			_shooter
					.setName("shooter")
					.setFactor(MechanismDimensions.shooter.LINEAR_TRAVEL_PER_ENCODER_TRAVEL.in(Units.Meters.per(Units.Rotations)))
					.setVelocityController(PIDControllers.shooter.kVelocity);
		}
		return _shooter;
	}

}
