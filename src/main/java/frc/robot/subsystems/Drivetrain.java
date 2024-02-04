package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.util.Util;

public class Drivetrain extends DifferentialDrive implements ISubsystem {
    private Drivetrain(CANSparkMax left, CANSparkMax right) {
        super(left, right);
        this.manualControl = true;
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
    public void setManualControl() { this.setManualControl(true); }
    public void setAutomaticControl() { this.setManualControl(false); }

    private Pose2d pose;
    public Pose2d getPose() { return this.pose; }
    private void setPose(Pose2d _pose) { this.pose = _pose; }

    public void curvatureDrive(double xSpeed, double zRotation) {
        this.curvatureDrive((xSpeed) * Math.abs(xSpeed), zRotation, Math.abs(xSpeed) < 0.1);
    }

    @Override
    public void onLoop() {
    }

    @Override
    public void submitTelemetry() {
        // TODO Auto-generated method stub
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