package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.util.Util;

public class Drivetrain extends DifferentialDrive implements ISubsystem {

    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private NetworkTable NT;
    private DoublePublisher p_positionX, p_positionY, p_velocity, p_velocityL, p_velocityR, p_angle, p_angVel;



    private RobotDirection state;
    public enum RobotDirection {
        FORWARD , REVERSED
    }


    private Drivetrain(CANSparkMax left, CANSparkMax right) {
        super(left, right);
        this.leftMotor = left; // TODO add velocity conversion factor
        this.rightMotor = right;
        this.manualControl = true;
        this.state = RobotDirection.FORWARD;
        this.NT = NetworkTableInstance.getDefault().getTable("drivetrain");
        this.p_positionX = this.NT.getDoubleTopic("position").publish();
        this.p_positionY = this.NT.getDoubleTopic("position").publish();
        this.p_velocity = this.NT.getDoubleTopic("velocity").publish();
        this.p_velocityL = this.NT.getDoubleTopic("velocityL").publish();
        this.p_velocityR = this.NT.getDoubleTopic("velocityR").publish();
        this.p_angle = this.NT.getDoubleTopic("angle").publish();
        this.p_angVel = this.NT.getDoubleTopic("angularVelocity").publish();
    }

    private static Drivetrain instance;
    private boolean manualControl;

    public static Drivetrain getInstance() {
        CANSparkMax left, right;
        if (instance == null) {
            left = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.LEFTS, true);
            right = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.RIGHTS, false);
            instance = new Drivetrain(left, right);

        }
        return instance;
    }

    public void setManualControl(boolean _mC) {
        this.manualControl = _mC;
    }

    public void setManualControl() {
        this.setManualControl(true);
    }

    public void setAutomaticControl() {
        this.setManualControl(false);
    }


    public void forward(){

    }

    public void reversed(){
        
    }

    
    private Pose2d pose;

    public Pose2d getPose() {
        return this.pose;
    }

    private void setPose(Pose2d _pose) {
        this.pose = _pose;
    }



    public void curvatureDrive(double xSpeed, double zRotation) {
        if (this.manualControl)
            this.curvatureDrive((xSpeed) * Math.abs(xSpeed), zRotation, Math.abs(xSpeed) < 0.1);
        else {
            // FIXME this isn't a real system; figure out a better architecture
            // maybe we don't even need to track whether it's self/human driving in here?
            // do we? don't we? figure it oot
            System.err.println("[EE] Someone tried to manually control the robot while in self-driving mode");
            new Exception().printStackTrace();
        }
    }


    


    @Override
    public void onLoop() {
        /**
         * FIXME maybe...maybe the other functions just set the variables:
         * - manualControl
         * - speed, rotation
         * - left, right
         * and then this function decides which set of vars to use and how to use them
         * this model reduces the amount of Actual Stuff that Happens outside of the onLoop family
         */
    }



    public double getVelocity() {
        return (getVelocityL() + getVelocityR()) / 2; 
    }

    public double getVelocityL() {
        return this.leftMotor.getEncoder().getVelocity(); 
    }

    public double getVelocityR() {
        return this.rightMotor.getEncoder().getVelocity(); 
    }

    public double getPositionX() {
        return getPose().getX();
    }

    public double getPositionY() {
        return getPose().getY();
    }

    public double getAngle() {
        return getPose().getRotation().getDegrees();
    }

    public double getAngularVelocity() { //TODO
        return 1;
    }



    @Override
    public void submitTelemetry() {
        p_positionX.set(getPositionX());
        p_positionY.set(getPositionY());
        p_velocity.set(getVelocity());
        p_velocityL.set(getVelocityL());
        p_velocityR.set(getVelocityR());
        p_angle.set(getAngle());
        p_angVel.set(getAngularVelocity());
    }

    @Override
    public void receiveOptions() {
        // TODO Auto-generated method stub
    }

    // TODO(justincredible2508,josephreed2600) implement field-relative control
    // scheme

    public void reverse() {
    } // TODO
}