package frc.robot.constants;

// FIXME all motor IDs are placeholders for simulation and need to be updated to fit the robot
public class Ports {
    public static class can {
        public static class drivetrain {
            public static final int[] LEFTS = { 1 };
            public static final int[] RIGHTS = {  };
        }

        public static class arm {
            public static final int[] MOTORS = {  };
        }

        public static class shooter {
            public static final int[] MOTORS = {  };
        }

        public static class intake {
            public static final int[] MOTORS = {  };
        }
    }

    public static class HID {
        public static final int DRIVER = 0;
        public static final int OPERATOR = 1;
    }
}