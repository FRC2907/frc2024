package frc.robot.constants;

// TODO configure motor controllers to reflect these IDs. They should ideally also line up with PDP port numbers.
// TODO[lib,later] rewrite this in a multi-robot-friendly way
public class Ports {
    public static class can {
        public static class arm {
            public static final Integer[] MOTORS = { 1, 14 };
        }

        public static class drivetrain {
            public static final Integer[] LEFTS = { 12, 13 };
            public static final Integer[] RIGHTS = { 2, 3 };
        }

        public static class shooter {
            public static final Integer[] MOTORS = { 0, 15 };
        }

        public static class intake {
            public static final Integer[] MOTORS = { 4 };
        }
    }

    public static class HID {
        public static final Integer DRIVER = 0;
        public static final Integer OPERATOR = 1;
    }
}