package frc.robot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.constants.MechanismConstraints;

public class Util {
	public static boolean wait(double step_start, double duration) {
		return new Timer().get() < step_start + duration;
	}

	public static double clamp(double min, double value, double max) {
		if (max < min) return clamp(max, value, min);
		if (value < min) return min;
		if (max < value) return max;
		return value;
	}

	public static double clampSymmetrical(double value, double cap) {
		return clamp(-cap, value, cap);
	}

  public static double clampV(double value) {
		return clampSymmetrical(value, MechanismConstraints.electrical.kMaxVoltage.in(Units.Volts));
  }

  public static Measure<Voltage> clampV(Measure<Voltage> value) {
		return clampSymmetrical(value, MechanismConstraints.electrical.kMaxVoltage);
  }

	public static double clamp12(double value) {
		return clamp(-12.0, value, 12.0);
	}

	public static Rotation2d clamp(Rotation2d min, Rotation2d value, Rotation2d max) {
		return Rotation2d.fromRotations(clamp(min.getRotations(), value.getRotations(), max.getRotations()));
	}

	public static <U extends Unit<U>> Measure<U> clamp(Measure<U> min, Measure<U> value, Measure<U> max) {
		if (max.lt(min)) return clamp(max, value, min);
		if (value.lt(min)) return min;
		if (max.lt(value)) return max;
		return value;
	}

	public static <U extends Unit<U>> Measure<U> clampSymmetrical(Measure<U> value, Measure<U> cap) {
			return clamp(cap.negate(), value, cap);
	}

	public static <U extends Unit<U>> boolean checkBetween(Measure<U> low, Measure<U> value, Measure<U> high) {
		if (low.gt(high)) return checkBetween(high, value, low);
		return low.lte(value) && value.lte(high);
	}

	public static <U extends Unit<U>> boolean checkHysteresis(Measure<U> error, Measure<U> hysteresis) {
		return hysteresis.negate().lt(error) && error.lt(hysteresis);
	}

	public static Measure<Angle> normalize360(Measure<Angle> value) {
		Measure<Angle> out = value;//.plus(Units.Degrees.of(180));
		while (out.in(Units.Degrees) >= 360)
			out = out.minus(Units.Degrees.of(360));
		while (out.in(Units.Degrees) < 0)
			out = out.plus(Units.Degrees.of(360));
		return out;
	}

	public static Measure<Angle> normalize180(Measure<Angle> value) {
		return normalize360(value.plus(Units.Degrees.of(180))).minus(Units.Degrees.of(180));
	}

	public static Rotation2d normalize360(Rotation2d value) {
		return new Rotation2d(normalize360(Units.Degrees.of(value.getDegrees())));
	}

	public static Rotation2d normalize180(Rotation2d value) {
		return new Rotation2d(normalize180(Units.Degrees.of(value.getDegrees())));
	}

	public static double[] normalizeSymmetrical(double cap, double... values) {
		if (values.length < 1) return values;
		if (cap < 0) return normalizeSymmetrical(-cap, values);
		double[] out = new double[values.length];
		if (cap == 0) {
			for (int i = 0; i < out.length; i++)
				out[i] = 0;
			return out;
		}

		// at this point, cap >= 0 and values has at least 1 element
		double max = values[0], min = max;
		for (double v : values) {
			if (max < v) max = v;
			if (v < min) min = v;
		}
		if (-cap <= min && max <= cap) {
			for (int i = 0; i < out.length; i++)
				out[i] = values[i];
			return out;
		}

		// at this point, we need to normalize something
		double factor = Math.abs(max) > Math.abs(min) ? Math.abs(max) : Math.abs(min);
		factor = cap/factor;
			for (int i = 0; i < out.length; i++)
				out[i] = values[i]*factor;
		return out;
	}
	
	public static boolean isBlue(){
		if (DriverStation.getAlliance().isPresent()){
      	return DriverStation.getAlliance().get() == Alliance.Blue;
		}
		return true;
	}

