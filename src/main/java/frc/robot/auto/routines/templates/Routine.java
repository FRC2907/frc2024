package frc.robot.auto.routines.templates;

import java.util.ArrayList;
import java.util.List;

import frc.robot.auto.actions.templates.Action;
import frc.robot.auto.actions.templates.SerialAction;

public class Routine extends SerialAction {
    public Routine(String _name, Action... _actions) {
        super(_actions);
        this.name = _name;
        routines.add(this);
    }

    public void onLoop() {
        if (!this.isDone())
            super.onLoop();
    }

    private static List<Routine> routines = new ArrayList<>();
    public static List<Routine> getRoutines() { return routines; }
    public static List<String> getRoutineNames() { return routines.stream().map(r -> r.getName()).toList(); }
    public static Routine getRoutineByName(String name) {
        return routines.stream().filter(r -> r.getName() == name).toList().get(0);
    }

    private String name;
    public String getName() { return name; }
}