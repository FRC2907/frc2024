package frc.robot.subsystems;

public class Superstructure {
    private Drivetrain drivetrain;
    private Intake intake;
    private Shooter shooter;
    private Arm arm;

    private RobotState state;

    public enum RobotState {
        MOVING_TO_START // could use in testing scenarios
        , START, NEUTRAL

        , MOVING_TO_INTAKING, INTAKING

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
                ;
            case INTAKING:
                ;

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
