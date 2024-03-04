package frc.robot.bodges.rawrlib.motors;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.robot.constants.MechanismConstraints;
import frc.robot.util.Util;;

public class WrappedModelMotor extends WrappedMotorController {

  double voltage = 0;

  public WrappedModelMotor() {
    setMotor(this);
  }
  
  DCMotorSim dcm = new DCMotorSim(LinearSystemId.createDCMotorSystem(2, 0.2), DCMotor.getFalcon500(2), 8.45);

  @Override
  public void setVoltage(double voltage) {
    this.voltage = voltage;
    dcm.setInputVoltage(voltage);
  }

  @Override
  public void onLoop() {
    dcm.update(MechanismConstraints.kPeriod.in(Units.Seconds));
  }

  @Override
  public void set(double speed) { setVoltage(12*Util.clampSymmetrical(speed, 1)); }

  @Override
  public double get() { return voltage; }

  @Override
  public void setInverted(boolean isInverted) { }

  @Override
  public boolean getInverted() { return false; }

  @Override
  public void disable() { }

  @Override
  public void stopMotor() { set(0); }

	@Override
	public Measure<Angle> getPosition_downstream() {
    return Units.Rotations.of(dcm.getAngularPositionRotations());
	}

	@Override
	public Measure<Velocity<Angle>> getVelocity_downstream() {
    return Units.RPM.of(dcm.getAngularVelocityRPM());
	}
}
