package frc.robot.bodges;

import edu.wpi.first.units.*;

/**
 * PIDF<U> represents dimensionally-accurate control gains for a controlled dimension U.
 */
public class PIDF<U extends Unit<U>> {
    public final Measure<Per     <Voltage,          U >> kP;
    public final Measure<Per<Mult<Voltage, Time>,   U >> kI;
    public final Measure<Per     <Voltage, Velocity<U>>> kD;
    public final Measure<         Voltage              > kF;

    public PIDF(
      Measure<Per     <Voltage,          U >> P
    , Measure<Per<Mult<Voltage, Time>,   U >> I
    , Measure<Per     <Voltage, Velocity<U>>> D
    , Measure<         Voltage              > F
    ) {
        this.kP = P;
        this.kI = I;
        this.kD = D;
        this.kF = F;
    }
}
