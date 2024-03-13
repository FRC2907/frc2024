package frc.robot.subsystems;

import frc.robot.constants.MechanismConstraints;
import frc.robot.constants.Ports;
import frc.robot.game_elements.FieldElements;
import frc.robot.io.GameController;
import frc.robot.subsystems.Drivetrain.DriveMode;
import frc.robot.util.Util;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Misc;

public class Superduperstructure implements ISubsystem {
    private Drivetrain drivetrain;
    private Arm arm;
    private Intake intake;
    private Shooter shooter;
    private Hat hat;
    private GameController driver, operator;
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

        , FOLLOWING_TRAJECTORY
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
        this.driver = GameController.getInstance(Ports.HID.DRIVER);
        this.operator = GameController.getInstance(Ports.HID.OPERATOR);
        this.subsystems = new ISubsystem[]{drivetrain, arm, intake, shooter, hat, driver, operator};

        this.manageState(); // initialize subsystem setpoints
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

    public RobotState getState() { return state; }

    public void moveToIntaking() {
        state = RobotState.MOVING_TO_INTAKING;
    }
    public void intakeNote() {
        state = RobotState.INTAKING;
    }
    public void outakeNote(){
        state = RobotState.OUTAKING;
    }
    public void moveToAmp() {
        state = RobotState.MOVING_TO_AMP;
    }
    public void scoreInAmp() {
        state = RobotState.SCORING_AMP;
    }
    public void moveToSpeaker() {
        state = RobotState.MOVING_TO_SPEAKER;
    }
    public void scoreInSpeaker() {
        state = RobotState.SCORING_SPEAKER;
    }
    public void prepareForClimb() {
        state = RobotState.PREPARING_FOR_CLIMB;
    }
    public void startClimb() {
        state = RobotState.CLIMBING;
    }
    public void knockedOver(){
        state = RobotState.KNOCKED_OVER;
    }
    public void selfRighting(){
        state = RobotState.SELF_RIGHTING;
    }
    public void followingTrajectory(){
        state = RobotState.FOLLOWING_TRAJECTORY;
    }
    public void followTrajectory(TrajectoryFollower path){
        this.tjf = path;
        followingTrajectory();
    }

    public void neutralPosition() {
        if (intake.hasNote()){
            state = RobotState.MOVING_TO_HOLDING_NOTE;
            operator.rumble(0.5);
        } else {
            state = RobotState.MOVING_TO_NEUTRAL;
        }
    }

    public void cancelAction() {
        tjf = null;
        neutralPosition();
    }
    public void automateScoring(boolean _automation) {
        automateScoring = _automation;
    }
    public boolean isScoringAutomated() {
        return automateScoring;
    }
    public BestTarget chooseBestTarget() {
        // TODO implement limelight sensor stuff
        return BestTarget.NONE;
    }

