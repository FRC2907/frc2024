package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.bodges.rawrlib.generics.DimensionalPIDFGains;
import frc.robot.bodges.rawrlib.linear.LinearDcMotorSpeedCurve;
import frc.robot.bodges.rawrlib.linear.LinearFeedbackMotor;
import frc.robot.bodges.rawrlib.motors.WrappedFakeMotor;
import frc.robot.bodges.rawrlib.motors.WrappedTalonFX;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class LinearMotorControllerTest implements ISubsystem {
  private DimensionalFeedbackMotor<Distance> m;

  private Measure<Distance> ref = Units.Meters.zero();

  public LinearMotorControllerTest() {
    //this.c = ControllerRumble.getInstance(0);
    this.m = new LinearFeedbackMotor()
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
    ;
  }

  @Override
  public void onLoop() {
    receiveOptions();
    m.setPosition(ref);
    m.onLoop();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
    SmartDashboard.putNumber("r.set", ref.in(Units.Meters));
    SmartDashboard.putNumber("p.set", m.getPositionController().getGains().getP().in(Units.Volts.per(Units.Meters)));
    SmartDashboard.putNumber("d.set", m.getPositionController().getGains().getD().in(Units.Volts.per(Units.MetersPerSecond)));
    SmartDashboard.putNumberArray("refstate", new double[] {
      m.getPositionController().getReference().in(Units.Meters)
      , m.getPositionController().getState().in(Units.Meters)
    });
    SmartDashboard.putNumberArray("errinput", new double[] {
      m.getPositionController().getError().in(Units.Meters)
      , m.getLastControlEffort().in(Units.Volts)
    });
    SmartDashboard.putNumber("reference", Util.fuzz() + m.getPositionController().getReference().in(Units.Meters));
    SmartDashboard.putNumber("state",     Util.fuzz() + m.getPositionController().getState().in(Units.Meters));
    SmartDashboard.putNumber("error",     Util.fuzz() + m.getPositionController().getError().in(Units.Meters));
    SmartDashboard.putNumber("input",     Util.fuzz() + m.getLastControlEffort().in(Units.Volts));
  }

  @Override
  public void receiveOptions() {
    ref = Units.Meters.of(SmartDashboard.getNumber("r.set", ref.in(Units.Meters)));
    m.getPositionController().getGains().setP(Units.Volts.of(SmartDashboard.getNumber("p.set",
        m.getPositionController().getGains().getP().in(Units.Volts.per(Units.Meters)))).per(Units.Meters));
    m.getPositionController().getGains().setD(Units.Volts.of(SmartDashboard.getNumber("d.set",
        m.getPositionController().getGains().getD().in(Units.Volts.per(Units.MetersPerSecond)))).per(Units.MetersPerSecond));
  }
}
