package frc.robot.constants;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.stuff.*;

public class MotorControllers {
	private static AWheeMotor<Angle> _arm;
	private static AWheeMotor<Distance> _drivetrainLeft, _drivetrainRight, _intake, _shooter;

	public static final AWheeMotor<Angle> arm() {
		if (_arm == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_arm = SparkMax.opposedPairOf(MotorType.kBrushless, Ports.CAN.arm.LEFT, Ports.CAN.arm.RIGHT);
					break;
				case FLAT:
				case DEBUG:
				default:
					_arm = new FakeMotor<>();
					break;
			}
			_arm
					.setInverted(true)
					.setFactor(MechanismDimensions.arm.ARM_TRAVEL_PER_ENCODER_TRAVEL)
					.setMinPosition(MechanismConstraints.arm.kMinPosition)
					.setMaxPosition(MechanismConstraints.arm.kMaxPosition)
					.setSymmetricalVelocity(MechanismConstraints.arm.kMaxVelocity)
					.setPositionP(PIDGains.arm.position.getP())
					.setPositionD(PIDGains.arm.position.getD())
					.setVelocityP(PIDGains.arm.velocity.getP())
					.setVelocityD(PIDGains.arm.velocity.getD())
					.setPosition(GameInteractions.arm.kStartPosition)
					;
		}
		return _arm;
	}

	public static final AWheeMotor<Distance> drivetrainLeft() {
		if (_drivetrainLeft == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainLeft = SparkMax.of(MotorType.kBrushless, Ports.CAN.drivetrain.LEFTS);
					break;
				case FLAT:
					_drivetrainLeft = TalonFX.of(Ports.CAN.drivetrain.LEFTS);
					break;
				case DEBUG:
				default:
					_drivetrainLeft = new FakeMotor<>();
					break;
			}
			_drivetrainLeft
					.setInverted(false)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.setSymmetricalVelocity(MechanismConstraints.drivetrain.kMaxVelocity)
					.setSymmetricalAcceleration(MechanismConstraints.drivetrain.kMaxAcceleration)
					.setVelocityP(PIDGains.drivetrain.velocity.getP())
					.setVelocityD(PIDGains.drivetrain.velocity.getD())
					.setVelocityF(PIDGains.drivetrain.velocity.getF())
					.setVelocity(Units.MetersPerSecond.zero())
					.zero()
					;
		}
		return _drivetrainLeft;
	}

	public static final AWheeMotor<Distance> drivetrainRight() {
		if (_drivetrainRight == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_drivetrainRight = SparkMax.of(MotorType.kBrushless, Ports.CAN.drivetrain.RIGHTS);
					break;
				case FLAT:
					_drivetrainRight = TalonFX.of(Ports.CAN.drivetrain.RIGHTS);
					break;
				case DEBUG:
				default:
					_drivetrainRight = new FakeMotor<>();
					break;
			}
			_drivetrainRight
					.setInverted(true)
					.setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.setSymmetricalVelocity(MechanismConstraints.drivetrain.kMaxVelocity)
					.setSymmetricalAcceleration(MechanismConstraints.drivetrain.kMaxAcceleration)
					.setVelocityP(PIDGains.drivetrain.velocity.getP())
					.setVelocityD(PIDGains.drivetrain.velocity.getD())
					.setVelocityF(PIDGains.drivetrain.velocity.getF())
					.setVelocity(Units.MetersPerSecond.zero())
					.zero()
					;
		}
		return _drivetrainRight;
	}

	public static final AWheeMotor<Distance> intake() {
		if (_intake == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_intake = SparkMax.of(MotorType.kBrushless, Ports.CAN.intake.MOTORS);
					break;
				case FLAT:
				case DEBUG:
				default:
					_intake = new FakeMotor<>();
					break;
			}
			_intake
					.setInverted(false)
					.setFactor(MechanismDimensions.intake.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.setVelocityP(PIDGains.intake.velocity.getP())
					.setVelocityF(PIDGains.intake.velocity.getF())
					.setVelocity(GameInteractions.intake.kOff)
					;
		}
		return _intake;
	}

	public static final AWheeMotor<Distance> shooter() {
		if (_shooter == null) {
			switch (Misc.kActiveRobot) {
				case COMP:
					_shooter = SparkMax.of(MotorType.kBrushless, Ports.CAN.shooter.MOTORS);
					break;
				case FLAT:
				case DEBUG:
				default:
					_shooter = new FakeMotor<>();
					break;
			}
			_shooter
					.setInverted(false)
					.setFactor(MechanismDimensions.shooter.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
					.setVelocityP(PIDGains.shooter.velocity.getP())
					.setVelocityF(PIDGains.shooter.velocity.getF())
					.setVelocity(GameInteractions.shooter.kOff)
					;
		}
		return _shooter;
	}

}
