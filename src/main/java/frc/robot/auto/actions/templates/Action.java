package frc.robot.auto.actions.templates;

public abstract class Action {
    protected boolean started = false, running = false, finished = false;
    protected Action nextAction;

    public void onLoop() {
             if (!this.isStarted())   { this.onStart();      }
        else if (this.isRunning())    { this.whileRunning(); }
        else                          { this.onCleanup();    }
    }

    public boolean isStarted() { return this.started; }
    public boolean isRunning() { return this.running; }
    public boolean isDone()    { return this.finished; }

    public abstract void onStart();
    public abstract void whileRunning();
    public abstract void onCleanup();
}
