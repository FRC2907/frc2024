package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Arm {
    private double setPoint;
    private double angle;
    private double error;
    private double lastError;
    private CANSparkMax motor;

    private Arm(CANSparkMax _motor){  
        this.motor = _motor;
    }
    private static Arm instance;
    public static Arm getInstance(){
        if (instance == null){
            CANSparkMax leftMotor = Util.createSparkGroup(Ports.can.arm.LEFTS);
            CANSparkMax rightMotor = Util.createSparkGroup(Ports.can.arm.RIGHTS);
            rightMotor.follow(leftMotor, true);
            instance = new Arm(leftMotor);
        }
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
