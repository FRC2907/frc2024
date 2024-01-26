package frc.robot.subsystems;

public class Arm {
    private double setPoint;
    private double angle;
    private double error;
    private double lastError;

    private Arm(){  
    }
    private static Arm instance;
    public static Arm getInstance(){
        if (instance == null)
        instance = new Arm();
        return instance;
    }

    public void onLoop(){
        //read angle
        //update variables
        //calculate output
        //update the motors
    }

    public void setSetPoint(double _setPoint){
        this.setPoint = _setPoint;
    }
}
