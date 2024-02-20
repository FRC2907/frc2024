package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Control;
import frc.robot.util.Util;

public class Drivetrain implements ISubsystem {

    private CANSparkMax leftMotor, rightMotor;
    private Measure<Velocity<Distance>> leftSpeed, rightSpeed;
    private Field2d sb_field;
    private DriveMode mode;


    private Drivetrain(CANSparkMax left, CANSparkMax right) {
        //super(left, right);
        this.leftMotor = left;
        this.rightMotor = right;
        this.mode = DriveMode.LOCAL_FORWARD;

        this.leftMotor .getEncoder().setPositionConversionFactor(
            Control.drivetrain.FLOOR_POS_PER_ENC_POS_UNIT.in(
            Units.Meters.per(Units.Revolutions)));
        this.rightMotor.getEncoder().setPositionConversionFactor(
            Control.drivetrain.FLOOR_POS_PER_ENC_POS_UNIT.in(
            Units.Meters.per(Units.Revolutions)));
        this.leftMotor .getEncoder().setVelocityConversionFactor(
            Control.drivetrain.FLOOR_VEL_PER_ENC_VEL_UNIT.in(
            Units.MetersPerSecond.per(Units.RPM)));
        this.rightMotor.getEncoder().setVelocityConversionFactor(
            Control.drivetrain.FLOOR_VEL_PER_ENC_VEL_UNIT.in(
            Units.MetersPerSecond.per(Units.RPM)));

        // Velocity PD control
        this.leftMotor.getPIDController().setP(
            Control.drivetrain.kP_velocity.in(
            Units.Volts.per(Units.MetersPerSecond)));
        this.leftMotor.getPIDController().setD(
            Control.drivetrain.kD_velocity.in(
            Units.Volts.per(Units.MetersPerSecondPerSecond)));
        this.rightMotor.getPIDController().setP(
            Control.drivetrain.kP_velocity.in(
            Units.Volts.per(Units.MetersPerSecond)));
        this.rightMotor.getPIDController().setD(
            Control.drivetrain.kD_velocity.in(
            Units.Volts.per(Units.MetersPerSecondPerSecond)));

        // Report pose and drivetrain stats to Shuffleboard
        this.sb_field = new Field2d();
        SmartDashboard.putData(this.sb_field);
        //SmartDashboard.putData(this);
    }

