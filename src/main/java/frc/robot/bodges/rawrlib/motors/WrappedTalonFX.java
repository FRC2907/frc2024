package frc.robot.bodges.rawrlib.motors;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.*;

public class WrappedTalonFX extends WrappedMotorController {
  protected TalonFX m;

  public WrappedTalonFX(int deviceId) {
    this(new TalonFX(deviceId));
  }

  public WrappedTalonFX(TalonFX motor) {
    this.m = motor;
    setMotor(m);
    // factory defaults
    m.getConfigurator().apply(new TalonFXConfiguration());
  }

  @Override
  public Measure<Angle> getPosition_downstream() {
    return Units.Rotations.of(m.getPosition().getValueAsDouble());
  }

  @Override
  public Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.RotationsPerSecond.of(m.getVelocity().getValueAsDouble());
  }
  
}