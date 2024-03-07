package frc.robot.subsystems;

import java.util.Map;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.bodges.rawrlib.generics.DimensionalPIDFController;
import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.MechanismDimensions;
import frc.robot.constants.MotorControllers;
import frc.robot.constants.PIDGains;
import frc.robot.util.LimelightHelpers;

import com.kauailabs.navx.frc.AHRS;

public class Drivetrain implements ISubsystem {

    public final DimensionalFeedbackMotor<Distance> leftMotor, rightMotor;
    private Measure<Velocity<Distance>> leftSpeed, rightSpeed;
    private Field2d sb_field;
    private DriveMode mode;
    private Timer timer;
    private AHRS gyro;
    private final DifferentialDrivePoseEstimator poseEstimator;
    private final DimensionalPIDFController<Angle, Velocity<Angle>> headingController = new DimensionalPIDFController<Angle, Velocity<Angle>>();


    private Drivetrain(DimensionalFeedbackMotor<Distance> left, DimensionalFeedbackMotor<Distance> right) {
        this.leftMotor = left;
        this.rightMotor = right;
        this.mode = DriveMode.LOCAL_FORWARD;
        this.timer = new Timer();
        this.gyro = new AHRS(SPI.Port.kMXP);

        // Report pose to Shuffleboard
        this.sb_field = new Field2d();
        SmartDashboard.putData(this.sb_field);

        this.timer.restart();

        this.poseEstimator = new DifferentialDrivePoseEstimator(
            MechanismDimensions.drivetrain.DRIVE_KINEMATICS,
            gyro.getRotation2d(),
            leftMotor.getPosition().in(Units.Meters),
            rightMotor.getPosition().in(Units.Meters),
            new Pose2d(),
            VecBuilder.fill(0.05, 0.05, Units.Degrees.of(5).in(Units.Radians)),
            VecBuilder.fill(0.5, 0.5, Units.Degrees.of(30).in(Units.Radians)));

        this.headingController
        .setStateSupplier(() -> Units.Rotations.of(this.getHeading().getRotations()))
        .setGains(PIDGains.drivetrain.heading)
        ;
    }

    private static Drivetrain instance;
    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain(MotorControllers.drivetrainLeft(), MotorControllers.drivetrainRight());
        }
        return instance;
    }


    public enum DriveMode {
        AUTO, FIELD_FORWARD, FIELD_REVERSED, LOCAL_FORWARD, LOCAL_REVERSED
    }
    public void setDriveMode(DriveMode newMode) {
        mode = newMode;
    }
    public DriveMode getDriveMode() {
        return mode;
    }


    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
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
        DifferentialDriveWheelSpeeds wheelSpeeds = MechanismDimensions.drivetrain.DRIVE_KINEMATICS.toWheelSpeeds(chassisSpeeds);
        leftSpeed  = Units.MetersPerSecond.of(wheelSpeeds. leftMetersPerSecond);
        rightSpeed = Units.MetersPerSecond.of(wheelSpeeds.rightMetersPerSecond);
    }

    public void stop(){
        setTankInputs(Units.MetersPerSecond.zero(), Units.MetersPerSecond.zero());
    }

    private void setTankInputs(Measure<Velocity<Distance>> left, Measure<Velocity<Distance>> right) {
        leftSpeed  = left ;
        rightSpeed = right;
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
        headingController.setReference(Units.Degrees.of(direction.getDegrees()));
        setCurvatureInputs(speed, headingController.calculate());
        //setCurvatureInputs(speed, Units.Rotations.of(direction.minus(getHeading()).getRotations()).per(Units.Second));
    }


    private void sendMotorInputs(Measure<Velocity<Distance>> left, Measure<Velocity<Distance>> right) {
        leftMotor.setVelocity(left);
        rightMotor.setVelocity(right);
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

    public void sealClip(){
        if (this.getVelocity().in(Units.MetersPerSecond) > 0.2){
            sendMotorInputs(Units.MetersPerSecond.of(1), Units.MetersPerSecond.of(0));
        }
    }


    @Override
    public void onLoop() {
        updatePoseFromSensors();
        sendMotorInputs(leftSpeed, rightSpeed);
        leftMotor.onLoop();
        rightMotor.onLoop();
    }

    
    public void localForward(){
        mode = DriveMode.LOCAL_FORWARD;
    }
    public void localReversed(){
        mode = DriveMode.LOCAL_REVERSED;
    }
    public void fieldForward(){
        mode = DriveMode.FIELD_FORWARD;
    }
    public void fieldReversed(){
        mode = DriveMode.FIELD_REVERSED;
    }
    

    private void updatePoseFromSensors() {
        poseEstimator.update(
            gyro.getRotation2d(), 
            leftMotor.getPosition().in(Units.Meters), 
            rightMotor.getPosition().in(Units.Meters)
        );
        
        if (LimelightHelpers.getLatestResults("").targetingResults.targets_Fiducials.length >= 1)
            poseEstimator.addVisionMeasurement(
                    LimelightHelpers.getBotPose2d(""),
                    Timer.getFPGATimestamp());

        /*Measure<Time> elapsed = Units.Seconds.of(timer.get());
        timer.restart();
        if (LimelightHelpers.getLatestResults("").
            targetingResults.targets_Fiducials.length >= 1) {
            setPose(LimelightHelpers.getBotPose2d(""));
        } else {
            setPose(
                getPose().exp(
                    Control.drivetrain.DRIVE_KINEMATICS.toTwist2d(
                        getVelocityL().in(Units.MetersPerSecond) * elapsed.in(Units.Seconds)
                        , getVelocityR().in(Units.MetersPerSecond) * elapsed.in(Units.Seconds)
                    )
                )
            );
        }*/
        //probably don't need to keep this
    }


    public Measure<Velocity<Distance>> getVelocity() {
        return getVelocityL().plus(getVelocityR()).divide(2.0);
    }

    public Measure<Velocity<Distance>> getVelocityL() {
        return leftMotor.getVelocity();
    }

    public Measure<Velocity<Distance>> getVelocityR() {
        return rightMotor.getVelocity();
    }

    public Measure<Distance> getPositionX() {
        return Units.Meters.of(getPose().getX());
    }

    public Measure<Distance> getPositionY() {
        return Units.Meters.of(getPose().getY());
    }

    public Rotation2d getHeading() {
        return getPose().getRotation();
    }

    public Measure<Velocity<Angle>> getAngularVelocity() {
        return Units.DegreesPerSecond.of(gyro.getRate());
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putNumber("dt.positionX", getPositionX().in(Units.Meters));
        SmartDashboard.putNumber("dt.positionY", getPositionY().in(Units.Meters));
        SmartDashboard.putNumber("dt.velocity",  getVelocity() .in(Units.MetersPerSecond));
        SmartDashboard.putNumber("dt.velocityL", getVelocityL().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("dt.velocityR", getVelocityR().in(Units.MetersPerSecond));
        SmartDashboard.putNumber("dt.heading",   getHeading().getDegrees());
        SmartDashboard.putNumber("dt.angVel",    getAngularVelocity().in(Units.DegreesPerSecond));

        sb_field.setRobotPose(getPose());
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