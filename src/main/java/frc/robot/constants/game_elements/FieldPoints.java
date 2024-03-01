package frc.robot.constants.game_elements;

import java.util.List;

import edu.wpi.first.math.geometry.Translation2d;

public class FieldPoints {
      public final Translation2d kAmp, kSpeaker, kStage, kSource;
      public final List<Translation2d> kWingNotes;

      public FieldPoints(Translation2d kAmp, Translation2d kSpeaker, Translation2d kStage, Translation2d kSource, List<Translation2d> kWingNotes) {
        this.kAmp = kAmp;
        this.kSpeaker = kSpeaker;
        this.kStage = kStage;
        this.kSource = kSource;
        this.kWingNotes = kWingNotes;
      }

      public FieldPoints(Translation2d kAmp, Translation2d kSpeaker, Translation2d kStage, Translation2d kSource, Translation2d[] kWingNotes) {
        this(kAmp, kSpeaker, kStage, kSource, List.of(kWingNotes));
      }

}
