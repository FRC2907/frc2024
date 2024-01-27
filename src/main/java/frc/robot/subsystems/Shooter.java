package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Shooter {
    private double setPoint;
    private double speed;
    private double error;
    private double lastError;

    private CANSparkMax motor;

    private Shooter(CANSparkMax _motor){
        this.motor = _motor;
    }
    private static Shooter instance;
    public static Shooter getInstance(){
        if (instance == null) {
            CANSparkMax motor = Util.createSparkGroup(Ports.can.intake.MOTORS);
            instance = new Shooter(motor);
        }
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
