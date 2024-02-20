package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;

public class LogAction extends Action {
    private String message;

    public LogAction(String _message) {
        this.message = _message;
    }

    @Override
    public void onStart() {
        this.started = true;
        this.running = true;
    }

    @Override
    public void whileRunning() {
        System.out.println("[II] " + this.message);
        this.running = false;
        this.finished = true;
    }

    @Override
    public void onCleanup() {}
    
}