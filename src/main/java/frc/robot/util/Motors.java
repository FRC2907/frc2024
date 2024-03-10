package frc.robot.util;

import java.util.Arrays;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.Pair;
import frc.robot.bodges.rawrlib.motors.WrappedMotorController;
import frc.robot.bodges.rawrlib.motors.WrappedSparkMaxBrushless;
import frc.robot.bodges.rawrlib.motors.WrappedTalonFX;
import frc.robot.constants.Misc;

public class Motors {
    public class sparkmax {
        public static WrappedMotorController createGroup(Integer... ids) {
            return createGroup(false, ids);
        }

        public static WrappedMotorController createGroup(boolean inverted, Integer... ids) {
            CANSparkMax out = Arrays.stream(ids)
                    .map(id -> new CANSparkMax(id, MotorType.kBrushless))
                    .reduce((leader, follower) -> {
                        follower.follow(leader);
                        return leader;
                    }).get();
            out.setInverted(inverted);
            return new WrappedSparkMaxBrushless(out);
        }

        @SafeVarargs
        public static WrappedMotorController createGroup(Pair<Integer, Boolean>... specs) {
            if (specs.length == 0) {
                if (Misc.debug) {
                    System.err.println("[EE] Attempted to create empty group of CANSparkMax");
                    new Exception().printStackTrace();
                }
            }
            return new WrappedSparkMaxBrushless(Arrays.stream(specs)
                    .map(spec -> {
                        CANSparkMax out = new CANSparkMax(spec.getFirst(), MotorType.kBrushless);
                        out.setInverted(spec.getSecond());
                        return out;
                    }).reduce((leader, follower) -> {
                        System.out.println("init sparkmax id " + follower.getDeviceId() + " following id " + leader.getDeviceId());
                        follower.follow(leader);
                        return leader;
                    }).get());
        }

        public static WrappedMotorController createOpposedPair(Integer... ids) {
            return createOpposedPair(ids[0], ids[1]);
        }

        public static WrappedMotorController createOpposedPair(boolean inverted, Integer... ids) {
            return createOpposedPair(inverted, ids[0], ids[1]);
        }

        public static WrappedMotorController createOpposedPair(Integer leader_id, Integer follower_id) {
            return createOpposedPair(false, leader_id, follower_id);
        }

        public static WrappedMotorController createOpposedPair(boolean inverted, Integer leader_id, Integer follower_id) {
            CANSparkMax leader = new CANSparkMax(leader_id, MotorType.kBrushless);
            @SuppressWarnings({ "resource" })
            CANSparkMax follower = new CANSparkMax(follower_id, MotorType.kBrushless);
            System.out.println("init sparkmax id " + follower.getDeviceId() + " opposing id " + leader.getDeviceId());
            follower.follow(leader);
            leader.setInverted(inverted);
            follower.setInverted(!inverted);
            return new WrappedSparkMaxBrushless(leader);
        }
    }

    public class talonfx {
        public static WrappedMotorController createGroup(Integer... ids) {
            return createGroup(false, ids);
        }

        public static WrappedMotorController createGroup(boolean inverted, Integer... ids) {
            TalonFX out = Arrays.stream(ids)
                    .map(id -> new TalonFX(id))
                    .reduce((leader, follower) -> {
                        System.out.println("init talonfx id " + follower.getDeviceID() + " following id " + leader.getDeviceID());
                        follower.setControl(new Follower(ids[0], false));
                        return leader;
                    }).get();
            out.setInverted(inverted);
            return new WrappedTalonFX(out);
        }

        public static WrappedMotorController createOpposedPair(Integer... ids) {
            return createOpposedPair(ids[0], ids[1]);
        }

        public static WrappedMotorController createOpposedPair(boolean inverted, Integer... ids) {
            return createOpposedPair(inverted, ids[0], ids[1]);
        }

        public static WrappedMotorController createOpposedPair(Integer leader_id, Integer follower_id) {
            return createOpposedPair(false, leader_id, follower_id);
        }

        public static WrappedMotorController createOpposedPair(boolean inverted, Integer leader_id, Integer follower_id) {
            TalonFX leader = new TalonFX(leader_id);
            @SuppressWarnings({ "resource" })
            TalonFX follower = new TalonFX(follower_id);
            System.out.println("init talonfx id " + follower.getDeviceID() + " opposing id " + leader.getDeviceID());
            follower.setControl(new Follower(leader_id, true));
            leader.setInverted(inverted);
            return new WrappedTalonFX(leader);
        }

        // mixed orientations not permitted at this time because i am lazy and tired and
        // ctre is goofy
    }

}
