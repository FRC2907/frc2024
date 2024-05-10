package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.stuff.AWheeMotor;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MotorControllers;
import frc.robot.io.GameController;
import frc.robot.subsystems.ISubsystem;

public class LinearMotorControllerTest implements ISubsystem {
  private AWheeMotor<Distance> l, r;
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
    this.l = MotorControllers.drivetrainLeft();
    this.r = MotorControllers.drivetrainRight();
  }

  @Override
  public void onLoop() {
    receiveOptions();
    double velocity = c.getRightY();
    double rotation = c.getLeftX();
    double left = (velocity + rotation) / 0.3;
    double right = (velocity - rotation) / 0.3;
    l.setVoltage(MechanismConstraints.electrical.kMaxVoltage.times((left) / 2.0));
    r.setVoltage(MechanismConstraints.electrical.kMaxVoltage.times((right) / 2.0));
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("test/UperX", l.getLastDirectVoltage().in(Units.Volts) / l.getVelocity().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("test/u", l.getLastDirectVoltage().in(Units.Volts));
    SmartDashboard.putNumber("test/x", l.getVelocity().in(Units.MetersPerSecond));
  }

  @Override
  public void receiveOptions() {
  }
}
