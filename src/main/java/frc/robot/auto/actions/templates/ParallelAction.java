package frc.robot.auto.actions.templates;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ParallelAction extends Action {
    protected List<Action> actions;

    public ParallelAction(Action... _actions) {
        this.actions = new ArrayList<>(Arrays.asList(_actions));
    }

    @Override
    public boolean isStarted() {
        boolean out = false; // if any are started, then the action is started
        for (Action a : this.actions) out |= a.isStarted();
        return out;
    }
    @Override
    public boolean isRunning() {
        boolean out = false; // if any are running, then the action is running
        for (Action a : this.actions) out |= a.isRunning();
        return out;
    }
    @Override
    public boolean isDone() {
        boolean out = true; // if any are unfinished, then the action is unfinished
        for (Action a : this.actions) out &= a.isDone();
        return out;
    }

    @Override
    public void onStart() {
        for (Action a : this.actions) a.onStart();
    }

    @Override
    public void whileRunning() {
        for (Action a : this.actions) if (a.isRunning()) a.whileRunning();
    }

    @Override
    public void onCleanup() {
        for (Action a : this.actions) a.onCleanup();
    }
}