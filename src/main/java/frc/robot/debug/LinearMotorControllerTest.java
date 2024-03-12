package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.stuff.AWheeMotor;
import frc.robot.bodges.rawrlib.stuff.TalonFX;
import frc.robot.constants.Ports;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class LinearMotorControllerTest implements ISubsystem {
  private AWheeMotor<Distance> m;

  private Measure<Velocity<Distance>> ref = Units.MetersPerSecond.zero();

  public LinearMotorControllerTest() {
    // this.c = ControllerRumble.getInstance(0);
    this.m = TalonFX.of(Ports.CAN.drivetrain.LEFTS);
    m.setFactor(Units.Centimeters.of(3).per(Units.Rotations))
        .setPositionP(Units.Volts.of(0.05).per(Units.Meters))
        .setPositionD(Units.Volts.zero().per(Units.MetersPerSecond))
        .setVelocityF(Units.Volts.of(0.115).per(Units.MetersPerSecond))
        .setVelocityP(Units.Volts.of(0.05).per(Units.MetersPerSecond))
        .setVelocityD(Units.Volts.zero().per(Units.MetersPerSecond.per(Units.Second)))
        .setVelocity(ref);
  }

  @Override
  public void onLoop() {
    receiveOptions();
    m.setVelocity(ref);
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("r.set", ref.in(Units.MetersPerSecond));
    SmartDashboard.putNumberArray("refstate", new double[] {
        Util.fuzz() + m.getVelocityReference().in(Units.MetersPerSecond),
        Util.fuzz() + m.getVelocity().in(Units.MetersPerSecond)
    });
    SmartDashboard.putNumber("reference", Util.fuzz() + m.getVelocityReference().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("state", Util.fuzz() + m.getVelocity().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("error", Util.fuzz() + m.getVelocityError().in(Units.MetersPerSecond));
  }

  @Override
  public void receiveOptions() {
    ref = Units.MetersPerSecond.of(SmartDashboard.getNumber("r.set", ref.in(Units.MetersPerSecond)));
  }
}
