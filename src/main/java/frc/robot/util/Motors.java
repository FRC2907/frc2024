package frc.robot.util;

import java.util.Arrays;

import com.ctre.phoenix6.controls.Follower;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.bodges.StupidSparkMax;
import frc.robot.bodges.StupidTalonFX;
import frc.robot.constants.Misc;

public class Motors {
    public class sparkmax {
        public static StupidSparkMax createGroup(Integer... ids) {
            return createGroup(false, ids);
        }

        public static StupidSparkMax createGroup(boolean inverted, Integer... ids) {
            return createGroup(inverted, 1.0, new PIDController(0, 0, 0), new PIDController(0, 0, 0), ids);
        }

        public static StupidSparkMax createGroup(boolean inverted, double factor, PIDController position_pid,
                PIDController velocity_pid, Integer... ids) {
            StupidSparkMax out = Arrays.stream(ids)
                    .map(id -> new StupidSparkMax(id, MotorType.kBrushless))
                    .reduce((leader, follower) -> {
                        follower.follow(leader);
                        return leader;
                    }).get();
            out.setInverted(inverted);
            out.setFactor(factor);
            out.setPositionController(position_pid);
            out.setVelocityController(velocity_pid);
            return out;
        }

        @SafeVarargs
        public static StupidSparkMax createGroup(Pair<Integer, Boolean>... specs) {
            if (specs.length == 0) {
                if (Misc.debug) {
                    System.err.println("[EE] Attempted to create empty group of CANSparkMax");
                    new Exception().printStackTrace();
                }
            }
            return Arrays.stream(specs)
                    .map(spec -> {
                        StupidSparkMax out = new StupidSparkMax(spec.getFirst(), MotorType.kBrushless);
                        out.setInverted(spec.getSecond());
                        return out;
                    }).reduce((leader, follower) -> {
                        follower.follow(leader);
                        return leader;
                    }).get();
        }

        public static StupidSparkMax createOpposedPair(Integer... ids) {
            return createOpposedPair(ids[0], ids[1]);
        }

        public static StupidSparkMax createOpposedPair(boolean inverted, Integer... ids) {
            return createOpposedPair(inverted, ids[0], ids[1]);
        }

        public static StupidSparkMax createOpposedPair(Integer leader_id, Integer follower_id) {
            return createOpposedPair(false, leader_id, follower_id);
        }

        public static StupidSparkMax createOpposedPair(boolean inverted, Integer leader_id, Integer follower_id) {
            return createOpposedPair(inverted, 1.0, new PIDController(0, 0, 0), new PIDController(0, 0, 0), leader_id,
                    follower_id);
        }

        public static StupidSparkMax createOpposedPair(boolean inverted, double factor, PIDController position_pid,
                PIDController velocity_pid, Integer... ids) {
            return createOpposedPair(inverted, factor, position_pid, velocity_pid, ids[0], ids[1]);
        }

        public static StupidSparkMax createOpposedPair(boolean inverted, double factor, PIDController position_pid,
                PIDController velocity_pid, Integer leader_id, Integer follower_id) {
            StupidSparkMax leader = new StupidSparkMax(leader_id, MotorType.kBrushless);
            @SuppressWarnings({ "resource" })
            StupidSparkMax follower = new StupidSparkMax(follower_id, MotorType.kBrushless);
            follower.follow(leader);
            leader.setInverted(inverted);
            follower.setInverted(!inverted);
            leader.setFactor(factor);
            leader.setPositionController(position_pid);
            leader.setVelocityController(velocity_pid);
            return leader;
        }
    }

    public class talonfx {
        public static StupidTalonFX createGroup(Integer... ids) {
            return createGroup(false, ids);
        }

        public static StupidTalonFX createGroup(boolean inverted, Integer... ids) {
            return createGroup(inverted, 1.0, new PIDController(0, 0, 0), new PIDController(0, 0, 0), ids);
        }

        public static StupidTalonFX createGroup(boolean inverted, double factor, PIDController position_pid,
                PIDController velocity_pid, Integer... ids) {
            StupidTalonFX out = Arrays.stream(ids)
                    .map(id -> new StupidTalonFX(id))
                    .reduce((leader, follower) -> {
                        follower.setControl(new Follower(ids[0], false));
                        return leader;
                    }).get();
            out.setInverted(inverted);
            out.setFactor(factor);
            out.setPositionController(position_pid);
            out.setVelocityController(velocity_pid);
            return out;
        }

        public static StupidTalonFX createOpposedPair(Integer... ids) {
            return createOpposedPair(ids[0], ids[1]);
        }

        public static StupidTalonFX createOpposedPair(boolean inverted, Integer... ids) {
            return createOpposedPair(inverted, ids[0], ids[1]);
        }

        public static StupidTalonFX createOpposedPair(Integer leader_id, Integer follower_id) {
            return createOpposedPair(false, leader_id, follower_id);
        }

        public static StupidTalonFX createOpposedPair(boolean inverted, Integer leader_id, Integer follower_id) {
            return createOpposedPair(inverted, 1.0, new PIDController(0, 0, 0), new PIDController(0, 0, 0), leader_id,
                    follower_id);
        }

        public static StupidTalonFX createOpposedPair(boolean inverted, double factor, PIDController position_pid,
                PIDController velocity_pid, Integer... ids) {
            return createOpposedPair(inverted, factor, position_pid, velocity_pid, ids[0], ids[1]);
        }

        public static StupidTalonFX createOpposedPair(boolean inverted, double factor, PIDController position_pid,
                PIDController velocity_pid, Integer leader_id, Integer follower_id) {
            StupidTalonFX leader = new StupidTalonFX(leader_id);
            @SuppressWarnings({ "resource" })
            StupidTalonFX follower = new StupidTalonFX(follower_id);
            follower.setControl(new Follower(leader_id, true));
            leader.setInverted(inverted);
            leader.setFactor(factor);
            leader.setPositionController(position_pid);
            leader.setVelocityController(velocity_pid);
            return leader;
        }

        // mixed orientations not permitted at this time because i am lazy and tired and
        // ctre is goofy
    }

}
