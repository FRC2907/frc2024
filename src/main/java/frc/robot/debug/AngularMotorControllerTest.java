package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.stuff.AWheeMotor;
import frc.robot.bodges.rawrlib.stuff.TalonFX;
import frc.robot.constants.Ports;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class AngularMotorControllerTest implements ISubsystem {
  private AWheeMotor<Angle> m;

  private Measure<Velocity<Angle>> ref = Units.RotationsPerSecond.zero();

  public AngularMotorControllerTest() {
    // this.c = ControllerRumble.getInstance(0);
    this.m = TalonFX.of(Ports.CAN.drivetrain.LEFTS);
    m.setFactor(Units.Rotations.of(1).per(Units.Rotations))
        .setPositionP(Units.Volts.of(0.05).per(Units.Rotations))
        .setPositionD(Units.Volts.zero().per(Units.RotationsPerSecond))
        .setVelocityF(Units.Volts.of(0.115).per(Units.RotationsPerSecond))
        .setVelocityP(Units.Volts.of(0.05).per(Units.RotationsPerSecond))
        .setVelocityD(Units.Volts.zero().per(Units.RotationsPerSecond.per(Units.Second)))
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
    SmartDashboard.putNumber("r.set", ref.in(Units.RotationsPerSecond));
    SmartDashboard.putNumberArray("refstate", new double[] {
        Util.fuzz() + m.getVelocityReference().in(Units.RotationsPerSecond),
        Util.fuzz() + m.getVelocity().in(Units.RotationsPerSecond)
    });
    SmartDashboard.putNumber("reference", Util.fuzz() + m.getVelocityReference().in(Units.RotationsPerSecond));
    SmartDashboard.putNumber("state", Util.fuzz() + m.getVelocity().in(Units.RotationsPerSecond));
    SmartDashboard.putNumber("error", Util.fuzz() + m.getVelocityError().in(Units.RotationsPerSecond));
  }

  @Override
  public void receiveOptions() {
    ref = Units.RotationsPerSecond.of(SmartDashboard.getNumber("r.set", ref.in(Units.RotationsPerSecond)));
  }
}
