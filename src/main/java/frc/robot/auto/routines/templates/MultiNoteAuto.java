package frc.robot.auto.routines.templates;

import java.util.List;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.auto.actions.DriveToIntake;
import frc.robot.auto.actions.GetNote;
import frc.robot.auto.actions.templates.Action;

public class MultiNoteAuto extends Routine {

  public MultiNoteAuto(String name, List<Supplier<Action>> goals, List<Translation2d> notes) {
    super(name);
    int list_length_diff = goals.size() - notes.size();
    if (list_length_diff != 0 && list_length_diff != 1) {
      System.err.println("Mismatched list lengths in MultiNoteAuto ctor: can't score " + goals.size() + " goals with " + notes.size() + "+1 notes. Truncating routine to whichever is shorter...");
    }

    while (goals.size() > 0 && notes.size() > 0) {
      this.add(goals.remove(0));
      this.add(() -> new DriveToIntake(notes.remove(0)));
      this.add(GetNote::new);
    }

    if (list_length_diff != 0 && list_length_diff != 1) {
      System.err.println("Assembled routine with an extra " + goals.size() + " goals and " + notes.size() + " notes.");
    }
  }
}
