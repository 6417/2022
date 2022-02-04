package frc.robot.subsystems.climber;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.FridolinsMotor.DirectionType;
import ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice;
import ch.fridolins.fridowpi.motors.FridolinsMotor.IdleMode;
import ch.fridolins.fridowpi.motors.FridolinsMotor.LimitSwitchPolarity;

public class TelescopeArm extends Module {
    public static final class Constants {
        private static final double motorSpeed = 0.1;
        private static final double threshold = 0.1;
    }

    private static class Params {
        private final int firstMotorId;
        private final int secondMotorId;
        private final boolean inverted;

        public Params(int firstMotorId, int secondMotorId, boolean inverted) {
            this.firstMotorId = firstMotorId;
            this.secondMotorId = secondMotorId;
            this.inverted = inverted;
        }
    }

    private final Params params;

    private FridolinsMotor motor;
    private FridolinsMotor followMotor;

    private double position;

    @Override
    public void init() {
        motor = new FridoCanSparkMax(params.firstMotorId, MotorType.kBrushless);
        followMotor = new FridoCanSparkMax(params.secondMotorId, MotorType.kBrushless);

        motor.factoryDefault();
        followMotor.factoryDefault();

        motor.setInverted(params.inverted);

        motor.setIdleMode(IdleMode.kBrake);
        followMotor.setIdleMode(IdleMode.kBrake);

        followMotor.follow(motor, DirectionType.followMaster);

        motor.configEncoder(FridoFeedBackDevice.kBuildin, 42);
        followMotor.configEncoder(FridoFeedBackDevice.kBuildin, 42);

        motor.enableForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen, true);
        motor.enableReverseLimitSwitch(LimitSwitchPolarity.kNormallyOpen, true);

        TelescopeArm.this.registerSubmodule(motor);
        TelescopeArm.this.registerSubmodule(followMotor);
    }

    public TelescopeArm(Params params) {
        this.params = params;
    }

    public void gotoZeroPosition() {
        motor.set(-Constants.motorSpeed);
    }

    public void setPosition(double position) {
        this.position = position;
        motor.setPosition(position);
    }

    public void resetEncoderTicks() {
        motor.setEncoderPosition(0);
    }

    public boolean isPidFinished() {
        return (Math.abs(position - motor.getEncoderTicks()) <= Constants.threshold);
    }
}
