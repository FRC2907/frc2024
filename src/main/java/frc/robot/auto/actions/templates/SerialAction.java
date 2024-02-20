package frc.robot.auto.actions.templates;

public class SerialAction extends ParallelAction {

    public SerialAction(Action... _actions) {
        super(_actions);
    }

    public void debug(String tag) {
        System.out.println(tag);
        for (Action a : this.actions)
            System.out.println(a.toString() + a.started + a.running + a.finished);
    }
    public void debug() { debug(""); }

    @Override
    public void onLoop() {
        System.out.println("[DD] SerialAction::onLoop()");
        if (this.actions.size() > 0) {
            Action current = this.actions.get(0);
            if (current.isDone()) {
            current.onCleanup();
            this.actions.remove(0);
            }
            else {
                current.onLoop();
            }
        } else {
            System.out.println("[II] No more actions to execute");
        }
    }

    @Override
    public boolean isDone() { return this.actions.size() == 0; }
    
}