package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;
import frc.robot.subsystems.Superduperstructure;
import frc.robot.subsystems.Superduperstructure.RobotState;

public class ScoreAmp extends Action {
    private Superduperstructure superstructure = Superduperstructure.getInstance();

    @Override
    public void onStart() {
        superstructure.moveToAmp();
    }

    @Override
    public void whileRunning() {
        if (superstructure.getState() == RobotState.NEUTRAL)
        this.running = false;
        this.finished = true;
    }

    @Override
    public void onCleanup() {
    }
    
}
