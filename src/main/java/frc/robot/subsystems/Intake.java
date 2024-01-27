package frc.robot.subsystems;

public class Intake {
    private double setPoint;
    private double speed;
    private double error;
    private double lastError;

    private Intake(){

    }
    private static Intake instance;
    public static Intake getInstance(){
    if (instance == null)
    instance = new Intake();
    return instance;
    }

    public void onLoop(){
    //detect speed
    //update variables
    //calculate output
    //update the motors
    }

    public void setSetPoint(double _setPoint){
    this.setPoint = _setPoint;
    }

}
