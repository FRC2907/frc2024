package frc.robot.util;
import edu.wpi.first.wpilibj.Timer;
//https://github.wpilib.org/allwpilib/docs/release/java/index.html

//This code is not testesd

public class util {
    public static boolean wait(double step_start,double duration){
        
        return new Timer().get() < step_start + duration;
    }
}
