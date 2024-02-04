package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;

/** DEPRECATED; not for new designs */
@Deprecated
public abstract class Subsystem {
    private Timer timer = new Timer();
    private double lastTime, lastDt;

    protected double getTime() {
        return this.lastTime;
    }

    protected double getDt() {
        return this.lastDt;
    }

    protected void onInit() {
        this.timer.reset();
        this.timer.start();
    }

    protected void onLoopTasks() {
        double now = this.timer.get();
        this.lastDt = now - this.lastTime;
        this.lastTime = now;
    }
}
