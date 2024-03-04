package frc.robot.auto.routines.templates;

import java.util.List;
import java.util.function.Supplier;

import org.reflections.Reflections;

import frc.robot.auto.actions.templates.Action;
import frc.robot.auto.actions.templates.SerialAction;

public class Routine extends SerialAction {
    @SafeVarargs
    public Routine(String _name, Supplier<Action>... action_suppliers) {
        super(action_suppliers);
        this.name = _name;
    }

    @Override
    public void onLoop() {
        if (!this.isDone())
            super.onLoop();
    }

    public static List<Class<? extends Routine>> getRoutines() {
        Reflections reflections = new Reflections("frc.robot.auto.routines");
        List<Class<? extends Routine>> routines = reflections.getSubTypesOf(Routine.class).stream().toList();
        return routines;
    }

    private String name;
    public String getName() { return name; }
}