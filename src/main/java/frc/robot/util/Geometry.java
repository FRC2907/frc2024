package frc.robot.util;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.*;

public class Geometry {

  public static class ScoringRegion {
    public final Pose2d inside, outside;
    public final Translation2d focus;
    public final Measure<Distance> in_radius, out_radius;
    public final Rotation2d left, right;

    private ScoringRegion(Translation2d focus, Measure<Distance> ir, Measure<Distance> or, Rotation2d a, Rotation2d c) {
      this.focus = focus;
      this.in_radius = ir;
      this.out_radius = or;
      this.left = a;
      this.right = c;
      this.inside = new Pose2d(ir, Units.Meters.zero(), new Rotation2d(Math.PI)).transformBy(new Transform2d(focus, a));
      this.outside = new Pose2d(or, Units.Meters.zero(), new Rotation2d(Math.PI)).transformBy(new Transform2d(focus, c));
    }
    public static ScoringRegion of(Translation2d focus, Measure<Distance> ir, Measure<Distance> or, Rotation2d a, Rotation2d c) {
      if (ir.lte(or)) return new ScoringRegion(focus, ir, or, a, c);
      else return new ScoringRegion(focus, or, ir, a, c);
    }
    public static ScoringRegion of(Pose2d focus, Measure<Distance> ir, Measure<Distance> or, Rotation2d width) {
      Translation2d focus_point = focus.getTranslation();
      Rotation2d a = focus.getRotation().unaryMinus().plus(width.div(2));
      Rotation2d c = focus.getRotation().unaryMinus().minus(width.div(2));
      if (ir.lte(or)) return new ScoringRegion(focus_point, ir, or, a, c);
      else return new ScoringRegion(focus_point, or, ir, a, c);
    }

    public static ScoringRegion circularRegion(Translation2d focus, Measure<Distance> ir, Measure<Distance> or) {
      return new ScoringRegion(focus, ir, or, Rotation2d.fromDegrees(-179.9), Rotation2d.fromDegrees(179.9));
    }

    public boolean contains(Pose2d pose) {
      Measure<Angle> l = Units.Degrees.of(left.getDegrees());
      Measure<Angle> p = Units.Degrees.of(pose.getRotation().getDegrees());
      Measure<Angle> r = Units.Degrees.of(right.getDegrees());
      Measure<Distance> distance = Units.Meters.of(pose.getTranslation().getDistance(focus));
      return Util.checkBetween(l, p, r) && Util.checkBetween(in_radius, distance, out_radius);
    }

    public Rotation2d getRotationForPoint(Translation2d point) {
      return point.minus(focus).unaryMinus().getAngle();
    }
    public Pose2d getPoseForPoint(Translation2d point) {
      return new Pose2d(point, getRotationForPoint(point));
    }
    public Pose2d center() {
      return inside.interpolate(outside, 0.5);
    }

    /**
     * Return a cloud of points across the region.
     * @param rd Radial divisions (circular rows)
     * @param td Angular divisions (straight rows)
     * @return
     */
    public Pose2d[] getPoints(int rd, int td) {
      // TODO[lib,later] handle the case of 1 for either
      rd = Math.max(2, rd);
      td = Math.max(2, td);
      Pose2d[] out = new Pose2d[rd*td];
      for (int i_t = 0; i_t < td; i_t++)
        for (int i_r = 0; i_r < rd; i_r++) {
          out[i_t*rd + i_r] = new Pose2d(
            inside.getTranslation().interpolate(outside.getTranslation(), i_r/(rd-1))
            , inside.getRotation().interpolate(outside.getRotation(), i_t/(td-1))
            );
        }
      return out;
    }

    public Pose2d getNearest(Pose2d source) {
      return getNearest(source, 5);
    }
    public Pose2d getNearest(Pose2d source, int resolution) {
      return source.nearest(List.of(getPoints(resolution, resolution)));
    }

  }

  @Deprecated
  public static class FieldOfView {
    //  Y+
    //   \
    //    A-----------B
    //     \            \
    //      \             \
    //       \              \
    //        \               \
    //         C----------------D---X+
    private final Translation2d A, B, C, D;

    public FieldOfView(Translation2d A, Translation2d B, Translation2d C, Translation2d D) {
      this.A = A;
      this.B = B;
      this.C = C;
      this.D = D;
    }

    public Translation2d interpolate(double x, double y) {
      Translation2d far = A.interpolate(B, x);
      Translation2d near = C.interpolate(D, x);
      return far.interpolate(near, y);
    }
  }

}