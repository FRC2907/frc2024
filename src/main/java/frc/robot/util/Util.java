package frc.robot.util;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class util {
	public static boolean wait(double step_start,double duration){
		return new Timer().get() < step_start + duration;
	}

	public static CANSparkMax createSparkGroup(int[] ids) {
		CANSparkMax[] mcs = new CANSparkMax[ids.length];
		for (int i = 0; i < ids.length; i++) {
			mcs[i] = new CANSparkMax(ids[i], MotorType.kBrushless);
			if (i > 0) mcs[i].follow(mcs[0]);
		}
		return mcs[0];
	}
}
