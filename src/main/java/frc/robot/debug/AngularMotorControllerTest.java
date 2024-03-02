package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.angular.AngularDcMotorSpeedCurve;
import frc.robot.bodges.rawrlib.angular.AngularFeedbackMotor;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.bodges.rawrlib.generics.DimensionalPIDFGains;
import frc.robot.bodges.rawrlib.motors.WrappedFakeMotor;
import frc.robot.bodges.rawrlib.motors.WrappedTalonFX;
import frc.robot.constants.Ports;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Motors;
import frc.robot.util.Util;

public class AngularMotorControllerTest implements ISubsystem {
  private DimensionalFeedbackMotor<Angle> m;

  private Measure<Velocity<Angle>> ref = Units.RotationsPerSecond.zero();

  public AngularMotorControllerTest() {
    //this.c = ControllerRumble.getInstance(0);
    this.m = new AngularFeedbackMotor()
        .setName("testmotor")
        .setWrappedMotorController(Motors.talonfx.createGroup(Ports.can.drivetrain.LEFTS))
        //.setWrappedMotorController(new WrappedTalonFX(1))
        //.setWrappedMotorController(new WrappedFakeMotor())
        .setFactor(Units.Rotations.of(1).per(Units.Rotations)) // 1 m per rotation, for testing
        .setSpeedCurve(new AngularDcMotorSpeedCurve(Units.Volts.zero(), Units.RotationsPerSecond.of(1/0.12).per(Units.Volts)))
        .configurePositionController(
          new DimensionalPIDFGains<Angle, Voltage>()
          .setP(Units.Volts.of(0.05).per(Units.Rotations))
          .setD(Units.Volts.zero().per(Units.RotationsPerSecond))
          )
        .configureVelocityController(
          new DimensionalPIDFGains<Velocity<Angle>, Voltage>()
          .setF(Units.Volts.of(0.115).per(Units.RotationsPerSecond))
          .setP(Units.Volts.of(0.05).per(Units.RotationsPerSecond))
          .setD(Units.Volts.zero().per(Units.RotationsPerSecond.per(Units.Second)))
          )
        .setVelocity(ref)
    ;
  }

  @Override
  public void onLoop() {
    receiveOptions();
    m.setVelocity(ref);
    m.onLoop();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("r.set", ref.in(Units.RotationsPerSecond));
    SmartDashboard.putNumber("p.set", m.getVelocityController().getGains().getP().in(Units.Volts.per(Units.RotationsPerSecond)));
    SmartDashboard.putNumber("d.set", m.getVelocityController().getGains().getD().in(Units.Volts.per(Units.RotationsPerSecond.per(Units.Second))));
    SmartDashboard.putNumberArray("refstate", new double[] {
      m.getVelocityController().getReference().in(Units.RotationsPerSecond)
      , m.getVelocityController().getState().in(Units.RotationsPerSecond)
    });
    SmartDashboard.putNumberArray("errinput", new double[] {
      m.getVelocityController().getError().in(Units.RotationsPerSecond)
      , m.getLastControlEffort().in(Units.Volts)
    });
    SmartDashboard.putNumber("feedforward", Util.fuzz() + m.getVelocityController().openLoop().in(Units.Volts));
    SmartDashboard.putNumber("reference", Util.fuzz() + m.getVelocityController().getReference().in(Units.RotationsPerSecond));
    SmartDashboard.putNumber("state",     Util.fuzz() + m.getVelocityController().getState().in(Units.RotationsPerSecond));
    SmartDashboard.putNumber("error",     Util.fuzz() + m.getVelocityController().getError().in(Units.RotationsPerSecond));
    SmartDashboard.putNumber("input",     Util.fuzz() + m.getLastControlEffort().in(Units.Volts));
  }

  @Override
  public void receiveOptions() {
    ref = Units.RotationsPerSecond.of(SmartDashboard.getNumber("r.set", ref.in(Units.RotationsPerSecond)));
    m.getVelocityController().getGains().setP(Units.Volts.of(SmartDashboard.getNumber("p.set",
        m.getVelocityController().getGains().getP().in(Units.Volts.per(Units.RotationsPerSecond)))).per(Units.RotationsPerSecond));
    m.getVelocityController().getGains().setD(Units.Volts.of(SmartDashboard.getNumber("d.set",
        m.getVelocityController().getGains().getD().in(Units.Volts.per(Units.RotationsPerSecond.per(Units.Second))))).per(Units.RotationsPerSecond.per(Units.Second)));
  }
}
