package frc.robot.constants.game_elements;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.*;
import frc.robot.constants.MechanismConstraints.drivetrain;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Util;
import frc.robot.util.Geometry.ScoringRegion;

// https://firstfrc.blob.core.windows.net/frc2024/FieldAssets/2024FieldDrawings.pdf circa p. 240

/*    BLUE                        RED
 * 90 -------------------------------
 * deg|  |      |    |    |      |  |
 *    |  |      |    |    |      |  |
 *    |  |      |    |    |      |  |
 *    |  |      |    |    |      |  |
 * Y+ |  |      |    |    |      |  |
 * ^  \-----------------------------/
 *    O  >X+      SCORING           F  0 deg
 */

public class FieldElements {
  public class lengths {
    public class x {
      // stage center measured with a ruler held up to the screen
      public static final Measure<Distance> kField = Units.Inches.of(651.223);
      public static final Measure<Distance> kMidline = kField.divide(2.0);

      // FIXME there must be a better way to do this
      public class blue {
        private static final Measure<Distance> kReferenceWall = Units.Inches.of(0);
        public static final Measure<Distance> kAllianceWall = kReferenceWall;
        public static final double kOffsetDirection = Rotation2d.fromDegrees(0).getCos();

        public static final Measure<Distance> kWing = kReferenceWall
            .plus(Units.Inches.of(231.201).times(kOffsetDirection));
        public static final Measure<Distance> kStartingArea = kReferenceWall
            .plus(Units.Inches.of(76.111).times(kOffsetDirection));
        public static final Measure<Distance> kAmpCenter = kReferenceWall
            .plus(Units.Inches.of(72.455).times(kOffsetDirection));
        public static final Measure<Distance> kSpeakerCenter = kReferenceWall
            .plus(Units.Inches.of(0).times(kOffsetDirection));
        public static final Measure<Distance> kStageCenter = kReferenceWall
            .plus(kMidline.times(4.125 / 7.0).times(kOffsetDirection));
        public static final Measure<Distance> kWingNotes = kStartingArea.plus(Units.Inches.of(39.90));
      }

      public class red {
        private static final Measure<Distance> kReferenceWall = kField;
        public static final Measure<Distance> kAllianceWall = kReferenceWall;
        public static final double kOffsetDirection = Rotation2d.fromDegrees(180).getCos();

        public static final Measure<Distance> kWing = kReferenceWall
            .plus(Units.Inches.of(231.201).times(kOffsetDirection));
        public static final Measure<Distance> kStartingArea = kReferenceWall
            .plus(Units.Inches.of(76.111).times(kOffsetDirection));
        public static final Measure<Distance> kAmpCenter = kReferenceWall
            .plus(Units.Inches.of(72.455).times(kOffsetDirection));
        public static final Measure<Distance> kSpeakerCenter = kReferenceWall
            .plus(Units.Inches.of(0).times(kOffsetDirection));
        public static final Measure<Distance> kStageCenter = kReferenceWall
            .plus(kMidline.times(4.125 / 7.0).times(kOffsetDirection));
        public static final Measure<Distance> kWingNotes = kStartingArea.plus(Units.Inches.of(39.90));
      }
    }

    public class y {
      public static final Measure<Distance> kField = Units.Inches.of(323.277);
      public static final Measure<Distance> kMidfield = kField.divide(2.0);
      public static final List<Measure<Distance>> kMidfieldNotes = List.of(
          kMidfield.plus(Units.Inches.of(66 * 2)), kMidfield.plus(Units.Inches.of(66 * 1)), kMidfield,
          kMidfield.minus(Units.Inches.of(66 * 1)), kMidfield.minus(Units.Inches.of(66 * 2)));

      public class blue {
        public static final Measure<Distance> kReferenceWall = Units.Inches.of(0);
        public static final double kOffsetDirection = Rotation2d.fromDegrees(90).getSin();

        public static final Measure<Distance> kSpeakerCenter = kReferenceWall
            .plus(Units.Inches.of(218.416).times(kOffsetDirection));
        public static final Measure<Distance> kAmpCenter = kReferenceWall.plus(kField.times(kOffsetDirection));
        public static final Measure<Distance> kStageCenter = kReferenceWall
            .plus(kField.divide(2.0).times(kOffsetDirection));
        public static final List<Measure<Distance>> kWingNotes = List.of(
            kMidfield, kMidfield.minus(Units.Inches.of(57 * 1).times(kOffsetDirection)),
            kMidfield.minus(Units.Inches.of(57 * 2).times(kOffsetDirection)));
      }

      public class red {
        public static final Measure<Distance> kReferenceWall = Units.Inches.of(0);
        public static final double kOffsetDirection = Rotation2d.fromDegrees(90).getSin();

