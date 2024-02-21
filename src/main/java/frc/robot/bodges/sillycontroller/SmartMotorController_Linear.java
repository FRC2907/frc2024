package frc.robot.bodges.sillycontroller;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.units.*;

 /**
  * You can overwrite PIDF gains for each mode after instantiation by calling the
  * setPD/setPIDF_*() methods. Unidentified linear units are probably meters.
  */
  // TODO braking
public class SmartMotorController_Linear {

    private final DownstreamControllerType type;
    private ProcessVariable mode;

    /* Supported downstream motor controllers */
    private final CANSparkMax spark;
    private final TalonFX talon;

    private SmartMotorControllerConfiguration_Linear config;
    private boolean positionControlConfigured = false, velocityControlConfigured = false;

    /**
     * Generate the leader of a group of controllers, where each is assigned its own
     * reversedness. With this constructor, the reversed field of _config is
     * ignored.
     * 
     * @param _type
     * @param can_ids
     * @param reversies
     * @param _config
     */
    public SmartMotorController_Linear(DownstreamControllerType _type, int[] can_ids, boolean[] reversies, SmartMotorControllerConfiguration_Linear _config) {
        if (can_ids.length < 1) {
			System.err.println("[EE] Attempted to create empty group of SmartMotorController");
			new Exception().printStackTrace();
        }
        if (can_ids.length != reversies.length) {
			System.err.println("[WW] Attempted to create group of SmartMotorController with mismatched number of controllers and reversednesses. Leftovers will cycle back through from the start of the reversies array");
			new Exception().printStackTrace();
            // i'm going to hell
        }
        this.type = _type;
        this.config = _config;
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                this.spark = new CANSparkMax(can_ids[0], MotorType.kBrushless);
                this.talon = null;
                ctor_SetupConfig();
                this.spark.setInverted(reversies[0]);

                for (int i = 1; i < can_ids.length; i++) {
                    @SuppressWarnings({"resource"})
                    CANSparkMax c = new CANSparkMax(can_ids[i], MotorType.kBrushless);
                    c.follow(this.spark, reversies[0] ^ reversies[i]);
                }
                break;
            case TALON_FX:
                this.spark = null;
                this.talon = new TalonFX(can_ids[0]);
                ctor_SetupConfig();
                this.talon.setInverted(reversies[0]);
                for (int i = 1; i < can_ids.length; i++) {
                    new TalonFX(can_ids[i]).setControl(new Follower(can_ids[0], reversies[0] ^ reversies[i]));
                }
                break;
            default:
            // FIXME we should probably perish
                System.err.println("[EE] Attempted to create group of SmartMotorController for unsupported downstream controller type");
                new Exception().printStackTrace();
                this.spark = null;
                this.talon = null;
                break;
        }
    }
    
    /**
     * Generate the leader of a group of synchronized controllers. All controllers
     * will run in the same direction (following whether _config tells them all to
     * reverse).
     * 
     * @param _type
     * @param can_ids
     * @param _config
     */
    public SmartMotorController_Linear(DownstreamControllerType _type, int[] can_ids, SmartMotorControllerConfiguration_Linear _config) {
        if (can_ids.length < 1) {
			System.err.println("[EE] Attempted to create empty group of SmartMotorController");
			new Exception().printStackTrace();
            // but we stay silly :3
        }
        this.type = _type;
        this.config = _config;
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                this.spark = new CANSparkMax(can_ids[0], MotorType.kBrushless);
                this.talon = null;
                ctor_SetupConfig(); // set up reversies before followies
                for (int i = 1; i < can_ids.length; i++) {
                    new CANSparkMax(can_ids[i], MotorType.kBrushless).follow(this.spark);
                }
                break;
            case TALON_FX:
                this.spark = null;
                this.talon = new TalonFX(can_ids[0]);
                ctor_SetupConfig();
                for (int i = 1; i < can_ids.length; i++) {
                    new TalonFX(can_ids[i]).setControl(new Follower(can_ids[0], false));
                }
                break;
            default:
            // FIXME we should probably perish
                System.err.println("[EE] Attempted to create group of SmartMotorController for unsupported downstream controller type");
                new Exception().printStackTrace();
                this.spark = null;
                this.talon = null;
                break;
        }
    }
    public SmartMotorController_Linear(DownstreamControllerType _type, int can_id, SmartMotorControllerConfiguration_Linear _config) {
        this.type = _type;
        this.config = _config;
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                this.spark = new CANSparkMax(can_id, MotorType.kBrushless);
                this.talon = null;
                break;
            case TALON_FX:
                this.spark = null;
                this.talon = new TalonFX(can_id);
                break;
            default:
            // FIXME we should probably perish
                System.err.println("[EE] Attempted to create SmartMotorController for unsupported downstream controller type");
                new Exception().printStackTrace();
                this.spark = null;
                this.talon = null;
                break;
        }
        ctor_SetupConfig();
    }
    public SmartMotorController_Linear(CANSparkMax downstream, SmartMotorControllerConfiguration_Linear _config) {
        this.type = DownstreamControllerType.SPARK_MAX_BRUSHLESS;
        this.config = _config;
        this.spark = downstream;
        this.talon = null;
        ctor_SetupConfig();
    }
    public SmartMotorController_Linear(TalonFX downstream, SmartMotorControllerConfiguration_Linear _config) {
        this.type = DownstreamControllerType.TALON_FX;
        this.config = _config;
        this.spark = null;
        this.talon = downstream;
        ctor_SetupConfig();
    }

    private void ctor_SetupConfig() {
        switch(type) {
            case SPARK_MAX_BRUSHLESS:
                spark.setInverted(config.reversed);
                if (config.mechanismPositionPerEncoderAngularPosition != null)
                    spark.getEncoder().setPositionConversionFactor(
                        config.mechanismPositionPerEncoderAngularPosition.in(Units.Meters.per(Units.Revolutions))
                    );
                if (config.mechanismVelocityPerEncoderAngularVelocity != null)
                    spark.getEncoder().setVelocityConversionFactor(
                        config.mechanismVelocityPerEncoderAngularVelocity.in(Units.MetersPerSecond.per(Units.RPM))
                    );
                break;
            case TALON_FX:
                talon.setInverted(config.reversed);
                // FIXME does this actually translate reported feedback numbers? who knows!
                if (config.mechanismPositionPerEncoderAngularPosition != null) {
                    FeedbackConfigs conversions = new FeedbackConfigs()
                        .withFeedbackSensorSource(FeedbackSensorSourceValue.RotorSensor)
                        .withSensorToMechanismRatio(
                            1.0/config.mechanismPositionPerEncoderAngularPosition.in(Units.Meters.per(Units.Revolutions))
                        );
                    talon.getConfigurator().apply(conversions);
                }
                break;
        }

        if (this.config.pidf_position != null)
            this.setPIDF_position(config.pidf_position);
        if (this.config.pidf_velocity != null)
            this.setPIDF_velocity(config.pidf_velocity);
        this.setPIDF_raw(0,0,0,0);
        if (!this.positionControlConfigured && !this.velocityControlConfigured) {
            System.err.println("[WW] Created a SmartMotorController with no PIDF gains for any mode! This motor will not function until you assign gains.");
            new Exception().printStackTrace();
        }
    }

    public void setPIDF_raw(double P, double I, double D, double F) {
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                spark.getPIDController().setP (P);
                spark.getPIDController().setI (I);
                spark.getPIDController().setD (D);
                spark.getPIDController().setFF(F);
                break;
            case TALON_FX:
                var slotConfig = new SlotConfigs();
                slotConfig.kP = P;
                slotConfig.kI = I;
                slotConfig.kD = D;
                slotConfig.kS = F;
                talon.getConfigurator().apply(slotConfig);
                break;
        }
    }
    /** Sets P and D gains, and zeroes I and F gains. */;
    public void setPD_raw(double P, double D) {
        setPIDF_raw(P, 0, D, 0);
    }

    /**
     * Sets PIDF gains, and saves them for use whenever position control is
     * requested.
     */
    public void setPIDF_position(PIDF<Distance> pidf) {
        config.pidf_position = pidf;
        positionControlConfigured = true;
        setPIDF_raw(
            pidf.kP == null ? 0 : pidf.kP.in(Units.Volts.per(Units.Meters))
            , pidf.kI == null ? 0 : pidf.kI.in(Units.Volts.mult(Units.Seconds).per(Units.Meters))
            , pidf.kD == null ? 0 : pidf.kD.in(Units.Volts.per(Units.MetersPerSecond))
            , pidf.kF == null ? 0 : pidf.kF.in(Units.Volts)
        );
    }

    /**
     * Sets P and D gains, and zeroes I and F gains. These gains are saved for
     * future position control requests.
     */
    public void setPD_position(PIDF<Distance> pidf) {
        setPIDF_raw(
            pidf.kP.in(Units.Volts.per(Units.Meters))
            , 0
            , pidf.kD.in(Units.Volts.per(Units.MetersPerSecond))
            , 0
        );
    }

    /**
     * Sets PIDF gains, and saves them for use whenever velocity control is
     * requested.
     */
    public void setPIDF_velocity(PIDF<Velocity<Distance>> pidf) {
        config.pidf_velocity = pidf;
        velocityControlConfigured = true;
        setPIDF_raw(
            pidf.kP == null ? 0 : pidf.kP.in(Units.Volts.per(Units.MetersPerSecond))
            , pidf.kI == null ? 0 : pidf.kI.in(Units.Volts.mult(Units.Seconds).per(Units.MetersPerSecond))
            , pidf.kD == null ? 0 : pidf.kD.in(Units.Volts.per(Units.MetersPerSecondPerSecond))
            , pidf.kF == null ? 0 : pidf.kF.in(Units.Volts)
        );
    }
    
    /**
     * Sets P and D gains, and zeroes I and F gains. These gains are saved for
     * future velocity control requests.
     */
    public void setPD_velocity(PIDF<Velocity<Distance>> pidf) {
        setPIDF_raw(
            pidf.kP.in(Units.Volts.per(Units.MetersPerSecond))
            , 0
            , pidf.kD.in(Units.Volts.per(Units.MetersPerSecondPerSecond))
            , 0
        );
    }

    /**
     * Restore PIDF gains for position control and target a given position.
     * @param r reference position
     */
    public void setPosition(Measure<Distance> r) {
        if (!positionControlConfigured) {
            System.err.println("[EE] Attempted to use position control on a SmartMotorController with no position gains!");
            new Exception().printStackTrace();
            return;
        }
        if (mode != ProcessVariable.POSITION) {
            setPIDF_position(config.pidf_position);
            mode = ProcessVariable.POSITION;
        }
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                spark.getPIDController().setReference(r.in(Units.Meters), ControlType.kPosition);
                break;
            case TALON_FX:
            // TODO verify units?
                talon.setControl(new PositionVoltage(r.in(Units.Meters)));
                break;
        }
    }

    /**
     * Restore PIDF gains for velocity control and target a given velocity.
     * 
     * @param r reference velocity
     */
    public void setVelocity(Measure<Velocity<Distance>> r) {
        if (!velocityControlConfigured) {
            System.err.println("[EE] Attempted to use velocity control on a SmartMotorController with no velocity gains!");
            new Exception().printStackTrace();
            return;
        }
        if (mode != ProcessVariable.VELOCITY) {
            setPIDF_velocity(config.pidf_velocity);
            mode = ProcessVariable.VELOCITY;
        }
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                spark.getPIDController().setReference(r.in(Units.MetersPerSecond), ControlType.kVelocity);
                break;
            case TALON_FX:
            // TODO verify units?
                talon.setControl(new VelocityVoltage(r.in(Units.MetersPerSecond)));
                break;
        }
    }

    public Measure<Distance> getPosition() {
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                return Units.Meters.of(spark.getEncoder().getPosition());
            case TALON_FX:
                return Units.Meters.of(talon.getPosition().getValueAsDouble());
            default: return null;
        }
    }
    public Measure<Velocity<Distance>> getVelocity() {
        switch (type) {
            case SPARK_MAX_BRUSHLESS:
                return Units.MetersPerSecond.of(spark.getEncoder().getVelocity());
            case TALON_FX:
                return Units.MetersPerSecond.of(talon.getVelocity().getValueAsDouble());
            default: return null;
        }
    }
}
