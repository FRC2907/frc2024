package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.constants.MotorControllers;
import frc.robot.io.ControllerRumble;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class LinearMotorControllerTest implements ISubsystem {
  private DimensionalFeedbackMotor<Distance> m;
  private ControllerRumble c = ControllerRumble.getInstance(0);

  private Measure<Velocity<Distance>> ref = Units.MetersPerSecond.zero();

  public LinearMotorControllerTest() {
  /*  this.m = new DimensionalFeedbackMotor<Distance>()
        .setName("testmotor")
        .setWrappedMotorController(new WrappedTalonFX(1))
        //.setWrappedMotorController(new WrappedFakeMotor())
        .setFactor(Units.Meters.of(1).per(Units.Rotations)) // 1 m per rotation, for testing
        .setSpeedCurve(new LinearDcMotorSpeedCurve(Units.Volts.zero(), Units.MetersPerSecond.of(1/0.12).per(Units.Volts)))
        .configurePositionController(new DimensionalPIDFGains<Distance, Voltage>()
          .setP(Units.Volts.of(0.05).per(Units.Meters))
          .setF(Units.Volts.of(0).per(Units.Meters))
          .setD(Units.Volts.zero().per(Units.MetersPerSecond))
          )
        .setPosition(ref)
    ;*/
    this.m = MotorControllers.drivetrainLeft();
  }

  @Override
  public void onLoop() {
    receiveOptions();
    ref = Units.MetersPerSecond.of(c.getLeftY());
    m.setVelocity(ref);
    m.onLoop();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("r.set", ref.in(Units.MetersPerSecond));
    SmartDashboard.putNumber("p.set", m.getVelocityController().getGains().getP().in(Units.Volts.per(Units.MetersPerSecond)));
    SmartDashboard.putNumber("d.set", m.getVelocityController().getGains().getD().in(Units.Volts.per(Units.MetersPerSecondPerSecond)));
    SmartDashboard.putNumberArray("refstate", new double[] {
      m.getVelocityController().getReference().in(Units.MetersPerSecond)
      , m.getVelocityController().getState().in(Units.MetersPerSecond)
    });
    SmartDashboard.putNumberArray("errinput", new double[] {
      m.getVelocityController().getError().in(Units.MetersPerSecond)
      , m.getLastControlEffort().in(Units.Volts)
    });
    SmartDashboard.putNumber("reference", Util.fuzz() + m.getVelocityController().getReference().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("state",     Util.fuzz() + m.getVelocityController().getState().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("error",     Util.fuzz() + m.getVelocityController().getError().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("input",     Util.fuzz() + m.getLastControlEffort().in(Units.Volts));
  }

  @Override
  public void receiveOptions() {
  }
}