        public static final Measure<Distance> kSpeakerCenter = kReferenceWall
            .plus(Units.Inches.of(218.416).times(kOffsetDirection));
        public static final Measure<Distance> kAmpCenter = kReferenceWall.plus(kField.times(kOffsetDirection));
        public static final Measure<Distance> kStageCenter = kReferenceWall
            .plus(kField.divide(2.0).times(kOffsetDirection));
        public static final List<Measure<Distance>> kWingNotes = List.of(
            kMidfield, kMidfield.minus(Units.Inches.of(57 * 1).times(kOffsetDirection)),
            kMidfield.minus(Units.Inches.of(57 * 2).times(kOffsetDirection)));
      }
    }

  }

  public class points {
    public static final List<Translation2d> kMidfieldNotes = lengths.y.kMidfieldNotes.stream()
        .map(y -> new Translation2d(lengths.x.kMidline, y)).toList();

    public static FieldPoints blue = new FieldPoints(
      new Translation2d(lengths.x.blue.kAmpCenter, lengths.y.blue.kAmpCenter),
      new Translation2d(lengths.x.blue.kSpeakerCenter, lengths.y.blue.kSpeakerCenter),
      new Translation2d(lengths.x.blue.kStageCenter, lengths.y.blue.kStageCenter),
      new Translation2d(lengths.x.red.kAllianceWall, lengths.y.blue.kReferenceWall),
      lengths.y.blue.kWingNotes.stream().map(y -> new Translation2d(lengths.x.blue.kWingNotes, y)).toList()
    );
    public static FieldPoints red = new FieldPoints(
      new Translation2d(lengths.x.red.kAmpCenter, lengths.y.red.kAmpCenter),
      new Translation2d(lengths.x.red.kSpeakerCenter, lengths.y.red.kSpeakerCenter),
      new Translation2d(lengths.x.red.kStageCenter, lengths.y.red.kStageCenter),
      new Translation2d(lengths.x.blue.kAllianceWall, lengths.y.red.kReferenceWall),
      lengths.y.red.kWingNotes.stream().map(y -> new Translation2d(lengths.x.red.kWingNotes, y)).toList()
    );

  }

  public static class scoring_regions {
    public static ScoringRegions blue = new ScoringRegions(
        ScoringRegion.of(
            new Pose2d(points.blue.kAmp, Rotation2d.fromDegrees(90)), Units.Inches.of(0).in(Units.Meters),
            Units.Inches.of(2).in(Units.Meters), Rotation2d.fromDegrees(20)),
        ScoringRegion.of(
            new Pose2d(points.blue.kSpeaker, Rotation2d.fromDegrees(0)), Units.Inches.of(60).in(Units.Meters),
            Units.Inches.of(120).in(Units.Meters), Rotation2d.fromDegrees(100)));

    public static ScoringRegions red = new ScoringRegions(
        ScoringRegion.of(
            new Pose2d(points.red.kAmp, Rotation2d.fromDegrees(90)), Units.Inches.of(0).in(Units.Meters),
            Units.Inches.of(2).in(Units.Meters), Rotation2d.fromDegrees(20)),
        ScoringRegion.of(
            new Pose2d(points.red.kSpeaker, Rotation2d.fromDegrees(0)), Units.Inches.of(60).in(Units.Meters),
            Units.Inches.of(120).in(Units.Meters), Rotation2d.fromDegrees(100)));
  }

  public static FieldPoints getFieldPoints() {
    return Util.isBlue() ? points.blue : points.red;
  }

  public static ScoringRegions getScoringRegions() {
    return Util.isBlue() ? scoring_regions.blue : scoring_regions.red;
  }

  public class directions{
    public static Rotation2d towardOtherWall(){
      return Util.isBlue() ? Rotation2d.fromDegrees(0) : Rotation2d.fromDegrees(180);
    }
    public static Rotation2d towardAllianceWall(){
      return Util.isBlue() ? Rotation2d.fromDegrees(180) : Rotation2d.fromDegrees(0);
    }
    public static Rotation2d left(){
      return Util.isBlue() ? Rotation2d.fromDegrees(90) : Rotation2d.fromDegrees(-90);
    }
    public static Rotation2d right(){
      return Util.isBlue() ? Rotation2d.fromDegrees(-90) : Rotation2d.fromDegrees(90);
    }
    public static Rotation2d towardStage(){
      return getFieldPoints().kStage.minus(Drivetrain.getInstance().getPose().getTranslation()).getAngle();
    }
    public static Rotation2d towardSource(){
      return getFieldPoints().kSource.minus(Drivetrain.getInstance().getPose().getTranslation()).getAngle();
    }
    public static Rotation2d towardTable(){
      return Rotation2d.fromDegrees(-90);
    }
    public static Rotation2d towardAudience(){
      return Rotation2d.fromDegrees(90);
    }
    public static Rotation2d towardBlue(){
      return Rotation2d.fromDegrees(180);
    }
    public static Rotation2d towardRed(){
      return Rotation2d.fromDegrees(0);
    }
    public static Rotation2d towardAmp(){
      return getFieldPoints().kAmp.minus(Drivetrain.getInstance().getPose().getTranslation()).getAngle();
    }
    public static Rotation2d towardSpeaker(){
      return getFieldPoints().kSpeaker.minus(Drivetrain.getInstance().getPose().getTranslation()).getAngle();
    }
  }
}
