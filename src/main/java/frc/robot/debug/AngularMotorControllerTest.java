package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.stuff.AWheeMotor;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MotorControllers;
import frc.robot.io.GameController;
import frc.robot.subsystems.ISubsystem;

public class AngularMotorControllerTest implements ISubsystem {
  private AWheeMotor<Angle> m;
  private GameController c;

  public AngularMotorControllerTest() {
    this.c = GameController.getInstance(0);
    //this.m = SparkMax.of(MotorType.kBrushless, Ports.CAN.arm.LEFT);
    this.m = MotorControllers.arm();
		this.m.setInverted(false) // change this to true if arm goes backwardsies
                                     // if the arm starts getting twisty, give up quick!
					//.setFactor(MechanismDimensions.arm.ARM_TRAVEL_PER_ENCODER_TRAVEL)
					//.setMinPosition(MechanismConstraints.arm.kMinPosition)
					//.setMaxPosition(MechanismConstraints.arm.kMaxPosition)
					//.setSymmetricalVelocity(MechanismConstraints.arm.kMaxVelocity)
					//.setPositionP(PIDGains.arm.position.getP())
					//.setPositionD(PIDGains.arm.position.getD())
					//.setVelocityP(PIDGains.arm.velocity.getP())
					//.setVelocityD(PIDGains.arm.velocity.getD())
					//.setPosition(GameInteractions.arm.kStartPosition)
					;
    //this.m = SparkMax.opposedPairOf(MotorType.kBrushless, Ports.CAN.arm.LEFT, Ports.CAN.arm.RIGHT);
    //this.m = TalonFX.of(Ports.CAN.drivetrain.LEFTS);
    //m.setFactor(Units.Rotations.of(1).per(Units.Rotations))
    //    .setPositionP(Units.Volts.of(0.05).per(Units.Rotations))
    //    .setPositionD(Units.Volts.zero().per(Units.RotationsPerSecond))
    //    .setVelocityF(Units.Volts.of(0.115).per(Units.RotationsPerSecond))
    //    .setVelocityP(Units.Volts.of(0.05).per(Units.RotationsPerSecond))
    //    .setVelocityD(Units.Volts.zero().per(Units.RotationsPerSecond.per(Units.Second)))
    //    .setVelocity(ref);
  }

  @Override
  public void onLoop() {
    receiveOptions();
    //m.setVelocity(ref);
    m.setVoltage(MechanismConstraints.electrical.kMaxVoltage.times(0.25).times(c.getLeftY()));
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("test/UperX'", m.getLastDirectVoltage().in(Units.Volts) / m.getVelocity().in(Units.DegreesPerSecond));
    SmartDashboard.putNumber("test/u", m.getLastDirectVoltage().in(Units.Volts));
    SmartDashboard.putNumber("test/x", m.getPosition().in(Units.Degrees));
    SmartDashboard.putNumber("test/x'", m.getVelocity().in(Units.DegreesPerSecond));
  }

  @Override
  public void receiveOptions() {
  }
}
