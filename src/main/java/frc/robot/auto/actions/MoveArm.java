package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;
import frc.robot.subsystems.Superduperstructure;
import frc.robot.subsystems.Superduperstructure.RobotState;

public class MoveArm extends Action {
    private Superduperstructure superduperstructure = Superduperstructure.getInstance();

    @Override
    public void onStart() {
        superduperstructure.moveToSpeaker();
        superduperstructure.automateScoring(true);
    }

    @Override
    public void whileRunning() {
        if (superduperstructure.getState() == RobotState.NEUTRAL){
            this.running = false;
            this.finished = true;
        }
    }

    @Override
    public void onCleanup() {}
    
}