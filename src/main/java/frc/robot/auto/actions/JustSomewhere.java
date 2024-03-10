package frc.robot.auto.actions;

import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.actions.templates.Action;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Superduperstructure;
import frc.robot.subsystems.Drivetrain.DriveMode;

public class JustSomewhere extends Action{
	Measure<Velocity<Distance>> leftSpeed;
	Measure<Velocity<Distance>> rightSpeed;
	Measure<Time> time;
	Timer timer;
  public JustSomewhere(Measure<Velocity<Distance>> leftSpeed, Measure<Velocity<Distance>> rightSpeed, Measure<Time> time) {
  this.leftSpeed = leftSpeed;
  this.rightSpeed = rightSpeed;
  this.time = time;
  this.timer = new Timer();
  System.out.println("another something");
}

@Override
public void onStart() {
	System.out.println("another other something");
	Superduperstructure.getInstance().neutralPosition();
	Drivetrain.getInstance().setDriveMode(DriveMode.AUTO);
	Drivetrain.getInstance().setAutoDriveInputs(leftSpeed, rightSpeed);
	timer.restart();
	System.out.println("another other other something");
	this.started = true;
	this.running = true;
}

@Override
public void whileRunning() {
	System.out.println("Something" + timer.get());
	if (time.in(Units.Seconds) < timer.get()){
		this.running = false;
		this.finished = true;
	}
}

@Override
public void onCleanup() {
	Drivetrain.getInstance().setAutoDriveInputs(Units.MetersPerSecond.zero(), Units.MetersPerSecond.zero());
}

}