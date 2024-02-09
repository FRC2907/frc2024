package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Intake implements ISubsystem {
    private double setPoint; // wheel rpm

    private CANSparkMax motor;
    private NetworkTable NT;
    private DoublePublisher p_velocity;


    private Intake(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setVelocityConversionFactor(1 / Control.intake.ENCODER_RPM_PER_WHEEL_RPM);
        this.NT = NetworkTableInstance.getDefault().getTable("intake");
        this.p_velocity = this.NT.getDoubleTopic("velocity").publish();
    }

    private static Intake instance;

    public static Intake getInstance() {
        if (instance == null) {
            CANSparkMax motor = Util.createSparkGroup(Ports.can.intake.MOTORS);
            instance = new Intake(motor);
        }
        return instance;
    }
    

    /** Return intake speed in wheel RPM. */
    public double getSpeed() {
        return motor.getEncoder().getVelocity();
    }

    /** Set the desired speed of the intake in wheel RPM. */
    public void setSetPoint(double _setPoint) {
        this.setPoint = _setPoint;
    }

    public void intake(){
        this.setSetPoint(Control.intake.kIntakingRpm);
    }

    public void outake(){
        this.setSetPoint(Control.intake.kIntakingRpm);
    }

    public void off(){
        this.setSetPoint(Control.intake.kOff);
    }

    /** Return whether the intake has a Note in it. */
    public boolean hasNote() {
        // TODO read sensor to determine whether there's a Note in the intake
        return false;
    }

    public double getVelocity(){ 
        return this.motor.getEncoder().getVelocity();
    }

    /** Update motor speed every cycle. */
    @Override
    public void onLoop() {
        this.motor.getPIDController().setReference(this.setPoint, CANSparkMax.ControlType.kVelocity);
    }

    @Override
    public void submitTelemetry() {
        p_velocity.set(getVelocity());
    }

    @Override
    public void receiveOptions() {
        // TODO Auto-generated method stub
    }

}
