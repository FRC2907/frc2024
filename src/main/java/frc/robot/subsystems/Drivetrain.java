package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.constants.Control;
import frc.robot.util.Util;

public class Drivetrain extends DifferentialDrive implements ISubsystem {

    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private NetworkTable NT;
    private DoublePublisher p_positionX, p_positionY, p_velocity, p_velocityL, p_velocityR, p_angle, p_angVel;
   private double leftSpeed, rightSpeed, totalSpeed, turningness;
   private Rotation2d desiredHeading;



    private DriveMode mode;
    public enum DriveMode {
          AUTO

        , FIELD_FORWARD, FIELD_REVERSED

        , LOCAL_FORWARD, LOCAL_REVERSED
    }
   public void setDriveMode(DriveMode newMode) { this.mode = newMode; }
   public DriveMode getDriveMode() { return this.mode; }



    private Drivetrain(CANSparkMax left, CANSparkMax right) {
        super(left, right);
        this.leftMotor = left; // TODO add velocity conversion factor
        this.rightMotor = right;
        this.mode = DriveMode.LOCAL_FORWARD;
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

    public static Drivetrain getInstance() {
        CANSparkMax left, right;
        if (instance == null) {
            left = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.LEFTS, true);
            right = Util.createSparkGroup(frc.robot.constants.Ports.can.drivetrain.RIGHTS, false);
            instance = new Drivetrain(left, right);

        }
        return instance;
    }



    private Pose2d pose;

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

    // TODO(justincredible2508,josephreed2600) implement field-relative control scheme
   public void setFieldDriveInputs(double magnitude, Rotation2d direction) {
            // TODO we can probably use curvatureDrive for field-relative steering as well
            // instead of turningness being just the x value of a stick,
            // use the difference between our heading and our desired heading
            // and then speed is probably just the magnitude of the stick
    this.totalSpeed = magnitude;
    this.desiredHeading = direction;
    // keeping in mind that we also need to account for forward/reverse when calculating our heading error
   }

   private double convertHeadingToTurningness(Rotation2d current, Rotation2d desired) {
    return Control.drivetrain.kP_fieldRelativeHeading * desired.minus(current).getRadians(); // TODO verify
   }



   /**
    * Other functions outside this class should interact by calling the helper
    * functions to set these variables:
    * <ul>
    * <li>manualControl
    * <li>speed, rotation
    * <li>left, right
    * </ul>
    *
    * This function decides which set of vars to use and how to use them. This
    * model reduces the amount of Actual Stuff that Happens outside of the onLoop
    * family.
    */

   //reversed means basically changing direction rather than like a car reverse

    @Override
    public void onLoop() {
        switch (this.mode) {
            case AUTO:
                double[] speeds = new double[] {leftSpeed, rightSpeed};
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
                this.curvatureDrive(totalSpeed, convertHeadingToTurningness(this.getAngle(), desiredHeading));
                break;
            case FIELD_REVERSED:
                this.curvatureDrive(-totalSpeed
                // TODO verify but I think this is right?
                    , convertHeadingToTurningness(
                        this.getAngle()
                        , desiredHeading.minus(Rotation2d.fromRotations(0.5))
                    )
                );
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

    public Rotation2d getAngle() {
        return getPose().getRotation();
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
        p_angle.set(getAngle().getDegrees());
        p_angVel.set(getAngularVelocity());
    }

    @Override
    public void receiveOptions() {
        // TODO Auto-generated method stub
    }


    public void reverse() {
    } // TODO

}