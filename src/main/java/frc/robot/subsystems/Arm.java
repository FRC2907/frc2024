package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Arm implements ISubsystem {
    private double setPoint;
    private CANSparkMax motor;
    private NetworkTable NT;
    private DoublePublisher p_position, p_velocity;

    private Arm(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setPositionConversionFactor(1 / Control.arm.TICK_PER_DEGREE);
        this.setPDGains(Control.arm.kP, Control.arm.kD);
        this.NT = NetworkTableInstance.getDefault().getTable("arm");
        this.p_position = this.NT.getDoubleTopic("position").publish();
        this.p_velocity = this.NT.getDoubleTopic("velocity").publish();
    }

    private static Arm instance;

    public static Arm getInstance() {
        if (instance == null) {
            CANSparkMax leftMotor = Util.createSparkGroup(Ports.can.arm.LEFTS);
            CANSparkMax rightMotor = Util.createSparkGroup(Ports.can.arm.RIGHTS);
            rightMotor.follow(leftMotor, true);
            instance = new Arm(leftMotor);
        }
        return instance;
    }

    @Override
    public void onLoop() {
        // TODO(justincredible2508@gmail.com) let's look at adding a FF here if we can
        // maybe improve system step response
        // TODO investigate whether it's helpful at all to cache the reference to reduce
        // CAN traffic
        // ^ actually if we were to generalize these classes, we could incorporate ref
        // caching there
        this.motor.getPIDController().setReference(this.setPoint, CANSparkMax.ControlType.kPosition);
    }

    public void setSetPoint(double _setPoint) {
        this.setPoint = Util.clamp(Control.arm.kMinPosition, _setPoint, Control.arm.kMaxPosition);
    }



    public void up() {
        this.setSetPoint(setPoint + Control.arm.kManualControlDiff);
    }

    public void down() {
        this.setSetPoint(setPoint - Control.arm.kManualControlDiff);
    }


    
    public boolean reachedSetPoint() {
        return Math.abs(this.setPoint - this.motor.getEncoder().getPosition()) < Control.arm.kPositionHysteresis
                && Math.abs(this.motor.getEncoder().getVelocity()) < Control.arm.kVelocityHysteresis;
    }



    public void startPosition(){
        this.setSetPoint(Control.arm.kStartPosition);
    }
    public void setPDGains(double P, double D) {
        this.motor.getPIDController().setP(P);
        this.motor.getPIDController().setD(D);
    }
    public void floorPosition(){
        this.setSetPoint(Control.arm.kFloorPosition);
    }
    public void holdingPosition(){
        this.setSetPoint(Control.arm.kHoldingPosition);
    }
    public void ampPosition(){
        this.setSetPoint(Control.arm.kAmpPosition);
    }
    public void speakerPosition(){
        this.setSetPoint(Control.arm.kSpeakerPosition);
    }
    public void climbReadyPosition(){
        this.setSetPoint(Control.arm.kClimbReadyPosition);
    }
    public void clumbPosition(){
        this.setSetPoint(Control.arm.kClimbClumbPosition);
    }
    public void climbClumbPosition(){
        this.setSetPoint(Control.arm.kClimbClumbPosition);
    }



    public double getPosition(){
        return this.motor.getEncoder().getPosition();
    }

    public double getVelocity(){
        return this.motor.getEncoder().getVelocity(); //TODO add velocity conversion factor
    }

    @Override
    public void submitTelemetry() {
        p_position.set(getPosition());
        p_velocity.set(getVelocity());
    }

    @Override
    public void receiveOptions() {
        // TODO Auto-generated method stub
    }
}
