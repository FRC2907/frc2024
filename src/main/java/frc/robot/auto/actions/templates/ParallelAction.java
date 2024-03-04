package frc.robot.auto.actions.templates;

import java.util.List;
import java.util.function.Supplier;

import frc.robot.auto.actions.DoNothingAction;
import frc.robot.util.Util;

public class ParallelAction extends Action {
    protected List<Supplier<Action>> action_suppliers;
    protected List<Action> actions = Util.asList();

    protected ParallelAction add(Supplier<Action> tail) {
        action_suppliers.add(tail);
        return this;
    }

    @SafeVarargs
    public ParallelAction(Supplier<Action>... action_suppliers) {
        this.action_suppliers = Util.asList(action_suppliers);
        if (this.action_suppliers.size() < 1)
            this.action_suppliers.add(DoNothingAction::new);
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
        for (Supplier<Action> sa : this.action_suppliers) this.actions.add(sa.get());
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