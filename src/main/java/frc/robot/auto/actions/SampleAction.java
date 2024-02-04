package frc.robot.auto.actions;

import frc.robot.auto.actions.templates.Action;

public class SampleAction extends Action {
    private String name;
    private int cyclesToRun, cyclesRun;

    public SampleAction(String _name) {
        this.name = _name;
        this.cyclesRun = 0;
        this.cyclesToRun = (int)Math.round(Math.random() * 9) + 1;
    }

    @Override
    public void onStart() {
        System.out.println("[II] Sample action " + this.name + ".onStart()");
        this.started = true;
        this.running = true;
    }

    @Override
    public void whileRunning() {
        this.cyclesRun += 1;
        System.out.println("[II] Sample action " + this.name + ".whileRunning() " + this.cyclesRun + "/" + this.cyclesToRun);
        if (this.cyclesRun >= this.cyclesToRun) {
            this.running = false;
            this.finished = true;
        }
    }

    @Override
    public void onCleanup() {
        System.out.println("[II] Sample action " + this.name + ".onCleanup()");
    }
    
}
