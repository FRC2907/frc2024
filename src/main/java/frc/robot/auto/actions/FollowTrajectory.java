package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;
import frc.robot.subsystems.Superduperstructure;
import frc.robot.subsystems.TrajectoryFollower;
import frc.robot.subsystems.Superduperstructure.RobotState;

public class FollowTrajectory extends Action {
    private Superduperstructure superduperstructure = Superduperstructure.getInstance();

    private TrajectoryFollower path;

    public FollowTrajectory(TrajectoryFollower GetWheelSpeeds) {
        this.path = GetWheelSpeeds;
    }

    @Override
    public void onStart() {
        superduperstructure.followTrajectory(path);
    }

    @Override
    public void whileRunning() {
        if (superduperstructure.getState() == RobotState.NEUTRAL){
            this.running = false;
            this.finished = true;
        }
    }

    @Override
    public void onCleanup() {
    }
    
}