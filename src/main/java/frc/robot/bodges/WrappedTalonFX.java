package frc.robot.bodges;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.*;

public class WrappedTalonFX extends WrappedMotorController {
  protected TalonFX m;

  public WrappedTalonFX(int deviceId) {
    this.m = new TalonFX(deviceId);
    setMotor(m);
    // factory defaults
    m.getConfigurator().apply(new TalonFXConfiguration());
  }

  @Override
  protected Measure<Angle> getPosition_downstream() {
    return Units.Rotations.of(m.getPosition().getValueAsDouble());
  }

  @Override
  protected Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.RotationsPerSecond.of(m.getVelocity().getValueAsDouble());
  }
}
