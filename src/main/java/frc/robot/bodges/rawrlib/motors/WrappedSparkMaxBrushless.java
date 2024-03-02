package frc.robot.bodges.rawrlib.motors;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.units.*;

public class WrappedSparkMaxBrushless extends WrappedMotorController {
  protected CANSparkMax m;

  public WrappedSparkMaxBrushless(int deviceId) {
    this(new CANSparkMax(deviceId, MotorType.kBrushless));
  }

  public WrappedSparkMaxBrushless(CANSparkMax motor) {
    this.m = motor;
    setMotor(m);
    m.restoreFactoryDefaults();
  }

  @Override
  public Measure<Angle> getPosition_downstream() {
    return Units.Rotations.of(m.getEncoder().getPosition());
  }

  @Override
  public Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.RPM.of(m.getEncoder().getVelocity());
  }
  
}