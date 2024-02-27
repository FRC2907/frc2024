package frc.robot.util;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;

public class Geometry {

  public static class ScoringRegion {
    public final Pose2d inside, outside;
    public final Translation2d focus;
    public final double in_radius, out_radius;
    public final Rotation2d left, right;

    private ScoringRegion(Translation2d focus, double ir, double or, Rotation2d a, Rotation2d c) {
      this.focus = focus;
      this.in_radius = ir;
      this.out_radius = or;
      this.left = a;
      this.right = c;
      this.inside = new Pose2d(ir, 0, new Rotation2d(Math.PI)).transformBy(new Transform2d(focus, a));
      this.outside = new Pose2d(or, 0, new Rotation2d(Math.PI)).transformBy(new Transform2d(focus, c));
    }
    public static ScoringRegion of(Translation2d focus, double ir, double or, Rotation2d a, Rotation2d c) {
      if (ir <= or) return new ScoringRegion(focus, ir, or, a, c);
      else return new ScoringRegion(focus, or, ir, a, c);
    }
    public static ScoringRegion of(Pose2d focus, double ir, double or, Rotation2d width) {
      Translation2d focus_point = focus.getTranslation();
      Rotation2d a = focus.getRotation().unaryMinus().plus(width.div(2));
      Rotation2d c = focus.getRotation().unaryMinus().minus(width.div(2));
      if (ir <= or) return new ScoringRegion(focus_point, ir, or, a, c);
      else return new ScoringRegion(focus_point, or, ir, a, c);
    }

    public boolean contains(Pose2d pose) {
      double l = left.getDegrees(), p = pose.getRotation().getDegrees(), r = right.getDegrees();
      double distance = pose.getTranslation().getDistance(focus);
      return ((l <= p && p <= r) || (l >= p && p >= r)) && (in_radius <= distance && distance <= out_radius);
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
      // TODO handle the case of 1 for either
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

}