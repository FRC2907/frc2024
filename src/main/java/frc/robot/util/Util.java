package frc.robot.util;

import edu.wpi.first.math.geometry.Rotation2d;
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
		if (max < min) { 
			return clamp(max, value, min);
		}
		if (value < min)
			return min;
		if (max < value)
			return max;
		return value;
	}

	public static double clamp12(double value) {
		return clamp(-12.0, value, 12.0);
	}

	public static Rotation2d clamp(Rotation2d min, Rotation2d value, Rotation2d max) {
		return Rotation2d.fromRotations(clamp(min.getRotations(), value.getRotations(), max.getRotations()));
	}
}