    public void autoScore() {
        switch (chooseBestTarget()) {
            case AMP:
                moveToAmp();
                break;
            case SPEAKER:
                moveToSpeaker();
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
        switch (state) {
            case MOVING_TO_START:
            case START:
            case MOVING_TO_NEUTRAL:
            case NEUTRAL:
            case MOVING_TO_HOLDING_NOTE:
            case HOLDING_NOTE:
            case OUTAKING:
            // in these states, we drive manually
                /*tjf = null;
                if (drivetrain.getDriveMode() == DriveMode.AUTO)
                    drivetrain.setDriveMode(
                        intake.hasNote()
                        ? Misc.kDefaultDriveModeWithNote
                        : Misc.kDefaultDriveModeWithoutNote
                    );*/
                switch (drivetrain.getDriveMode()) {
                    case FIELD_FORWARD:
                        if (Util.checkDriverDeadband(driver.getLeftMagnitude()))
                            drivetrain.setFieldDriveInputs(Util.scaleDriverInput(driver.getLeftMagnitude()),
                                    driver.getLeftAngle().rotateBy(FieldElements.directions.towardOtherWall()));
                        else
                            drivetrain.stop();
                        break;
                    case FIELD_REVERSED:
                        if (Util.checkDriverDeadband(driver.getLeftMagnitude()))
                            drivetrain.setFieldDriveInputs(Util.scaleDriverInput(driver.getLeftMagnitude()),
                                    driver.getLeftAngle().rotateBy(Rotation2d.fromDegrees(180))
                                            .rotateBy(FieldElements.directions.towardOtherWall()));
                        else
                            drivetrain.stop();
                        break;
                    case LOCAL_FORWARD:
                        if (Util.checkDriverDeadband(driver.getRightY()) || Util.checkDriverDeadband(driver.getLeftX()))
                            drivetrain.setLocalDriveInputs(Util.scaleDriverInput(driver.getRightY()),
                                    MechanismConstraints.drivetrain.kMaxAngularVelocity.times(driver.getLeftX()).negate());
                        else
                            drivetrain.stop();
                        break;
                    case LOCAL_REVERSED:
                        if (Util.checkDriverDeadband(driver.getRightY()) || Util.checkDriverDeadband(driver.getLeftX()))
                            drivetrain.setLocalDriveInputs(Util.scaleDriverInput(driver.getRightY()).negate(),
                                    MechanismConstraints.drivetrain.kMaxAngularVelocity.times(driver.getLeftX()).negate());
                        else
                            drivetrain.stop();
                        break;
                    default:
                        /*if (Misc.debug) {
                            System.err.println("[EE] Auto driving in non-auto robot state");
                            new Exception().printStackTrace();
                        }*/
                        break;
                }
                break;
            default:
            // in all other states, we follow a trajectory
            // FIXME don't know if this is right
                if (tjf == null || tjf.isDone()) {
                    driver.rumble(2);
                    operator.rumble(2);
                    if (Misc.debug) {
                        System.err.println("[EE] Tried to self-drive without a trajectory to follow");
                        new Exception().printStackTrace();
                    }
                    cancelAction();
                } else {
                    drivetrain.setDriveMode(DriveMode.AUTO);
                    drivetrain.setAutoDriveInputs(tjf.getWheelSpeeds(drivetrain.getPose()));
                }
        }
    }

    public void handleInputs() {
        if (operator.getCircleButtonPressed() || driver.getCircleButtonPressed()) {
            cancelAction();
        }



        if (operator.getSquareButton()) {
            shooter.speaker();
            if (shooter.reachedSetPoint()){
                intake.shoot();
            }
        }
        if (operator.getTriangleButtonPressed()) {
            neutralPosition();
        }
        if (operator.getR2Button()) {
            shooter.manualShoot();
        }
        if (operator.getR2ButtonReleased()){
            intake.shoot();
        }
        if (operator.getR1ButtonPressed()) {
            outakeNote();
        }
        if (operator.getL1ButtonPressed()) {
            intake.off();
            
        }
        if (operator.getL2Button()) { 
            arm.floor();
            intake.intake();
        }
        if (operator.getL2ButtonReleased()){
            intake.off();
        }



        if (driver.getR2ButtonPressed()) {
            moveToSpeaker();
        }
        if (driver.getL2ButtonPressed()) {
            moveToAmp();
        }
        if (driver.getR1ButtonPressed()) {
            drivetrain.reverse();
        }
        if (driver.getL1ButtonPressed()) {
            drivetrain.localFieldSwitch();
        }
        if (driver.getCrossButtonPressed()) {
            prepareForClimb();
        }
        if (driver.getSquareButtonPressed()) {
            autoScore();
        }
        if (driver.getTriangleButtonPressed()) {
            moveToIntaking();
        } 
    }

    /**
     * Control the arm, intake, and shooter.
     */
    public void manageMechanisms() {
        switch (state) {
            // Order cases in reverse chronological order when grouped.
            // This way, when everyone in the group shares some basic functions,
            // but later ones add in a function (like running the intake to shoot),
            // we can keep them condensed together.

					case START:
					case MOVING_TO_START:
                arm.startPosition();
                intake.off();
                shooter.off();
						break;

					case MOVING_TO_NEUTRAL:
					case MOVING_TO_HOLDING_NOTE:
                arm.holding();
                intake.off();
                shooter.off();
						break;

					case NEUTRAL:
					case HOLDING_NOTE:
                        if (Util.checkDriverDeadband(operator.getRightY())){
                            arm.motor.setVelocity(Util.scaleArmInput(operator.getRightY()));
                        }
					case FOLLOWING_TRAJECTORY:
                // don't continue setting motor states: this allows manual control in this state
						break;

					case INTAKING:
					case MOVING_TO_INTAKING:
                arm.floor();
                intake.intake();
						break;

					case OUTAKING:
                intake.outake();
						break;

					case SCORING_AMP: // add intake action
                intake.shoot();
					case READY_TO_SCORE_AMP:
					case MOVING_TO_AMP:
                arm.amp();
                shooter.amp();
						break;

					case SCORING_SPEAKER:
                intake.shoot();
					case READY_TO_SCORE_SPEAKER:
					case MOVING_TO_SPEAKER:
                arm.speaker();
                shooter.speaker();
						break;

					case PREPARING_FOR_CLIMB:
                intake.off();
                shooter.off();
                arm.climbReady();
						break;
					case CLIMBING:
					case HUNG:
                arm.clumb();
						break;

					case SELF_RIGHTING:
                // TODO check if we need to push number back
                arm.selfRighting();
					case KNOCKED_OVER:
                intake.off();
                shooter.off();
						break;
					default:
						break;
        }
    }

    public void managePathfinding() {
        switch (state) {
            case MOVING_TO_INTAKING:
                if (tjf == null) tjf = hat.findPathToNote();
                break;
            case MOVING_TO_AMP:
                if (tjf == null) tjf = hat.findPathToAmp();
                break;
            case MOVING_TO_SPEAKER:
                if (tjf == null) tjf = hat.findPathToSpeaker();
                break;
            default:
                break;
        }
    }

    /**
     * Manage the flow of the state machine that directs robot activity.
     */
    public void manageState() {
        switch (state) {
            case MOVING_TO_START:
                if (arm.reachedSetPoint())
                    state = RobotState.START;
                break;
            case START:
                break;

            case MOVING_TO_INTAKING:
                if (arm.reachedSetPoint())
                    state = RobotState.INTAKING;
                break;
            case INTAKING:
                if (intake.hasNote())
                    state = RobotState.MOVING_TO_HOLDING_NOTE;
                break;

            case OUTAKING:
                if (automateScoring && !intake.hasNote())
                    state = RobotState.NEUTRAL;
                break;

            case MOVING_TO_HOLDING_NOTE:
                state = RobotState.HOLDING_NOTE;
                break;

            case HOLDING_NOTE:
                break;

            case MOVING_TO_AMP:
                if (arm.reachedSetPoint() && shooter.reachedSetPoint())
                    state = RobotState.READY_TO_SCORE_AMP;
                break;
            case READY_TO_SCORE_AMP:
                if (isScoringAutomated())
                    state = RobotState.SCORING_AMP;
                break;
            case SCORING_AMP:
                if (shooter.noteGone())
                    state = RobotState.NEUTRAL;
                break;

            case MOVING_TO_SPEAKER:
                if (arm.reachedSetPoint() && shooter.reachedSetPoint() && tjf.isDone())
                    state = RobotState.READY_TO_SCORE_SPEAKER;
                break;
            case READY_TO_SCORE_SPEAKER:
                if (isScoringAutomated())
                    state = RobotState.SCORING_SPEAKER;
                break;
            case SCORING_SPEAKER:
                if (shooter.noteGone()) {
                    state = RobotState.NEUTRAL;
                }
                break;

            case PREPARING_FOR_CLIMB:
                if (isScoringAutomated() && arm.reachedSetPoint()) {
                    state = RobotState.CLIMBING;
                }
                break;
            case CLIMBING:
                if (arm.reachedSetPoint()) {
                    state = RobotState.HUNG;
                }
                break;
            case HUNG:
                break;
            case MOVING_TO_NEUTRAL:
                state = RobotState.NEUTRAL;
                break;
            case NEUTRAL:
               break;
            case KNOCKED_OVER:
                state = RobotState.SELF_RIGHTING;
                break;
            case SELF_RIGHTING:
                break;
            case FOLLOWING_TRAJECTORY:
                if (tjf.isDone())
                    neutralPosition();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoop() {
        receiveOptions();

        handleInputs();
        manageState();
        handleDriving();

        // Tell all the subsystems to do their thing for this cycle
        for (ISubsystem s : subsystems)
            s.onLoop();

        submitTelemetry();
    }

    @Override
    public void submitTelemetry() {
        SmartDashboard.putString("sup/state", getState().toString());
        SmartDashboard.putString("sup/drivemode", drivetrain.getDriveMode().toString());
    }
}