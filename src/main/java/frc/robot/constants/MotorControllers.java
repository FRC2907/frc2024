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
			switch (Misc.kActiveRobot) {
				case COMP:
					//_arm = new DimensionalFeedbackMotor<Distance>(Motors.sparkmax.createOpposedPair(Ports.can.arm.MOTORS));
					//break;
				case FLAT:
				case DEBUG:
				default:
					_arm = new DimensionalFeedbackMotor<Angle>(new WrappedFakeMotor());
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
					.setPosition(GameInteractions.arm.kStartPosition)
					.getPositionController().setHysteresis(MechanismConstraints.arm.kPositionHysteresis)
					;
		}
		return _arm;
	}

	public static final DimensionalFeedbackMotor<Distance> drivetrainLeft() {
		if (_drivetrainLeft == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainLeft = new DimensionalFeedbackMotor<Distance>(Motors.sparkmax.createGroup(Ports.CAN.drivetrain.LEFTS));
					break;
				case FLAT:
					_drivetrainLeft = new DimensionalFeedbackMotor<Distance>(Motors.talonfx.createGroup(Ports.CAN.drivetrain.LEFTS));
					break;
				case DEBUG:
				default:
					_drivetrainLeft = new DimensionalFeedbackMotor<Distance>(new WrappedModelMotor());
					break;
			}
			_drivetrainLeft
					.setName("dt_L")
					.setInverted(false)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.drivetrain.velocity)
					.setSymmetricalVelocity(MechanismConstraints.drivetrain.kMaxVelocity)
					.setSymmetricalAcceleration(MechanismConstraints.drivetrain.kMaxAcceleration)
					.setVelocity(Units.MetersPerSecond.zero())
					;
		}
		return _drivetrainLeft;
	}

	public static final DimensionalFeedbackMotor<Distance> drivetrainRight() {
		if (_drivetrainRight == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainRight = new DimensionalFeedbackMotor<Distance>(Motors.sparkmax.createGroup(Ports.CAN.drivetrain.RIGHTS));
					break;
				case FLAT:
					_drivetrainRight = new DimensionalFeedbackMotor<Distance>(Motors.talonfx.createGroup(Ports.CAN.drivetrain.RIGHTS));
					break;
				case DEBUG:
				default:
					_drivetrainRight = new DimensionalFeedbackMotor<Distance>(new WrappedFakeMotor());
					break;
			}
			_drivetrainRight
					.setName("dt_R")
					.setInverted(true)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.drivetrain.velocity)
					.setSymmetricalVelocity(MechanismConstraints.drivetrain.kMaxVelocity)
					.setSymmetricalAcceleration(MechanismConstraints.drivetrain.kMaxAcceleration)
					.setVelocity(Units.MetersPerSecond.zero())
					;
		}
		return _drivetrainRight;
	}

	public static final DimensionalFeedbackMotor<Distance> intake() {
		if (_intake == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					//_intake = new DimensionalFeedbackMotor<Distance>(Motors.sparkmax.createGroup(Ports.can.intake.MOTORS));
					//break;
				case FLAT:
				case DEBUG:
				default:
					_intake = new DimensionalFeedbackMotor<Distance>(new WrappedFakeMotor());
					break;
			}
			_intake
					.setName("intake")
					.setInverted(false)
					.setFactor(MechanismDimensions.intake.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.intake.velocity)
					.setVelocity(GameInteractions.intake.kOff)
					;
		}
		return _intake;
	}

	public static final DimensionalFeedbackMotor<Distance> shooter() {
		if (_shooter == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					//_shooter = new DimensionalFeedbackMotor<Distance>(Motors.sparkmax.createOpposedPair(Ports.can.shooter.MOTORS));
					//break;
				case FLAT:
				case DEBUG:
				default:
					_shooter = new DimensionalFeedbackMotor<Distance>(new WrappedFakeMotor());
					break;
			}
			_shooter
					.setName("shooter")
					.setInverted(false)
					.setFactor(MechanismDimensions.shooter.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.configureVelocityController(PIDGains.shooter.velocity)
					.setVelocity(GameInteractions.shooter.kOff)
					.getVelocityController().setHysteresis(MechanismConstraints.shooter.kVelocityHysteresis)
					;
		}
		return _shooter;
	}

}
