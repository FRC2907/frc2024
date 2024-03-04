package frc.robot.constants;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.bodges.rawrlib.motors.*;
import frc.robot.util.Motors;

public class MotorControllers {
	private static DimensionalFeedbackMotor<Angle> _arm;
	private static DimensionalFeedbackMotor<Distance> _drivetrainLeft, _drivetrainRight, _intake, _shooter;

	public static final DimensionalFeedbackMotor<Angle> arm() {
		if (_arm == null) {
		_arm = new DimensionalFeedbackMotor<Angle>();
			switch (Misc.kActiveRobot) {
				case COMP:
					_arm.setWrappedMotorController(Motors.sparkmax.createOpposedPair(Ports.can.arm.MOTORS));
					break;
				case FLAT:
				case DEBUG:
				default:
					_arm.setWrappedMotorController(new WrappedFakeMotor());
					break;
			}
			_arm
					.setName("arm")
					.setInverted(false)
					.setFactor(MechanismDimensions.arm.ARM_TRAVEL_PER_ENCODER_TRAVEL)
					.configurePositionController(PIDGains.arm.position)
					.configureVelocityController(PIDGains.arm.velocity)
					.setMinPosition(MechanismConstraints.arm.kMinPosition)
					.setMaxPosition(MechanismConstraints.arm.kMaxPosition)
					.setSymmetricalVelocity(MechanismConstraints.arm.kMaxVelocity)
					.getPositionController().setHysteresis(MechanismConstraints.arm.kPositionHysteresis)
					;
		}
		return _arm;
	}

	public static final DimensionalFeedbackMotor<Distance> drivetrainLeft() {
		if (_drivetrainLeft == null) {
			_drivetrainLeft = new DimensionalFeedbackMotor<Distance>();
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainLeft.setWrappedMotorController(Motors.sparkmax.createGroup(Ports.can.drivetrain.LEFTS));
					break;
				case FLAT:
					_drivetrainLeft.setWrappedMotorController(Motors.talonfx.createGroup(Ports.can.drivetrain.LEFTS));
					break;
				case DEBUG:
				default:
					_drivetrainLeft.setWrappedMotorController(new WrappedModelMotor());
					break;
			}
			_drivetrainLeft
					.setName("dt_L")
					.setInverted(false)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.drivetrain.velocity)
					.setSymmetricalVelocity(MechanismConstraints.drivetrain.kMaxVelocity)
					.setSymmetricalAcceleration(MechanismConstraints.drivetrain.kMaxAcceleration)
					;
		}
		return _drivetrainLeft;
	}

	public static final DimensionalFeedbackMotor<Distance> drivetrainRight() {
		if (_drivetrainRight == null) {
			_drivetrainRight = new DimensionalFeedbackMotor<Distance>();
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainRight.setWrappedMotorController(Motors.sparkmax.createGroup(Ports.can.drivetrain.RIGHTS));
					break;
				case FLAT:
					_drivetrainRight.setWrappedMotorController(Motors.talonfx.createGroup(Ports.can.drivetrain.RIGHTS));
					break;
				case DEBUG:
				default:
					_drivetrainRight.setWrappedMotorController(new WrappedFakeMotor());
					break;
			}
			_drivetrainRight
					.setName("dt_R")
					.setInverted(true)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.drivetrain.velocity)
					.setSymmetricalVelocity(MechanismConstraints.drivetrain.kMaxVelocity)
					.setSymmetricalAcceleration(MechanismConstraints.drivetrain.kMaxAcceleration)
					;
		}
		return _drivetrainRight;
	}

	public static final DimensionalFeedbackMotor<Distance> intake() {
		if (_intake == null) {
			_intake = new DimensionalFeedbackMotor<Distance>();
			switch (Misc.kActiveRobot) {
				case COMP:
					_intake.setWrappedMotorController(Motors.sparkmax.createGroup(Ports.can.intake.MOTORS));
					break;
				case FLAT:
				case DEBUG:
				default:
					_intake.setWrappedMotorController(new WrappedFakeMotor());
					break;
			}
			_intake
					.setName("intake")
					.setInverted(false)
					.setFactor(MechanismDimensions.intake.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.intake.velocity);
		}
		return _intake;
	}

	public static final DimensionalFeedbackMotor<Distance> shooter() {
		if (_shooter == null) {
			_shooter = new DimensionalFeedbackMotor<Distance>();
			switch (Misc.kActiveRobot) {
				case COMP:
					_shooter.setWrappedMotorController(Motors.sparkmax.createOpposedPair(Ports.can.shooter.MOTORS));
					break;
				case FLAT:
				case DEBUG:
				default:
					_shooter.setWrappedMotorController(new WrappedFakeMotor());
					break;
			}
			_shooter
					.setName("shooter")
					.setInverted(false)
					.setFactor(MechanismDimensions.shooter.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.shooter.velocity)
					.getVelocityController().setHysteresis(MechanismConstraints.shooter.kVelocityHysteresis)
					;
		}
		return _shooter;
	}

}
