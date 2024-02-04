package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.Superstructure.RobotState;

public class ScoreAmp extends Action {
    private Superstructure superstructure = Superstructure.getInstance();

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
