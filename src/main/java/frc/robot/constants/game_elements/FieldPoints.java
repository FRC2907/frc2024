package frc.robot.constants.game_elements;

import java.util.List;

import edu.wpi.first.math.geometry.Translation2d;

public class FieldPoints {
      public final Translation2d kAmp, kSpeaker, kStage;
      public final List<Translation2d> kWingNotes;

      public FieldPoints(Translation2d kAmp, Translation2d kSpeaker, Translation2d kStage, List<Translation2d> kWingNotes) {
        this.kAmp = kAmp;
        this.kSpeaker = kSpeaker;
        this.kStage = kStage;
        this.kWingNotes = kWingNotes;
      }

      public FieldPoints(Translation2d kAmp, Translation2d kSpeaker, Translation2d kStage, Translation2d[] kWingNotes) {
        this(kAmp, kSpeaker, kStage, List.of(kWingNotes));
      }

}
