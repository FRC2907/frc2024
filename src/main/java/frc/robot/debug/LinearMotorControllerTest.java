package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.stuff.AWheeMotor;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MotorControllers;
import frc.robot.io.GameController;
import frc.robot.subsystems.ISubsystem;

public class LinearMotorControllerTest implements ISubsystem {
  private AWheeMotor<Distance> m;
  private GameController c;

  public LinearMotorControllerTest() {
    this.c = GameController.getInstance(0);
    //this.m = TalonFX.of(Ports.CAN.drivetrain.LEFTS);
    //m.setFactor(Units.Centimeters.of(3).per(Units.Rotations))
    //    .setPositionP(Units.Volts.of(0.05).per(Units.Meters))
    //    .setPositionD(Units.Volts.zero().per(Units.MetersPerSecond))
    //    .setVelocityF(Units.Volts.of(0.115).per(Units.MetersPerSecond))
    //    .setVelocityP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
    //    .setVelocityD(Units.Volts.zero().per(Units.MetersPerSecond.per(Units.Second)))
    //    .setVelocity(ref);
    this.m = MotorControllers.drivetrainLeft();
  }

  @Override
  public void onLoop() {
    receiveOptions();
    m.setVoltage(MechanismConstraints.electrical.kMaxVoltage.times(c.getLeftY()));
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("test/UperX", m.getLastDirectVoltage().in(Units.Volts) / m.getVelocity().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("test/u", m.getLastDirectVoltage().in(Units.Volts));
    SmartDashboard.putNumber("test/x", m.getVelocity().in(Units.MetersPerSecond));
  }

  @Override
  public void receiveOptions() {
  }
}
