package frc.robot.bodges;

import edu.wpi.first.units.*;

/**
 * Describes a generic motor with closed-loop linear position and velocity control.
 */
public class LinearFeedbackMotor extends DimensionalFeedbackMotor<Distance> {

    @Override
    public DimensionalFeedbackMotor<Distance> configurePositionController(
            DimensionalPIDFGains<Distance, Voltage> gains) {
        return configurePositionController(gains, Units.Meters.zero());
    }

    @Override
    public DimensionalFeedbackMotor<Distance> configurePositionController(DimensionalPIDFGains<Distance, Voltage> gains,
            Measure<Distance> izone) {
        this.positionController.setGains(gains);
        this.positionController.setErrorTracker(DimensionalErrorTracker.createLinear(izone));
        return this;
    }

    @Override
    public DimensionalFeedbackMotor<Distance> configureVelocityController(
            DimensionalPIDFGains<Velocity<Distance>, Voltage> gains) {
        return configureVelocityController(gains, Units.MetersPerSecond.zero());
    }

    @Override
    public DimensionalFeedbackMotor<Distance> configureVelocityController(
            DimensionalPIDFGains<Velocity<Distance>, Voltage> gains, Measure<Velocity<Distance>> izone) {
        this.velocityController.setGains(gains);
        this.velocityController.setErrorTracker(DimensionalErrorTracker.createLinearVelocity(izone));
        return this;
    }

    @Override
    public DimensionalFeedbackMotor<Distance> setPositionController(DimensionalPIDFController<Distance, Voltage> ctlr) {
        this.positionController = ctlr;
        this.positionController.setStateSupplier(this::getPosition);
        return this;
    }

    @Override
    public DimensionalFeedbackMotor<Distance> setVelocityController(
            DimensionalPIDFController<Velocity<Distance>, Voltage> ctlr) {
        this.velocityController = ctlr;
        this.velocityController.setStateSupplier(this::getVelocity);
        return this;
    }
}
