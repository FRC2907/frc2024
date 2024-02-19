package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Control;
import frc.robot.util.Util;

public class Drivetrain extends DifferentialDrive implements ISubsystem {

    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private double leftSpeed, rightSpeed, totalSpeed, turningness;
    private Rotation2d desiredHeading;
    private Field2d sb_field;
    private DriveMode mode;


    private Drivetrain(CANSparkMax left, CANSparkMax right) {
        super(left, right);
        this.leftMotor = left; // TODO add velocity conversion factor
        this.rightMotor = right;
        this.mode = DriveMode.LOCAL_FORWARD;

        this.leftMotor.getEncoder().setPositionConversionFactor(Control.drivetrain.METER_PER_ENC_POS_UNIT);
        this.rightMotor.getEncoder().setPositionConversionFactor(Control.drivetrain.METER_PER_ENC_POS_UNIT);
        this.leftMotor.getEncoder().setVelocityConversionFactor(Control.drivetrain.METER_PER_SEC_PER_ENC_VEL_UNIT);
        this.rightMotor.getEncoder().setVelocityConversionFactor(Control.drivetrain.METER_PER_SEC_PER_ENC_VEL_UNIT);

        this.sb_field = new Field2d();
        SmartDashboard.putData(this.sb_field);
        SmartDashboard.putData(this);
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


    // TODO revisit whether that turnInPlace parameter makes sense
    public void curvatureDrive(double xSpeed, double zRotation) {
        this.curvatureDrive((xSpeed) * Math.abs(xSpeed), zRotation, Math.abs(xSpeed) < 0.1);
    }


    public void setTankInputs(double left, double right) {
        this.leftSpeed = left;
        this.rightSpeed = right;
    }
    public void setTankInputs(double[] wheelSpeeds) {
        setTankInputs(wheelSpeeds[0], wheelSpeeds[1]);
    }
    public void setLocalDriveInputs(double speed, double turn) {
        this.totalSpeed = speed;
        this.turningness = turn;
    }
    public void setFieldDriveInputs(double magnitude, Rotation2d direction) {
        this.totalSpeed = magnitude;
        this.desiredHeading = direction;
    }


    private double convertHeadingToTurningness(Rotation2d current, Rotation2d desired) {
        return Control.drivetrain.kP_fieldRelativeHeading * desired.minus(current).getRadians(); // TODO verify
    }


    /**
     * Other functions outside this class should interact by calling the helper
     * functions to set these variables:
     * <ul>
     * <li>manualControl
     * <li>local mode: speed, rotation
     * <li>field mode: speed, heading
     * <li>tank/follower mode: leftSpeed, rightSpeed
     * </ul>
     *
     * This function decides which set of vars to use and how to use them. This
     * model reduces the amount of Actual Stuff that Happens outside of the onLoop
     * family.
     */

    // reversed means basically changing direction rather than like a car reverse

    @Override
    public void onLoop() {
        switch (this.mode) {
            case AUTO:
                double[] speeds = new double[] { leftSpeed, rightSpeed };
                normalize(speeds);
                this.tankDrive(speeds[0], speeds[1], false);
                break;

            case LOCAL_FORWARD:
                this.curvatureDrive(totalSpeed, turningness);
                break;

            case LOCAL_REVERSED:
                this.curvatureDrive(-totalSpeed, -turningness);
                break;

            case FIELD_FORWARD:
                this.curvatureDrive(totalSpeed, convertHeadingToTurningness(this.getHeading(), desiredHeading));
                break;
            case FIELD_REVERSED:
                this.curvatureDrive(-totalSpeed, convertHeadingToTurningness(
                        this.getHeading(), desiredHeading.minus(Rotation2d.fromRotations(0.5)))); // TODO verify but I
                                                                                                  // think this is
                                                                                                  // right?
                break;
            default:
                break;
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
        SmartDashboard.putNumber("dt.velocity", getVelocity());
        SmartDashboard.putNumber("dt.velocityL", getVelocityL());
        SmartDashboard.putNumber("dt.velocityR", getVelocityR());
        SmartDashboard.putNumber("dt.heading", getHeading().getDegrees());
        SmartDashboard.putNumber("dt.angVel", getAngularVelocity());

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
}