  public static <T extends Unit<T>> Measure<T> initializeOrAdd(Measure<T> collector, Measure<T> other) {
    if (collector == null) return other;
    return collector.plus(other);
  }

	public static double fuzz() { return Math.random() * 0.000001; }

	public static Measure<Voltage> angleTimesVoltagePerAngle(Measure<Angle> angle, Measure<Per<Voltage, Angle>> rate) {
		return Units.Volts.of(angle.in(Units.Rotations) * rate.in(Units.Volts.per(Units.Rotations)));
	}
	public static Measure<Voltage> angleTimeTimesVoltagePerAngleTime(Measure<Mult<Angle,Time>> angleSum, Measure<Per<Voltage, Mult<Angle,Time>>> rate) {
		return Units.Volts.of(angleSum.in(Units.Rotations.mult(Units.Seconds)) * rate.in(Units.Volts.per(Units.Rotations.mult(Units.Seconds))));
	}
	public static Measure<Voltage> angleSpeedTimesVoltagePerAngleSpeed(Measure<Velocity<Angle>> angle, Measure<Per<Voltage, Velocity<Angle>>> rate) {
		return Units.Volts.of(angle.in(Units.RotationsPerSecond) * rate.in(Units.Volts.per(Units.RotationsPerSecond)));
	}
	public static Measure<Voltage> angleSpeedTimeTimesVoltagePerAngleSpeedTime(Measure<Mult<Velocity<Angle>,Time>> angleSum, Measure<Per<Voltage, Mult<Velocity<Angle>,Time>>> rate) {
		return Units.Volts.of(angleSum.in(Units.RotationsPerSecond.mult(Units.Seconds)) * rate.in(Units.Volts.per(Units.RotationsPerSecond.mult(Units.Seconds))));
	}
	public static Measure<Voltage> angleSpeedSpeedTimesVoltagePerAngleSpeedSpeed(Measure<Velocity<Velocity<Angle>>> angle, Measure<Per<Voltage, Velocity<Velocity<Angle>>>> rate) {
		return Units.Volts.of(angle.in(Units.RotationsPerSecond.per(Units.Second)) * rate.in(Units.Volts.per(Units.RotationsPerSecond.per(Units.Second))));
	}

	public static Measure<Voltage> distanceTimesVoltagePerDistance(Measure<Distance> angle, Measure<Per<Voltage, Distance>> rate) {
		return Units.Volts.of(angle.in(Units.Meters) * rate.in(Units.Volts.per(Units.Meters)));
	}
	public static Measure<Voltage> distanceTimeTimesVoltagePerDistanceTime(Measure<Mult<Distance,Time>> angleSum, Measure<Per<Voltage, Mult<Distance,Time>>> rate) {
		return Units.Volts.of(angleSum.in(Units.Meters.mult(Units.Seconds)) * rate.in(Units.Volts.per(Units.Meters.mult(Units.Seconds))));
	}
	public static Measure<Voltage> distanceSpeedTimesVoltagePerDistanceSpeed(Measure<Velocity<Distance>> angle, Measure<Per<Voltage, Velocity<Distance>>> rate) {
		return Units.Volts.of(angle.in(Units.MetersPerSecond) * rate.in(Units.Volts.per(Units.MetersPerSecond)));
	}
	public static Measure<Voltage> distanceSpeedTimeTimesVoltagePerDistanceSpeedTime(Measure<Mult<Velocity<Distance>,Time>> distanceSum, Measure<Per<Voltage, Mult<Velocity<Distance>,Time>>> rate) {
		return Units.Volts.of(distanceSum.in(Units.MetersPerSecond.mult(Units.Seconds)) * rate.in(Units.Volts.per(Units.MetersPerSecond.mult(Units.Seconds))));
	}
	public static Measure<Voltage> distanceSpeedSpeedTimesVoltagePerDistanceSpeedSpeed(Measure<Velocity<Velocity<Distance>>> distance, Measure<Per<Voltage, Velocity<Velocity<Distance>>>> rate) {
		return Units.Volts.of(distance.in(Units.MetersPerSecond.per(Units.Second)) * rate.in(Units.Volts.per(Units.MetersPerSecond.per(Units.Second))));
	}

