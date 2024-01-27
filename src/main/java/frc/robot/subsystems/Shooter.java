package frc.robot.subsystems;

public class Shooter {
    private double setPoint;
    private double speed;
    private double error;
    private double lastError;

    private Shooter(){

    }
    private static Shooter instance;
    public static Shooter getInstance(){
        if (instance == null)
        instance = new Shooter();
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
