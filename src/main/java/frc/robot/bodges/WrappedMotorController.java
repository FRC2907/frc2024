package frc.robot.bodges;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.util.Util;

public abstract class WrappedMotorController implements MotorController {

    protected abstract Measure<Angle> getPosition_downstream();
    protected abstract Measure<Velocity<Angle>> getVelocity_downstream();

  /* everything below this line is boilerplate; you may copy-paste into new files */

  protected MotorController _m;

  protected void setMotor(MotorController motor) {
    this._m = motor;
  }

  @Override
  public void set(double speed) { _m.set(Util.clampSymmetrical(speed, 1)); }

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