	public static Measure<?> anyZero() { return Units.Value.zero().times(Units.Value.zero()); }

	@SuppressWarnings("unchecked")
	public static <T extends Unit<T>> boolean isNegative(Measure<T> value) {
		return value.lt((Measure<T>) anyZero());
	}
	@SuppressWarnings("unchecked")
	public static <T extends Unit<T>> boolean isPositive(Measure<T> value) {
		return value.gt((Measure<T>) anyZero());
	}
	@SuppressWarnings("unchecked")
	public static <T extends Unit<T>> boolean isNonNegative(Measure<T> value) {
		return value.gte((Measure<T>) anyZero());
	}
	@SuppressWarnings("unchecked")
	public static <T extends Unit<T>> boolean isNonPositive(Measure<T> value) {
		return value.lte((Measure<T>) anyZero());
	}
	@SuppressWarnings("unchecked")
	public static <T extends Unit<T>> boolean isZero(Measure<T> value) {
		return value.isNear((Measure<T>) anyZero(), 0.000001);
	}
	public static <T extends Unit<T>> Measure<T> abs(Measure<T> value) {
		return isNegative(value) ? value.negate() : value;
	}

	// TODO[lib,later] move a bunch of this stuff to a util.Units library

	public static <T> List<T> removeNulls(List<T> values) {
		return values.stream().filter(x -> x != null).toList();
	}
	public static <T> List<T> unwrapOptionals(List<Optional<T>> values) {
		return values.stream().map(x -> x.get()).toList();
	}
	public static <T> List<T> unwrapOptionalValues(List<Optional<T>> values) {
		return removeNulls(unwrapOptionals(values));
	}
	public static <T extends Unit<T>> Measure<T> min(List<Measure<T>> values) {
		return removeNulls(values).stream().min((a,b) -> a.compareTo(b)).get();
	}
	public static <T extends Unit<T>> Measure<T> max(List<Measure<T>> values) {
		return removeNulls(values).stream().max((a,b) -> a.compareTo(b)).get();
	}
	@SafeVarargs
	public static <T extends Unit<T>> Measure<T> min(Measure<T>... values) {
		return min(asList(values));
	}
	@SafeVarargs
	public static <T extends Unit<T>> Measure<T> max(Measure<T>... values) {
		return max(asList(values));
	}
	@SafeVarargs
	public static <T extends Unit<T>> Measure<T> min(Optional<Measure<T>>... values) {
		return min(unwrapOptionalValues(asList(values)));
	}
	@SafeVarargs
	public static <T extends Unit<T>> Measure<T> max(Optional<Measure<T>>... values) {
		return max(unwrapOptionalValues(asList(values)));
	}

	@SafeVarargs
	public static <T> List<T> asList(T... values) {
		return new ArrayList<T>(Arrays.asList(values));
	}

	public static double signedSquare(double value) { return value * Math.abs(value); }

	public static Measure<Velocity<Distance>> scaleDriverInput(double value) {
		return MechanismConstraints.drivetrain.kMaxVelocity.times(
				MechanismConstraints.drivetrain.kSquareInputs ? signedSquare(value) : value);
	}
	public static boolean checkDriverDeadband(double value) {
		return Math.abs(value) > MechanismConstraints.drivetrain.kDriverDeadband;
	}

	public static Pose2d pointToPose(Pose2d start, Translation2d end) {
		Rotation2d startHeading = start.getRotation();
		Rotation2d headingToPoint = end.minus(start.getTranslation()).getAngle();
		Rotation2d difference = headingToPoint.minus(startHeading);
		return new Pose2d(end, headingToPoint.plus(difference));
	}
}