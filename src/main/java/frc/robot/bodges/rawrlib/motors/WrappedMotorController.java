package frc.robot.bodges.rawrlib.motors;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.subsystems.ISubsystem;
import frc.robot.util.Util;

public abstract class WrappedMotorController implements MotorController, ISubsystem {

    public abstract Measure<Angle> getPosition_downstream();
    public abstract Measure<Velocity<Angle>> getVelocity_downstream();

  /* everything below this line is boilerplate; you may copy-paste into new files */

  protected MotorController _m;

  protected void setMotor(MotorController motor) {
    this._m = motor;
  }

  @Override
  public void onLoop() {}
  @Override
  public void receiveOptions() {}
  @Override
  public void submitTelemetry() {}

  @Override
  public void set(double speed) { _m.set(Util.clampSymmetrical(speed, 1)); }

  @Override
  public void setVoltage(double voltage) { set(voltage / 12.0); }

  @Override
  public double get() { return _m.get(); }

  @Override
  public void setInverted(boolean isInverted) { _m.setInverted(isInverted); }

  @Override
  public boolean getInverted() { return _m.getInverted(); }

  @Override
  public void disable() { _m.disable(); }

  @Override
  public void stopMotor() { _m.stopMotor(); }
}
