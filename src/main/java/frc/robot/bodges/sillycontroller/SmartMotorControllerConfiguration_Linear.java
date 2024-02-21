package frc.robot.bodges.sillycontroller;

import edu.wpi.first.units.*;

public class SmartMotorControllerConfiguration_Linear {

    public final Measure<Per<         Distance,           Angle >>
        mechanismPositionPerEncoderAngularPosition;
    public final Measure<Per<Velocity<Distance>, Velocity<Angle>>>
        mechanismVelocityPerEncoderAngularVelocity;

    public PIDF<         Distance > pidf_position;
    public PIDF<Velocity<Distance>> pidf_velocity;

    public final boolean reversed;

    public SmartMotorControllerConfiguration_Linear(
      Measure<Per<         Distance,           Angle >>
        _mechanismPositionPerEncoderAngularPosition
    , Measure<Per<Velocity<Distance>, Velocity<Angle>>>
        _mechanismVelocityPerEncoderAngularVelocity
    , PIDF<         Distance > _pidf_position
    , PIDF<Velocity<Distance>> _pidf_velocity
    , boolean _reversed
    ) {
        this.mechanismPositionPerEncoderAngularPosition = _mechanismPositionPerEncoderAngularPosition;
        this.mechanismVelocityPerEncoderAngularVelocity = _mechanismVelocityPerEncoderAngularVelocity;
        this.pidf_position = _pidf_position;
        this.pidf_velocity = _pidf_velocity;
        this.reversed = _reversed;
    }

}

