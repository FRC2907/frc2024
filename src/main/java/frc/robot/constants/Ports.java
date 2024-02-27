package frc.robot.constants;

// FIXME all motor IDs are placeholders for simulation and need to be updated to fit the robot
public class Ports {
    public static class can {
        public static class arm {
            public static final Integer[] MOTORS = {  };
        }

        public static class drivetrain {
            public static final Integer[] LEFTS = { 1 };
            public static final Integer[] RIGHTS = { 3, 4 };
        }

        public static class shooter {
            public static final Integer[] MOTORS = {  };
        }

        public static class intake {
            public static final Integer[] MOTORS = {  };
        }
    }

    public static class HID {
        public static final Integer DRIVER = 0;
        public static final Integer OPERATOR = 1;
    }
}