package frc.robot.util;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Unit;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Util {
	public static boolean wait(double step_start, double duration) {
		return new Timer().get() < step_start + duration;
	}

	public static CANSparkMax createSparkGroup(int[] ids) {
		return createSparkGroup(ids, false);
	}

	public static CANSparkMax createSparkGroup(int[] ids, boolean invert) {
		if (ids.length == 0) {
			System.err.println("[EE] Attempted to create empty group of CANSparkMax");
			new Exception().printStackTrace();
		}
		CANSparkMax[] mcs = new CANSparkMax[ids.length];
		for (int i = 0; i < ids.length; i++) {
			mcs[i] = new CANSparkMax(ids[i], MotorType.kBrushless);
			if (i > 0)
				mcs[i].follow(mcs[0]);
		}
		mcs[0].setInverted(invert);
		return mcs[0];
	}

	public static double clamp(double min, double value, double max) {
		if (max < min) return clamp(max, value, min);
		if (value < min) return min;
		if (max < value) return max;
		return value;
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

	public static <U extends Unit<U>> boolean checkHysteresis(Measure<U> error, Measure<U> hysteresis) {
		return hysteresis.negate().lt(error) && error.lt(hysteresis);
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
}