    private static Drivetrain instance;
    public static Drivetrain getInstance() {
        CANSparkMax left, right;
        if (instance == null) {
            left = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.LEFTS, true);
            right = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.RIGHTS, false);
            instance = new Drivetrain(left, right);

        }
        return instance;
    }


    public enum DriveMode {
        AUTO, FIELD_FORWARD, FIELD_REVERSED, LOCAL_FORWARD, LOCAL_REVERSED
    }
    public void setDriveMode(DriveMode newMode) {
        this.mode = newMode;
    }
    public DriveMode getDriveMode() {
        return this.mode;
    }


    private Pose2d pose = new Pose2d(); // FIXME this might be bad. or maybe not

    public Pose2d getPose() {
        return this.pose;
    }
    private void setPose(Pose2d _pose) {
        this.pose = _pose;
    }

    public Rotation2d getHeadingError(Rotation2d reference) {
        return reference.minus(getHeading());
    }


    /**
     * Generate wheel speeds with well-defined units from a target chassis speed and
     * angular velocity.
     * 
     * @param speed    desired overall chassis speed
     * @param rotation desired chassis angular velocity
     */
    private void setCurvatureInputs(Measure<Velocity<Distance>> speed, Measure<Velocity<Angle>> rotation) {
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(
            speed.in(Units.MetersPerSecond)
            , 0.0
            , rotation.in(Units.RadiansPerSecond)
        );
        DifferentialDriveWheelSpeeds wheelSpeeds = Control.drivetrain.DRIVE_KINEMATICS.toWheelSpeeds(chassisSpeeds);
        this.leftSpeed  = Units.MetersPerSecond.of(wheelSpeeds. leftMetersPerSecond);
        this.rightSpeed = Units.MetersPerSecond.of(wheelSpeeds.rightMetersPerSecond);
    }

    private void setTankInputs(Measure<Velocity<Distance>> left, Measure<Velocity<Distance>> right) {
        this.leftSpeed  = left ;
        this.rightSpeed = right;
    }
    

    public void setAutoDriveInputs(Measure<Velocity<Distance>> left, Measure<Velocity<Distance>> right) {
        setTankInputs(left, right);
    }
    public void setAutoDriveInputs(Map<String,Measure<Velocity<Distance>>> speeds) {
        setAutoDriveInputs(speeds.get("left"), speeds.get("right"));
    }
    public void setLocalDriveInputs(Measure<Velocity<Distance>> speed, Measure<Velocity<Angle>> turn) {
        setCurvatureInputs(speed, turn);
    }
    public void setFieldDriveInputs(Measure<Velocity<Distance>> speed, Rotation2d direction) {
        setCurvatureInputs(speed, 
            Units.DegreesPerSecond.of(
                // this is goofy
                // we're just fighting the type system
                Control.drivetrain.kP_fieldRelativeHeading
                // deg/s / deg => 1/s
                .in(Units.DegreesPerSecond.per(Units.Degrees))
                // then (1/s) * deg => deg/s
                * getHeadingError(direction).getDegrees()
                // which is a unit of angular velocity
                // which is what we want :)
            )
        );
    }


    private void sendMotorInputs(Measure<Velocity<Distance>> left, Measure<Velocity<Distance>> right) {
        sendMotorInputs(left.in(Units.MetersPerSecond), right.in(Units.MetersPerSecond));
    }
    /**
     * Send motor inputs in m/s.
     */
    private void sendMotorInputs(double left, double right) {
        double[] normalSpeeds = Util.normalizeSymmetrical(
            Control.drivetrain.kMaxSpeed.in(Units.MetersPerSecond)
            , left, right);
        leftMotor .getPIDController().setReference(normalSpeeds[0], CANSparkMax.ControlType.kVelocity);
        rightMotor.getPIDController().setReference(normalSpeeds[1], CANSparkMax.ControlType.kVelocity);
    }


    /**
     * Other functions outside this class should interact by calling these helper
     * functions:
     * 
     * <ul>
     * <li>setAutoDriveInputs
     * <li>setLocalDriveInputs
     * <li>setFieldDriveInputs
     * </ul>
     * 
     * The above helpers set up internal variables (leftSpeed, rightSpeed) which
     * we assign to the motors in onLoop().
     */

    // reversed means basically changing direction rather than like a car reverse

    @Override
    public void onLoop() {
        updatePoseFromSensors();
        sendMotorInputs(leftSpeed, rightSpeed);
    }

    
    public void localForward(){
        this.mode = DriveMode.LOCAL_FORWARD;
    }
    public void localReversed(){
        this.mode = DriveMode.LOCAL_REVERSED;
    }
    public void fieldForward(){
        this.mode = DriveMode.FIELD_FORWARD;
    }
    public void fieldReversed(){
        this.mode = DriveMode.FIELD_REVERSED;
    }
    

    private void updatePoseFromSensors() {
        // TODO(justinho) implement
        if (8 == 9/* if we have April tag info */){
            //set pose
        } else {
            setPose(getPose().exp(Control.drivetrain.DRIVE_KINEMATICS.toTwist2d(
                   (getVelocityL() * 0.02), (getVelocityR() * 0.02))));
        }
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

    public Rotation2d getHeading() {
        return getPose().getRotation();
    }

    public double getAngularVelocity() { // TODO
        return 1;
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("dt.positionX", getPositionX());
        SmartDashboard.putNumber("dt.positionY", getPositionY());
        SmartDashboard.putNumber("dt.velocity",  getVelocity());
        SmartDashboard.putNumber("dt.velocityL", getVelocityL());
        SmartDashboard.putNumber("dt.velocityR", getVelocityR());
        SmartDashboard.putNumber("dt.heading",   getHeading().getDegrees());
        SmartDashboard.putNumber("dt.angVel",    getAngularVelocity());

        // TODO verify
        this.sb_field.setRobotPose(getPose());
    }

    @Override
    public void receiveOptions() {
    }

    public void reverse() {
        if (mode == DriveMode.LOCAL_FORWARD)
            mode = DriveMode.LOCAL_REVERSED;
        else if (mode == DriveMode.LOCAL_REVERSED)
            mode = DriveMode.LOCAL_FORWARD;
        else if (mode == DriveMode.FIELD_FORWARD)
            mode = DriveMode.FIELD_REVERSED;
        else if (mode == DriveMode.FIELD_REVERSED)
            mode = DriveMode.FIELD_FORWARD;
    }
    public void localFieldSwitch() {
        if (mode == DriveMode.LOCAL_FORWARD)
            mode = DriveMode.FIELD_FORWARD;
        else if (mode == DriveMode.LOCAL_REVERSED)
            mode = DriveMode.FIELD_REVERSED;
        else if (mode == DriveMode.FIELD_FORWARD)
            mode = DriveMode.LOCAL_FORWARD;
        else if (mode == DriveMode.FIELD_REVERSED)
            mode = DriveMode.LOCAL_REVERSED;
    }
}