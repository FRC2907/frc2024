package frc.robot.auto.actions;

import java.util.function.BooleanSupplier;

import frc.robot.auto.actions.templates.Action;
import frc.robot.subsystems.Superduperstructure;
import frc.robot.subsystems.TrajectoryFollower;
import frc.robot.subsystems.Superduperstructure.RobotState;

public class FollowTrajectory extends Action {
    private Superduperstructure superduperstructure = Superduperstructure.getInstance();

    private TrajectoryFollower path;
    private BooleanSupplier quit_condition;

    public FollowTrajectory(TrajectoryFollower path) {
        this(path, () -> false);
    }
    public FollowTrajectory(TrajectoryFollower path, BooleanSupplier quit_condition) {
        this.path = path;
        this.quit_condition = quit_condition;
    }

    @Override
    public void onStart() {
        superduperstructure.followTrajectory(path);
    }

    @Override
    public void whileRunning() {
        if (superduperstructure.getState() == RobotState.NEUTRAL
                || superduperstructure.getState() == RobotState.HOLDING_NOTE
                || quit_condition.getAsBoolean()
            ) {
            this.running = false;
            this.finished = true;
        }
    }

    @Override
    public void onCleanup() {
    }
    
}