package frc.robot.auto.actions.templates;

import java.util.function.Supplier;

public class SerialAction extends ParallelAction {

    protected Action currentAction;

    @SafeVarargs
    public SerialAction(Supplier<Action>... action_suppliers) {
        super(action_suppliers);
    }

    @Override
    public void onStart() {
        this.currentAction = action_suppliers.remove(0).get();
    }

    @Override
    public void onLoop() {
        if (!currentAction.isStarted()) {
            currentAction.onStart();
        } else if (currentAction.isDone()) {
            currentAction.onCleanup();
            if (action_suppliers.size() > 0) {
                onStart();
            }
        } else {
            currentAction.onLoop();
        }
    }

    @Override
    public boolean isDone() {
        return this.action_suppliers.size() == 0 && currentAction.isDone();
    }

}