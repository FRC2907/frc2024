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

        //TODO self-righting
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
                break;
            case START:
                break;

            case MOVING_TO_INTAKING:
                arm.setSetPoint(Control.arm.kFloorPosition);
                intake.setSetPoint(Control.intake.kIntakingRpm);
                // TODO automatically drive up to the Note
                if (arm.reachedSetPoint())
                    this.state = RobotState.INTAKING;
                break;
            case INTAKING:
                arm.setSetPoint(Control.arm.kFloorPosition);
                intake.setSetPoint(Control.intake.kIntakingRpm);
                if (false /* TODO we have a note */) 
                    this.state = RobotState.HOLDING_NOTE;
                break;

            case HOLDING_NOTE:
                arm.setSetPoint(Control.arm.kHoldingPosition);
                intake.setSetPoint(Control.intake.kOff);
                break;

            case MOVING_TO_AMP:
                arm.setSetPoint(Control.arm.kAmpPosition);
                // TODO automatically drive up to the Amp
                if (arm.reachedSetPoint()){ //TODO add drivetrain reached set point
                    this.state = RobotState.READY_TO_SCORE_AMP;
                }
                break;
            case READY_TO_SCORE_AMP:
                this.state = RobotState.SCORING_AMP;
                break;
            case SCORING_AMP:
                shooter.setSetPoint(Control.shooter.kAmpRPM);
                if (false/*scoring is done */)
                    this.state = RobotState.NEUTRAL;
                break;

            case MOVING_TO_SPEAKER:
                arm.setSetPoint(Control.arm.kSpeakerPosition);
                // TODO automatically drive up to the Speaker
                if (arm.reachedSetPoint()){ //TODO add drivetrain reached set point
                    this.state = RobotState.READY_TO_SCORE_SPEAKER;
                }
                break;
            case READY_TO_SCORE_SPEAKER:
                this.state = RobotState.SCORING_SPEAKER;
                break;
            case SCORING_SPEAKER:
                shooter.setSetPoint(Control.shooter.kSpeakerRPM);
                if (false/*scoring is done */){
                    this.state = RobotState.NEUTRAL;
                }
                break;

            case PREPARING_FOR_CLIMB:
                arm.setSetPoint(Control.arm.kClimbReadyPosition);
                // TODO automatically drive up to the Stage
                if (arm.reachedSetPoint()){
                    this.state = RobotState.CLIMBING;
                }
                break;
            case CLIMBING:
                arm.setSetPoint(Control.arm.kClimbClumbPosition);
                if (arm.reachedSetPoint()){
                    this.state = RobotState.HUNG;
                }
                break;
            case HUNG:
                arm.setSetPoint(Control.arm.kClimbClumbPosition);
                break;
            case NEUTRAL:
                arm.setSetPoint(Control.arm.kHoldingPosition);
                intake.setSetPoint(Control.intake.kOff);
                shooter.setSetPoint(Control.shooter.kOff);
                break;
            default:
                break; 
        }
    }
}
