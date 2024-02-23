package frc.robot.bodges.sillycontroller;

import edu.wpi.first.units.*;

public class SmartMotorControllerConfiguration_Angular {

    public final Measure<Per<         Angle,           Angle >>
        mechanismPositionPerEncoderAngularPosition;
    public final Measure<Per<Velocity<Angle>, Velocity<Angle>>>
        mechanismVelocityPerEncoderAngularVelocity;

    public PIDF<         Angle > pidf_position;
    public PIDF<Velocity<Angle>> pidf_velocity;

    public final boolean reversed, brake;

    public SmartMotorControllerConfiguration_Angular(
      Measure<Per<         Angle,           Angle >>
        _mechanismPositionPerEncoderAngularPosition
    , Measure<Per<Velocity<Angle>, Velocity<Angle>>>
        _mechanismVelocityPerEncoderAngularVelocity
    , PIDF<         Angle > _pidf_position
    , PIDF<Velocity<Angle>> _pidf_velocity
    , boolean _reversed
    , boolean _brake
    ) {
        this.mechanismPositionPerEncoderAngularPosition = _mechanismPositionPerEncoderAngularPosition;
        this.mechanismVelocityPerEncoderAngularVelocity = _mechanismVelocityPerEncoderAngularVelocity;
        this.pidf_position = _pidf_position;
        this.pidf_velocity = _pidf_velocity;
        this.reversed = _reversed;
        this.brake = _brake;
    }

}