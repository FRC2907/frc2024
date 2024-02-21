package frc.robot.constants;

import org.opencv.core.Scalar;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.units.*;
import frc.robot.bodges.sillycontroller.PIDF;
import frc.robot.bodges.sillycontroller.SmartMotorControllerConfiguration_Angular;
import frc.robot.bodges.sillycontroller.SmartMotorControllerConfiguration_Linear;
import frc.robot.subsystems.Drivetrain.DriveMode;

/**
 * Style guidance:
 * - SCREAMING_SNAKE_CASE for properties of the robot hardware, things that are not configurable as code
 * - kPascalCase for configurable values, such as setpoints and gains
 * - comment the unit on any value where it makes sense (gains have units but they're too hard to think about)
 */

public class Control {

    public static class arm {
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Per<Angle, Angle>>
            ARM_POS_PER_ENC_POS_UNIT = Units.Revolutions.of(GEAR_RATIO).per(Units.Revolutions);
        public static final Measure<Per<Velocity<Angle>, Velocity<Angle>>>
            ARM_VEL_PER_ENC_VEL_UNIT = Units.RPM        .of(GEAR_RATIO).per(Units.RPM        );

        public static final Measure<Per<Voltage,          Angle>>
            kP_position = Units.Volts.of(1).per(Units.Rotations); /// TODO empirical
        public static final Measure<Per<Voltage, Velocity<Angle>>>
            kD_position = Units.Volts.of(1).per(Units.RotationsPerSecond); /// TODO empirical
        public static final Measure<Per<Voltage, Velocity<Angle>>>
            kP_velocity = Units.Volts.of(1).per(Units.RotationsPerSecond); /// TODO empirical
        public static final Measure<Per<Voltage, Velocity<Velocity<Angle>>>>
            kD_velocity = Units.Volts.of(1).per(Units.RotationsPerSecond.per(Units.Second)); /// TODO empirical

        public static final PIDF<Angle> kPD_position = new PIDF<>(kP_position, null, kD_position, null);
        public static final PIDF<Velocity<Angle>> kPD_velocity = new PIDF<>(kP_velocity, null, kD_velocity, null);
        public static final SmartMotorControllerConfiguration_Angular kMotorConf
            = new SmartMotorControllerConfiguration_Angular(
                ARM_POS_PER_ENC_POS_UNIT
                , ARM_VEL_PER_ENC_VEL_UNIT
                , kPD_position
                , kPD_velocity
                , false
            );
        public static final boolean[] MOTORS_REVERSED = {false, true};


        public static final Measure<         Angle>
            kPositionHysteresis =  Units.Degrees         .of( 2); /// TODO empirical
        public static final Measure<Velocity<Angle>>
            kVelocityHysteresis =  Units.DegreesPerSecond.of( 2); /// TODO empirical

        public static final Measure<Angle> kStartPosition      = Units.Degrees.of( 90); /// TODO empirical
        public static final Measure<Angle> kFloorPosition      = Units.Degrees.of(  0); /// TODO empirical
        public static final Measure<Angle> kAmpPosition        = Units.Degrees.of(100); /// TODO empirical
        public static final Measure<Angle> kSpeakerPosition    = Units.Degrees.of( 30); /// TODO empirical
        public static final Measure<Angle> kHoldingPosition    = Units.Degrees.of( 20); /// TODO empirical
        public static final Measure<Angle> kClimbReadyPosition = Units.Degrees.of( 90); /// TODO empirical
        public static final Measure<Angle> kClimbClumbPosition = Units.Degrees.of( 30); /// TODO empirical
        public static final Measure<Angle> kMinPosition        = Units.Degrees.of(  0); /// TODO empirical
        public static final Measure<Angle> kMaxPosition        = Units.Degrees.of(120); /// TODO empirical

        /**
         * manual control works by incrementing the arm's position setpoint every cycle.
         * this might be a bad idea. TODO consider complexity of moving to a velocity
         * model?
         */
        public static final Measure<Angle> kManualControlDiff  = Units.Degrees.of(  2); /// TODO empirical
        public static final Measure<Velocity<Angle>> kMaxVelocity = Units.DegreesPerSecond.of(60); /// TODO empirical
    }

    public static class drivetrain {
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Distance> TRACK_WIDTH         = Units.Meters.of(0.5823458);
        public static final Measure<Distance> WHEEL_DIAMETER      = Units.Inches.of(6);
        public static final Measure<Distance> WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER.times(Math.PI);
        public static final Measure<Per<Distance, Angle>>
            FLOOR_POS_PER_ENC_POS_UNIT = WHEEL_CIRCUMFERENCE.times(GEAR_RATIO).per(Units.Revolutions);
        /**
         * Linear distance per encoder rotation.
         */
        public static final Measure<Per<Velocity<Distance>, Velocity<Angle>>>
            FLOOR_VEL_PER_ENC_VEL_UNIT = WHEEL_CIRCUMFERENCE.times(GEAR_RATIO).per(Units.Minute).per(Units.RPM);


        /** Extra Number for padding track width to compensate for wheel scrub. */
        public static final Measure<Distance> kTrackWidthFudge = Units.Meters.of(0); /// TODO empirical
        public static final DifferentialDriveKinematics
            DRIVE_KINEMATICS = new DifferentialDriveKinematics(TRACK_WIDTH.plus(kTrackWidthFudge));

