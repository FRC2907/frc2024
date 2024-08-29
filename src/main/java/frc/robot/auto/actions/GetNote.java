package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;
import frc.robot.subsystems.Superduperstructure;
import frc.robot.subsystems.Superduperstructure.RobotState;

public class GetNote extends Action {
    private Superduperstructure superduperstructure = Superduperstructure.getInstance();

    @Override
    public void onStart() {
        superduperstructure.moveToIntaking();
    }

    @Override
    public void whileRunning() {
        if (superduperstructure.getState() == RobotState.NEUTRAL || superduperstructure.getState() == RobotState.HOLDING_NOTE){
            this.running = false;
            this.finished = true;
        }
    }

    @Override
    public void onCleanup() {
    }
    
}