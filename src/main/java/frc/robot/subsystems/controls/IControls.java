package frc.robot.subsystems.controls;

import frc.robot.io.GameController;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.ISubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Superduperstructure;

public abstract class IControls implements ISubsystem {

  protected GameController driver, operator;
  protected Superduperstructure superduperstructure;
  protected Shooter shooter;
  protected Intake intake;
  protected Arm arm;
  protected Drivetrain drivetrain;

  public IControls(GameController driver, GameController operator) {
    this.driver = driver;
    this.operator = operator;
  }
  /** This function must be called after creating an instance, to avoid a race condition. */
  public void init() {
    this.superduperstructure = Superduperstructure.getInstance();
    this.shooter = Shooter.getInstance();
    this.intake = Intake.getInstance();
    this.arm = Arm.getInstance();
    this.drivetrain = Drivetrain.getInstance();
  }
  public IControls(int driver, int operator) {
    this(GameController.getInstance(driver), GameController.getInstance(operator));
  }

  public abstract void handleInputs();

  @Override
  public void onLoop() {
    receiveOptions();
    driver.onLoop();
    operator.onLoop();
    handleInputs();
    submitTelemetry();
  }

  @Override
  public void submitTelemetry() {}

  @Override
  public void receiveOptions() {}

}
