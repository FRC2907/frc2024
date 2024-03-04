package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;

public class DoNothingAction extends Action {
    public DoNothingAction() {
    }

    @Override
    public void onStart() {
        this.started = true;
        this.running = false;
        this.finished = true;
    }

    @Override
    public void whileRunning() {
        this.running = false;
        this.finished = true;
    }

    @Override
    public void onCleanup() {}
}
