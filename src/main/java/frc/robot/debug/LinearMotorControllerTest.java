package frc.robot.debug;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.bodges.rawrlib.motors.WrappedFakeMotor;
//this never used 
import frc.robot.bodges.rawrlib.motors.WrappedModelMotor;
import frc.robot.bodges.rawrlib.motors.WrappedMotorController;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MechanismDimensions;
import frc.robot.constants.PIDGains;
import frc.robot.io.ControllerRumble;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public class LinearMotorControllerTest implements ISubsystem {
  private DimensionalFeedbackMotor<Distance> m;
  //The value of the field LinearMotorControllerTest.c is not used Java(570425421)
  private ControllerRumble c = ControllerRumble.getInstance(0);
  private Measure<Velocity<Distance>> ref = Units.MetersPerSecond.zero();


  public LinearMotorControllerTest() {
    WrappedMotorController motor = new WrappedFakeMotor();
    m = new DimensionalFeedbackMotor<Distance>()
      .setWrappedMotorController(motor)
      .setName("testmotor")
      .setInverted(false)
      .setFactor(MechanismDimensions.drivetrain.LINEAR_TRAVEL_PER_ENCODER_TRAVEL)
      .configureVelocityController(PIDGains.drivetrain.velocity)
      .setSymmetricalVelocity(MechanismConstraints.drivetrain.kMaxVelocity)
      .setSymmetricalAcceleration(MechanismConstraints.drivetrain.kMaxAcceleration)
      ;
  }

  @Override
  public void onLoop() {
    receiveOptions();
    //ref = Units.MetersPerSecond.of(c.getLeftY());
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
    SmartDashboard.putNumber("reference raw", Util.fuzz() + m.getVelocityController().getReference_unbounded().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("reference", Util.fuzz() + m.getVelocityController().getReference().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("state",     Util.fuzz() + m.getVelocityController().getState().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("error",     Util.fuzz() + m.getVelocityController().getError().in(Units.MetersPerSecond));
    SmartDashboard.putNumber("input",     Util.fuzz() + m.getLastControlEffort().in(Units.Volts));
  }

  @Override
  public void receiveOptions() {
    ref = Units.MetersPerSecond.of(SmartDashboard.getNumber("r.set", ref.in(Units.MetersPerSecond)));
  }
}
