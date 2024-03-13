package frc.robot.constants;

import edu.wpi.first.wpilibj.I2C.Port;

// TODO configure motor controllers to reflect these IDs. They should ideally also line up with PDP port numbers + 1 (0 may be bcast).
// TODO[lib,later] rewrite this in a multi-robot-friendly way
public class Ports {
    public static class CAN {
        public static class arm {
            public static final Integer LEFT = 3;
            public static final Integer RIGHT = 14;
        }

        public static class drivetrain {
            public static final Integer[] LEFTS = { 1, 2 };
            public static final Integer[] RIGHTS = { 15, 16 };
        }

        public static class shooter {
            public static final Integer[] MOTORS = { 4, 13 };
        }

        public static class intake {
            public static final Integer[] MOTORS = { 12 };
        }
    }

    public static class HID {
        public static final Integer DRIVER = 0;
        public static final Integer OPERATOR = 1;
    }

    public static class I2C {
        public static final Port kIntakeSensor = Port.kOnboard; // TODO
    }
}