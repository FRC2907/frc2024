package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.util.Util;

public class Arm implements ISubsystem {
    private Rotation2d setPoint;
    private CANSparkMax motor;
    private DoublePublisher p_position, p_velocity;

    private Arm(CANSparkMax _motor) {
        this.motor = _motor;
        this.motor.getEncoder().setPositionConversionFactor(Control.arm.ARM_REV_PER_ENC_POS_UNIT);
        this.motor.getEncoder().setVelocityConversionFactor(Control.arm.ARM_REV_PER_SEC_PER_ENC_VEL_UNIT);
        this.setPDGains(Control.arm.kP_pos, Control.arm.kD_pos);
        NetworkTable NT = NetworkTableInstance.getDefault().getTable("arm");
        this.p_position = NT.getDoubleTopic("position").publish();
        this.p_velocity = NT.getDoubleTopic("velocity").publish();
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
        // TODO work on motion profiling this / using velocity -> position control
        this.motor.getPIDController().setReference(this.setPoint.getRotations(), CANSparkMax.ControlType.kPosition);
    }


    
    public void setSetPoint(Rotation2d _setPoint) {
        this.setPoint = Util.clamp(Control.arm.kMinPosition, _setPoint, Control.arm.kMaxPosition);
    }
    public Rotation2d getSetPoint() {
        return this.setPoint;
    }



    public void up() {
        this.setSetPoint(setPoint.plus(Control.arm.kManualControlDiff));
    }
    public void down() {
        this.setSetPoint(setPoint.minus(Control.arm.kManualControlDiff));
    }


    
    public boolean reachedSetPoint() {
        return
            Math.abs(setPoint.minus(getPosition()).getDegrees()) < Control.arm.kPositionHysteresis.getDegrees()
         && Math.abs(               getVelocity() .getDegrees()) < Control.arm.kVelocityHysteresis.getDegrees();
    }



    // FIXME if we use this for position and velocity PD control
    // we might need a way to differentiate controllers or something, idk
    public void setPDGains(double P, double D) {
        this.motor.getPIDController().setP(P);
        this.motor.getPIDController().setD(D);
    }
    public void startPosition(){
        this.setSetPoint(Control.arm.kStartPosition);
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



    public Rotation2d getPosition(){
        return Rotation2d.fromRotations(this.motor.getEncoder().getPosition());
    }
    /** Returns the velocity of the arm in Rotation2d-per-second. */
    public Rotation2d getVelocity(){
        return Rotation2d.fromRotations(this.motor.getEncoder().getVelocity());
    }



    @Override
    public void submitTelemetry() {
        p_position.set(getPosition().getDegrees());
        p_velocity.set(getVelocity().getDegrees());
    }

    @Override
    public void receiveOptions() {
    }
}