        public static final Measure<Per<Velocity<Angle>, Angle>>
            kP_fieldRelativeHeading = Units.DegreesPerSecond.of(2).per(Units.Degrees); // TODO empirical
        public static final Measure<Per<Voltage, Velocity<Distance>>>
            kP_velocity = Units.Volts.of(1).per(Units.MetersPerSecond); // TODO empirical
        public static final Measure<Per<Voltage, Velocity<Velocity<Distance>>>>
            kD_velocity = Units.Volts.of(1).per(Units.MetersPerSecondPerSecond); // TODO empirical

        public static final PIDF<Velocity<Distance>> kPD_velocity = new PIDF<>(kP_velocity, null, kD_velocity, null);
        public static final SmartMotorControllerConfiguration_Linear kLeftMotorConf
            = new SmartMotorControllerConfiguration_Linear(
                FLOOR_POS_PER_ENC_POS_UNIT
                , FLOOR_VEL_PER_ENC_VEL_UNIT
                , null
                , kPD_velocity
                , false
            );
        public static final SmartMotorControllerConfiguration_Linear kRightMotorConf
            = new SmartMotorControllerConfiguration_Linear(
                FLOOR_POS_PER_ENC_POS_UNIT
                , FLOOR_VEL_PER_ENC_VEL_UNIT
                , null
                , kPD_velocity
                , true
            );

        public static final Measure<Velocity<Distance>>
            kMaxSpeed  = Units.MetersPerSecond .of( 3); // TODO empirical
        public static final Measure<Velocity<Angle>>
            kMaxAngVel = Units.DegreesPerSecond.of(60); // TODO empirical

        public static final DriveMode kDefaultDriveModeWithoutNote = DriveMode.FIELD_FORWARD;
        public static final DriveMode kDefaultDriveModeWithNote = DriveMode.FIELD_REVERSED;
    }

    public static class intake {
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(2); /// TODO verify with Build
        public static final Measure<Distance> WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER.times(Math.PI);
        /**
         * Linear distance per encoder rotation.
         */
        public static final Measure<Per<Velocity<Distance>, Velocity<Angle>>>
            LINEAR_VEL_PER_ENC_VEL_UNIT = WHEEL_CIRCUMFERENCE.times(GEAR_RATIO).per(Units.Minute).per(Units.RPM);

        public static final Measure<Per<Voltage, Velocity<Distance>>>
            kP = Units.Volts.of(1).per(Units.MetersPerSecond);          /// TODO empirical
        public static final Measure<Per<Voltage, Velocity<Velocity<Distance>>>>
            kD = Units.Volts.of(1).per(Units.MetersPerSecondPerSecond); /// TODO empirical

        public static final PIDF<Velocity<Distance>> kPD = new PIDF<>(kP, null, kD, null);
        public static final SmartMotorControllerConfiguration_Linear kMotorConf
            = new SmartMotorControllerConfiguration_Linear(
                null
                , LINEAR_VEL_PER_ENC_VEL_UNIT
                , null
                , kPD
                , false
            );

        public static final Measure<Velocity<Distance>>
            kIntakingSpeed = Units.MetersPerSecond.of(0); /// TODO empirical
        public static final Measure<Velocity<Distance>>
            kOutakingSpeed = Units.MetersPerSecond.of(0); /// TODO empirical
        public static final Measure<Velocity<Distance>>
            kOff           = Units.MetersPerSecond.of(0); /// TODO empirical
    }

    public static class shooter {
        public static final double GEAR_RATIO = 1; /// TODO get from Build
        public static final Measure<Distance> WHEEL_DIAMETER = Units.Inches.of(4);
        public static final Measure<Distance> WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER.times(Math.PI);
        /**
         * Linear distance per encoder rotation.
         */
        public static final Measure<Per<Velocity<Distance>, Velocity<Angle>>>
            LINEAR_VEL_PER_ENC_VEL_UNIT = WHEEL_CIRCUMFERENCE.times(GEAR_RATIO).per(Units.Minute).per(Units.RPM);

        public static final Measure<Per<Voltage, Velocity<Distance>>>
            kP = Units.Volts.of(1).per(Units.MetersPerSecond);          /// TODO empirical
        public static final Measure<Per<Voltage, Velocity<Velocity<Distance>>>>
            kD = Units.Volts.of(1).per(Units.MetersPerSecondPerSecond); /// TODO empirical

        public static final PIDF<Velocity<Distance>> kPD = new PIDF<>(kP, null, kD, null);
        public static final SmartMotorControllerConfiguration_Linear kMotorConf
            = new SmartMotorControllerConfiguration_Linear(
                null
                , LINEAR_VEL_PER_ENC_VEL_UNIT
                , null
                , kPD
                , false
            );
        public static final boolean[] MOTORS_REVERSED = {false, true};

        public static final Measure<Velocity<Distance>>
            kAmpSpeed      = Units.MetersPerSecond.of(0); /// TODO empirical
        public static final Measure<Velocity<Distance>>
            kSpeakerSpeed  = Units.MetersPerSecond.of(0); /// TODO empirical
        public static final Measure<Velocity<Distance>>
            kOff           = Units.MetersPerSecond.of(0); /// TODO empirical
    }

    public static class camera {
        public static final int WIDTH  = 320;
        public static final int HEIGHT = 240;
        public static final Scalar kOrangeLow  = new Scalar( 4, 127, 127);
        public static final Scalar kOrangeHigh = new Scalar(32, 255, 255);
        public static final double kAreaFilterFactor = 0.075;
        public static final boolean kEnabled             = true;
        public static final boolean kNoteTrackingEnabled = false;
    }
}
