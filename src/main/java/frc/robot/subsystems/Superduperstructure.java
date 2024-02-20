package frc.robot.subsystems;

import frc.robot.constants.Control;
import frc.robot.constants.Ports;
import frc.robot.io.ControllerRumble;
import frc.robot.subsystems.Drivetrain.DriveMode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Superduperstructure implements ISubsystem {
    private Drivetrain drivetrain;
    private Arm arm;
    private Intake intake;
    private Shooter shooter;
    private Hat hat;
    private ControllerRumble driver, operator;
    private ISubsystem[] subsystems;


    private RobotState state;
    private boolean automateScoring;
    private TrajectoryFollower tjf;


    public enum RobotState {
        MOVING_TO_START // could use in testing scenarios
        , START, MOVING_TO_NEUTRAL, NEUTRAL

        , MOVING_TO_INTAKING, INTAKING, OUTAKING
        , MOVING_TO_HOLDING_NOTE, HOLDING_NOTE

        , MOVING_TO_AMP, READY_TO_SCORE_AMP, SCORING_AMP

        , MOVING_TO_SPEAKER, READY_TO_SCORE_SPEAKER, SCORING_SPEAKER

        , PREPARING_FOR_CLIMB, CLIMBING, HUNG

        , KNOCKED_OVER, SELF_RIGHTING
        // TODO self-righting
    }

    public enum BestTarget {
        AMP, SPEAKER , NONE , TIED
    }


    private Superduperstructure(RobotState _state, boolean _automation) {
        this.state = _state;
        this.automateScoring(_automation);
        this.drivetrain = Drivetrain.getInstance();
        this.arm = Arm.getInstance();
        this.intake = Intake.getInstance();
        this.shooter = Shooter.getInstance();
        this.hat = Hat.getInstance();
        this.driver = ControllerRumble.getInstance(Ports.HID.DRIVER);
        this.operator = ControllerRumble.getInstance(Ports.HID.OPERATOR);
        this.subsystems = new ISubsystem[]{drivetrain, arm, intake, shooter, hat, driver, operator};

        this.handleState(); // initialize subsystem setpoints
    }

    private Superduperstructure() {
        this(RobotState.START, true);
    }

    private static Superduperstructure instance;
    public static Superduperstructure getInstance() {
        if (instance == null)
            instance = new Superduperstructure();
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
    public void knockedOver(){
        this.state = RobotState.KNOCKED_OVER;
    }
    public void selfRighting(){
        this.state = RobotState.SELF_RIGHTING;
    }

    public void neutralPosition() {
        if (intake.hasNote()){
            this.state = RobotState.MOVING_TO_HOLDING_NOTE;
            operator.rumble(0.5);
        } else {
            this.state = RobotState.MOVING_TO_NEUTRAL;
        }
    }

    public void cancelAction() {
        this.tjf = null;
        neutralPosition();
    }
    public void automateScoring(boolean _automation) {
        this.automateScoring = _automation;
    }
    public boolean isScoringAutomated() {
        return this.automateScoring;
    }
    public BestTarget chooseBestTarget() {
        //TODO implement limelight sensor stuff
        return BestTarget.NONE;
    }

    public void autoScore() {
        switch (this.chooseBestTarget()) {
            case AMP:
                this.moveToAmp();
                break;
            case SPEAKER:
                this.moveToSpeaker();
                break;
            case NONE:
                operator.rumble(1);
                break;
            case TIED:
                operator.rumble(0.5);
                break;
            default:
                break;
        }
    }


    /**
     * If we're in a manual-driving state, tell the drivetrain that, and send driver input.
     * If we're in a self-driving state, tell the drivetrain that and let it do its thing.
     */
    public void handleDriving() {
        switch (this.state) {
            case MOVING_TO_START:
            case START:
            case MOVING_TO_NEUTRAL:
            case NEUTRAL:
            case MOVING_TO_HOLDING_NOTE:
            case HOLDING_NOTE:
            case OUTAKING:
            // in these states, we drive manually
                this.tjf = null;
                if (drivetrain.getDriveMode() == DriveMode.AUTO)
                    drivetrain.setDriveMode(
                        intake.hasNote()
                        ? Control.drivetrain.kDefaultDriveModeWithNote
                        : Control.drivetrain.kDefaultDriveModeWithoutNote
                    );
                switch(drivetrain.getDriveMode()){
                    case FIELD_FORWARD:
                        drivetrain.setFieldDriveInputs(
                            Control.drivetrain.kMaxSpeed.times(driver.getLeftMagnitude())
                            , driver.getLeftAngle()
                        );
                        break;
                    case FIELD_REVERSED:
                        drivetrain.setFieldDriveInputs(
                            Control.drivetrain.kMaxSpeed.times(driver.getLeftMagnitude())
                            , driver.getLeftAngle().rotateBy(Rotation2d.fromDegrees(180))
                        );
                        break;
                    case LOCAL_FORWARD:
                        drivetrain.setLocalDriveInputs(
                            Control.drivetrain.kMaxSpeed.times(driver.getLeftY())
                            , Control.drivetrain.kMaxAngVel.times(driver.getRightX())
                        );
                        break;
                    case LOCAL_REVERSED:
                        drivetrain.setLocalDriveInputs(
                            Control.drivetrain.kMaxSpeed.times(driver.getLeftY()).negate()
                            , Control.drivetrain.kMaxAngVel.times(driver.getRightX()).negate()
                        );
                        break;
                    default:
                        System.err.println("[EE] Auto driving in non-auto robot state");
                        new Exception().printStackTrace();
                        break;
                }
                break;
            default:
            // in all other states, we follow a trajectory
                drivetrain.setDriveMode(DriveMode.AUTO);
                // NOTE so if we reach the end of the trajectory, but we haven't moved on to the next state...
                // then what? should we have a timeout that auto-cancels if that happens? FIXME consider that
                if (tjf == null) {
                    driver.rumble(2);
                    operator.rumble(2);
                    System.err.println("[EE] Tried to self-drive without a trajectory to follow");
                    new Exception().printStackTrace();
                    cancelAction();
                } else
                drivetrain.setAutoDriveInputs(tjf.getWheelSpeeds(drivetrain.getPose()));
        }
    }

    public void handleInputs() {
        if (operator.getCircleButtonPressed() || driver.getCircleButtonPressed()) {
            cancelAction();
        }


        if (operator.getCrossButtonPressed()) { // TODO automatic intake
            outakeNote();
        }
        if (operator.getSquareButtonPressed()) {
            autoScore();
        }
        if (operator.getTriangleButtonPressed()) {
            neutralPosition();
        }
        if (operator.getL2Button()) {
            moveToSpeaker(); // TODO gavin rawr
        }
        if (operator.getL1Button()) {
            moveToIntaking();
        }
        if (operator.getR1ButtonPressed()) {
            outakeNote();
        }
        if (operator.getR2ButtonPressed()) { // TODO manual intaking
            intakeNote();
        }


        if (driver.getR2Button()) {
            moveToSpeaker();
        }
        if (driver.getR1Button()) {
            moveToAmp();
        }
        if (driver.getCrossButtonPressed()) {
            prepareForClimb();
        }
        if (driver.getR3ButtonPressed()) {
            // TODO finish
            if (/* DriveMode == LOCAL_FORWARD */8 == 8){
                /*setDriveMode(LOCAL_REVERSED) */;
            }
            //and then copy and paste it a few more times
            //i tried
            //it's probably something stupid i didn't get
        }
    }

    public void handleState() {
        switch (this.state) {
            case MOVING_TO_START:
                arm.startPosition();
                intake.off();
                shooter.off();
                if (arm.reachedSetPoint())
                    this.state = RobotState.START;
                break;
            case START:
                arm.startPosition();
                intake.off();
                shooter.off();
                break;

            case MOVING_TO_INTAKING:
                arm.floorPosition();
                intake.intake();
                // TODO automatically drive up to the Note
                // steps:
                // if we don't currently have a TrajectoryFollower: get one from the Hat
                // then the handleDriving function will follow it each cycle
                // for intaking i guess we don't really need to check whether we've finished the trajectory
                // since having the note is all that matters
                if (this.tjf == null){
                    this.tjf = hat.findPathToNote();
                }
                if (arm.reachedSetPoint()){
                    this.state = RobotState.INTAKING;
                }
                break;
            case INTAKING:
                arm.floorPosition();
                intake.intake();
                if (intake.hasNote()){
                    this.state = RobotState.HOLDING_NOTE;
                }
                break;

            case OUTAKING:
                intake.outake();
                if (automateScoring && !intake.hasNote())
                    this.state = RobotState.NEUTRAL;
                break;

            case MOVING_TO_HOLDING_NOTE:
                arm.holdingPosition();
                intake.off();
                break;

            case HOLDING_NOTE:
                break;

            case MOVING_TO_AMP:
                arm.ampPosition();
                if (this.tjf == null){
                    this.tjf = hat.findPathToAmp();
                }
                if (arm.reachedSetPoint()) { // TODO add drivetrain reached set point
                    this.state = RobotState.READY_TO_SCORE_AMP;
                }
                break;
            case READY_TO_SCORE_AMP:
                if (this.isScoringAutomated())
                    this.state = RobotState.SCORING_AMP;
                break;
            case SCORING_AMP:
                shooter.amp();
                if (shooter.noteScored()){
                    this.state = RobotState.NEUTRAL;
                }
                break;

            case MOVING_TO_SPEAKER:
                arm.speakerPosition();
                if (this.tjf == null)
                    this.tjf = hat.findPathToSpeaker();
                if (arm.reachedSetPoint() && tjf.isDone()) { // TODO add drivetrain reached set point
                    // TODO do we also want to get the shooter wheels up to speed first? or no?
                    this.state = RobotState.READY_TO_SCORE_SPEAKER;
                }
                break;
            case READY_TO_SCORE_SPEAKER:
                if (this.isScoringAutomated())
                    this.state = RobotState.SCORING_SPEAKER;
                break;
            case SCORING_SPEAKER:
                shooter.shooter();
                if (shooter.noteScored()) {
                    this.state = RobotState.NEUTRAL;
                }
                break;

            case PREPARING_FOR_CLIMB:
                arm.climbReadyPosition();
                // TODO automatically drive up to the Stage
                if (this.isScoringAutomated() && arm.reachedSetPoint()) {
                    this.state = RobotState.CLIMBING;
                }
                break;
            case CLIMBING:
                arm.clumbPosition();
                if (arm.reachedSetPoint()) {
                    this.state = RobotState.HUNG;
                }
                break;
            case HUNG:
                arm.clumbPosition();
                break;
            case MOVING_TO_NEUTRAL:
                arm.holdingPosition();
                intake.off();
                shooter.off();
                this.state = RobotState.NEUTRAL;
                break;
            case NEUTRAL:
                break;
            case KNOCKED_OVER:
                //TODO
                break;
            case SELF_RIGHTING:
                //TODO
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoop() {
        receiveOptions();
        handleInputs();
        handleState();
        handleDriving();

        // Tell all the subsystems to do their thing for this cycle
        for (ISubsystem s : this.subsystems)
            s.onLoop();

        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putString("superduperstructure.state", getState().toString());

        for (ISubsystem s : this.subsystems)
            s.submitTelemetry();
    }

    @Override
    public void receiveOptions() {
        for (ISubsystem s : this.subsystems)
            s.receiveOptions();
    }
}
