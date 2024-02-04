package frc.robot.auto.routines.templates;

import frc.robot.auto.actions.templates.Action;
import frc.robot.auto.actions.templates.SerialAction;

public class Routine extends SerialAction {
    public Routine(Action... _actions) {
        super(_actions);
    }

    public void onLoop() {
        if (!this.isDone())
            super.onLoop();
    }
}
