package frc.robot.game_elements;

import java.util.List;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Units;

public class FieldPoints {
      public final Translation2d kAmp, kSpeaker, kStage, kSource;
      public final List<Translation2d> kWingNotes;
      public final Translation3d kSpeakerHole;

      public FieldPoints(Translation2d kAmp, Translation2d kSpeaker, Translation2d kStage, Translation2d kSource, List<Translation2d> kWingNotes, Measure<Distance> kSpeakerZ) {
        this.kAmp = kAmp;
        this.kSpeaker = kSpeaker;
        this.kStage = kStage;
        this.kSource = kSource;
        this.kWingNotes = kWingNotes;
        this.kSpeakerHole = new Translation3d(Units.Meters.of(kSpeaker.getX()), Units.Meters.of(kSpeaker.getY()), kSpeakerZ);
      }

      public FieldPoints(Translation2d kAmp, Translation2d kSpeaker, Translation2d kStage, Translation2d kSource, Translation2d[] kWingNotes, Measure<Distance> kSpeakerZ) {
        this(kAmp, kSpeaker, kStage, kSource, List.of(kWingNotes), kSpeakerZ);
      }

}
