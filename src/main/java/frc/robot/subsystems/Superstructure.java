package frc.robot.subsystems;

import frc.robot.constants.Control;

public class Superstructure implements ISubsystem {
    private Drivetrain drivetrain;
    private Arm arm;
    private Intake intake;
    private Shooter shooter;
    private ISubsystem[] subsystems;

    private RobotState state;
    private boolean automation;

    public enum RobotState {
        MOVING_TO_START // could use in testing scenarios
        , START, NEUTRAL

        , MOVING_TO_INTAKING, INTAKING, HOLDING_NOTE, OUTAKING

        , MOVING_TO_AMP, READY_TO_SCORE_AMP, SCORING_AMP

        , MOVING_TO_SPEAKER, READY_TO_SCORE_SPEAKER, SCORING_SPEAKER

        , PREPARING_FOR_CLIMB, CLIMBING, HUNG

        // TODO self-righting
    }

    public enum BestTarget {
        AMP, SPEAKER , NONE , TIED
    }

    private Superstructure(RobotState _state, boolean _automation) {
        this.state = _state;
        this.automateScoring(_automation);
        this.drivetrain = Drivetrain.getInstance();
        this.arm = Arm.getInstance();
        this.intake = Intake.getInstance();
        this.shooter = Shooter.getInstance();
        this.subsystems = new ISubsystem[]{drivetrain, arm, intake, shooter};
    }

    private Superstructure() {
        this(RobotState.START, true);
    }

    private static Superstructure instance;

    public static Superstructure getInstance() {
        if (instance == null)
            instance = new Superstructure();
        return instance;
    }

    public RobotState getState() { return this.state; }

    public void moveToIntaking() {
        this.state = RobotState.MOVING_TO_INTAKING;
    }

    public void intakeNote() {
        this.state = RobotState.INTAKING;
    }

    public void outakeNote(){
        this.state = RobotState.OUTAKING;
    }

    public void moveToAmp() {
        this.state = RobotState.MOVING_TO_AMP;
    }

    public void scoreInAmp() {
        this.state = RobotState.SCORING_AMP;
    }

    public void moveToSpeaker() {
        this.state = RobotState.MOVING_TO_SPEAKER;
    }

    public void scoreInSpeaker() {
        this.state = RobotState.SCORING_SPEAKER;
    }

    public void prepareForClimb() {
        this.state = RobotState.PREPARING_FOR_CLIMB;
    }

    public void startClimb() {
        this.state = RobotState.CLIMBING;
    }

    public void neutralPosition() {
        this.state = RobotState.NEUTRAL;
    }

    public void automateScoring(boolean _automation) {
        this.automation = _automation;
    }

    public boolean isScoringAutomated() {
        return this.automation;
    }

    public void autoScore(){
        switch (this.chooseBestTarget()) {
            case AMP:
                
                break;
            case SPEAKER:

                break;
            case NONE:

                break;
            case TIED:

                break;
            default:
                break;
        }
    }

    public void cancelAction(){
        if (intake.hasNote()){
            this.state = RobotState.HOLDING_NOTE;
        } else {
            this.state = RobotState.NEUTRAL;
        }
    }

    public BestTarget chooseBestTarget(){
        //TODO implement limelight sensor stuff
        return BestTarget.NONE;
    }

    @Override
    public void onLoop() {
        switch (this.state) {
            case MOVING_TO_START:
                arm.startPosition();
                intake.off();
                shooter.off();
                if (arm.reachedSetPoint())
                    this.state = RobotState.START;
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
                if (intake.hasNote()){
                    this.state = RobotState.HOLDING_NOTE;
                }
                break;

            case OUTAKING:
                intake.outake();

            case HOLDING_NOTE:
                arm.setSetPoint(Control.arm.kHoldingPosition);
                intake.setSetPoint(Control.intake.kOff);
                break;

            case MOVING_TO_AMP:
                arm.setSetPoint(Control.arm.kAmpPosition);
                // TODO automatically drive up to the Amp
                if (arm.reachedSetPoint()) { // TODO add drivetrain reached set point
                    this.state = RobotState.READY_TO_SCORE_AMP;
                }
                break;
            case READY_TO_SCORE_AMP:
                if (this.isScoringAutomated())
                    this.state = RobotState.SCORING_AMP;
                break;
            case SCORING_AMP:
                shooter.setSetPoint(Control.shooter.kAmpRPM);
                if (shooter.noteScored()){
                    this.state = RobotState.NEUTRAL;
                }
                break;

            case MOVING_TO_SPEAKER:
                arm.setSetPoint(Control.arm.kSpeakerPosition);
                // TODO automatically drive up to the Speaker
                if (arm.reachedSetPoint()) { // TODO add drivetrain reached set point
                    // TODO do we also want to get the shooter wheels up to speed first? or no?
                    this.state = RobotState.READY_TO_SCORE_SPEAKER;
                }
                break;
            case READY_TO_SCORE_SPEAKER:
                if (this.isScoringAutomated())
                    this.state = RobotState.SCORING_SPEAKER;
                break;
            case SCORING_SPEAKER:
                shooter.setSetPoint(Control.shooter.kSpeakerRPM);
                if (shooter.noteScored()) {
                    this.state = RobotState.NEUTRAL;
                }
                break;

            case PREPARING_FOR_CLIMB:
                arm.setSetPoint(Control.arm.kClimbReadyPosition);
                // TODO automatically drive up to the Stage
                if (this.isScoringAutomated() && arm.reachedSetPoint()) {
                    this.state = RobotState.CLIMBING;
                }
                break;
            case CLIMBING:
                arm.setSetPoint(Control.arm.kClimbClumbPosition);
                if (arm.reachedSetPoint()) {
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

        // Tell all the subsystems to do their thing for this cycle
        for (ISubsystem s : this.subsystems)
            s.onLoop();
    }

    @Override
    public void submitTelemetry() {
        // TODO Auto-generated method stub
    }

    @Override
    public void receiveOptions() {
        // TODO Auto-generated method stub
    }
}
