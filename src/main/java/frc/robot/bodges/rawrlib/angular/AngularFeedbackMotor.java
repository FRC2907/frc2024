package frc.robot.bodges.rawrlib.angular;

import edu.wpi.first.units.*;
import frc.robot.bodges.rawrlib.generics.DimensionalErrorTracker;
import frc.robot.bodges.rawrlib.generics.DimensionalFeedbackMotor;
import frc.robot.bodges.rawrlib.generics.DimensionalPIDFController;
import frc.robot.bodges.rawrlib.generics.DimensionalPIDFGains;

/**
 * Describes a generic motor with closed-loop angular position and velocity control.
 */
public class AngularFeedbackMotor extends DimensionalFeedbackMotor<Angle> {

    //public AngularFeedbackMotor() {
    //    this.setPositionController(new AngularVoltagePIDFController());
    //}

    @Override
    public DimensionalFeedbackMotor<Angle> configurePositionController(
            DimensionalPIDFGains<Angle, Voltage> gains) {
        return configurePositionController(gains, Units.Degrees.zero());
    }

    @Override
    public DimensionalFeedbackMotor<Angle> configurePositionController(DimensionalPIDFGains<Angle, Voltage> gains,
            Measure<Angle> izone) {
        this.positionController.setGains(gains);
        this.positionController.setErrorTracker(DimensionalErrorTracker.createAngular(izone));
        return this;
    }

    @Override
    public DimensionalFeedbackMotor<Angle> configureVelocityController(
            DimensionalPIDFGains<Velocity<Angle>, Voltage> gains) {
        return configureVelocityController(gains, Units.DegreesPerSecond.zero());
    }

    @Override
    public DimensionalFeedbackMotor<Angle> configureVelocityController(
            DimensionalPIDFGains<Velocity<Angle>, Voltage> gains, Measure<Velocity<Angle>> izone) {
        this.velocityController.setGains(gains);
        this.velocityController.setErrorTracker(DimensionalErrorTracker.createAngularVelocity(izone));
        return this;
    }

    @Override
    public DimensionalFeedbackMotor<Angle> setPositionController(DimensionalPIDFController<Angle, Voltage> ctlr) {
        this.positionController = ctlr;
        this.positionController.setStateSupplier(this::getPosition);
        return this;
    }

    @Override
    public DimensionalFeedbackMotor<Angle> setVelocityController(
            DimensionalPIDFController<Velocity<Angle>, Voltage> ctlr) {
        this.velocityController = ctlr;
        this.velocityController.setStateSupplier(this::getVelocity);
        return this;
    }
}
