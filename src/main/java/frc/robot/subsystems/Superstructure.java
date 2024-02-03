package frc.robot.subsystems;

import frc.robot.constants.Control;

public class Superstructure {
    private Drivetrain drivetrain;
    private Intake intake;
    private Shooter shooter;
    private Arm arm;

    private RobotState state;

    public enum RobotState {
        MOVING_TO_START // could use in testing scenarios
        , START, NEUTRAL

        , MOVING_TO_INTAKING, INTAKING, HOLDING_NOTE

        , MOVING_TO_AMP, READY_TO_SCORE_AMP, SCORING_AMP

        , MOVING_TO_SPEAKER, READY_TO_SCORE_SPEAKER, SCORING_SPEAKER

        , PREPARING_FOR_CLIMB, CLIMBING, HUNG
    }

    private Superstructure(RobotState _state) {
        this.state = _state;
    }

    private Superstructure() {
        this(RobotState.START);
    }

    private static Superstructure instance;

    public static Superstructure getInstance() {
        if (instance == null)
            instance = new Superstructure();
        return instance;
    }

    // TODO
    public void moveToIntaking() {
    }

    public void intakeNote() {
    }

    public void moveToAmp() {
    }

    public void scoreInAmp() {
    }

    public void moveToSpeaker() {
    }

    public void scoreInSpeaker() {
    }

    public void prepareForClimb() {
    }

    public void startClimb() {
    }

    public void onLoop() {
        switch (this.state) {
            case MOVING_TO_START:
                ;
            case START:
                ;

            case MOVING_TO_INTAKING:
                arm.setSetPoint(Control.arm.kFloorPosition);
                intake.setSetPoint(Control.intake.kIntakingRpm);
                // TODO automatically drive up to the Note
                ;
            case INTAKING:
                if ( false /* TODO we have a note */) {
                    this.state = RobotState.HOLDING_NOTE;
                }
                ;

            case HOLDING_NOTE:
                arm.setSetPoint(Control.arm.kHoldingPosition);
                intake.setSetPoint(Control.intake.kOff);

            case MOVING_TO_AMP:
                ;
            case READY_TO_SCORE_AMP:
                ;
            case SCORING_AMP:
                ;

            case MOVING_TO_SPEAKER:
                ;
            case READY_TO_SCORE_SPEAKER:
                ;
            case SCORING_SPEAKER:
                ;

            case PREPARING_FOR_CLIMB:
                ;
            case CLIMBING:
                ;
            case HUNG:
                ;
            default:
                ; // do nothing i guess
        }
    }
}
