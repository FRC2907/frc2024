package frc.robot.util;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Util {
	public static boolean wait(double step_start,double duration){
		return new Timer().get() < step_start + duration;
	}

	public static CANSparkMax createSparkGroup(int[] ids) { return createSparkGroup(ids, false); }
	public static CANSparkMax createSparkGroup(int[] ids, boolean invert) {
		CANSparkMax[] mcs = new CANSparkMax[ids.length];
		for (int i = 0; i < ids.length; i++) {
			mcs[i] = new CANSparkMax(ids[i], MotorType.kBrushless);
			if (i > 0) mcs[i].follow(mcs[0]);
		}
		mcs[0].setInverted(invert);
		return mcs[0];
	}

	public static double clamp(double min, double value, double max) {
		if (max < min) // FIXME we really oughta like, catch this or something
			System.err.println("[EE] I was asked to clamp value " + value + " between min " + min + " and max " + max);
		if (value < min) return min;
		if (max < value) return max;
		return value;
	}

	public static double clamp12(double value) { return clamp(-12.0, value, 12.0); }
}
