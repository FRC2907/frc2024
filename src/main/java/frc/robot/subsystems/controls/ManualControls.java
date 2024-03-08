package frc.robot.subsystems.controls;

import frc.robot.io.GameController;

public class ManualControls extends IControls {

  public ManualControls(int driver, int operator) {
    super(driver, operator);
  }

  public ManualControls(GameController driver, GameController operator) {
    super(driver, operator);
	}

  protected void handleDriver() {
    if (driver.getCircleButtonPressed()) {
      superduperstructure.cancelAction();
    }
    if (driver.getCrossButtonPressed()) {
      superduperstructure.prepareForClimb();
    }

    if (driver.getR1Button()) {
      superduperstructure.moveToAmp();
    }
    if (driver.getR2Button()) {
      superduperstructure.moveToSpeaker();
    }

    if (driver.getR3ButtonPressed()) {
      drivetrain.reverse();
    }
    if (driver.getL3ButtonPressed()) {
      drivetrain.localFieldSwitch();
    }
  }

  protected void handleOperator() {
    if (operator.getCircleButtonPressed()) {
      superduperstructure.cancelAction();
    }
    if (operator.getCrossButtonPressed()) {
      superduperstructure.intakeNote();
      intake.intakeManual();
    }
    if (operator.getCrossButtonReleased()) {
      superduperstructure.neutralPosition();
    }
    if (operator.getSquareButtonPressed()) {
      superduperstructure.autoScore();
    }
    if (operator.getTriangleButtonPressed()) {
      superduperstructure.neutralPosition();
    }

    if (operator.getR1ButtonPressed()) {
      superduperstructure.outakeNote();
    }
    if (operator.getR2ButtonPressed()) {
      shooter.speaker();
    }

    if (operator.getL1ButtonPressed()) {
      shooter.amp();
    }
  }

  public void handleInputs() {
    handleDriver();
    handleOperator();
  }

  @Override
  public void onLoop() {
    receiveOptions();
    handleInputs();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {
  }

  @Override
  public void receiveOptions() {
  }

}
