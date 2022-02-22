package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.Binding;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.joystick.joysticks.Logitech;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;

import java.util.List;
import java.util.Optional;

import ch.fridolins.fridowpi.motors.utils.PidValues;
import ch.fridolins.fridowpi.sensors.Navx;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Joysticks;
import frc.robot.subsystems.climber.TelescopeArm.Constants;
import frc.robot.subsystems.climber.base.TilterBase;

public class Tilter extends TilterBase {
    private static TilterBase instance = null;
    private static final boolean enabled = true;

    public static final class Constants {
        public class Positions {
            public static final double traversalPosition = 0;
        }

        public static final int motorId = 0;
        public static final FridolinsMotor.LimitSwitchPolarity forwardLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kDisabled;
        public static final FridolinsMotor.LimitSwitchPolarity reverseLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kDisabled;

        public static final double zeroingSpeed = 0.0;

        // TODO: use corresponding limit switches
        public static final TilterHook.Params hookParams = new TilterHook.Params(0, true, null, null);

        public static final PidValues pid = new PidValues(0.0, 0.0, 0.0);
        public static final double kF = 1.0;

        static {
            pid.setTolerance(0.0);
        }
    }

    private FridolinsMotor motor;
    private TilterHook hook;
    private Thread updateKF;
    private Vector2d pointOfMass = new Vector2d(0.0, 0.0);

    private Tilter() {
        hook = new TilterHook(Constants.hookParams);
        requires(hook);
        Initializer.getInstance().addInitialisable(this);
        JoystickHandler.getInstance().bind(this);
    }

    private double calcFeedForward() {
        return Constants.kF * (Math.cos(Navx.getInstance().getPitch()) * pointOfMass.x - Math.sin(Navx.getInstance().getPitch())) / Math.cos(Navx.getInstance().getPitch());
    }

    private void updateFeedForward() {
        PidValues pid = Constants.pid;
        pid.kF = Optional.of(calcFeedForward());
        motor.setPID(Constants.pid);
    }

    @Override
    public void init() {
        super.init();

        motor = new FridoCanSparkMax(Constants.motorId, CANSparkMaxLowLevel.MotorType.kBrushed);

        motor.configEncoder(FridolinsMotor.FridoFeedBackDevice.kBuildin, 1);
        motor.setPID(Constants.pid);

        motor.enableForwardLimitSwitch(Constants.forwardLimitSwitchPolarity, true);
        motor.enableReverseLimitSwitch(Constants.reverseLimitSwitchPolarity, true);

        updateKF = new Thread(() -> {
            while (true) {
                updateFeedForward();
                try {
                    wait(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        updateKF.start();
    }

    @Override
    public void gotoZeroPoint() {
        motor.set(Constants.zeroingSpeed);
    }

    @Override
    public void gotoTraversalPoint() {
        motor.setPosition(Constants.Positions.traversalPosition);
    }

    @Override
    public boolean frontLimitSwitch() {
        return motor.isForwardLimitSwitchActive();
    }

    @Override
    public boolean backLimitSwitch() {
        return motor.isReverseLimitSwitchActive();
    }

    public static TilterBase getInstance() {
        if (instance == null) {
            if (enabled)
                instance = new Tilter();
            else
                instance = new TilterBase();
        }
        return instance;
    }

    @Override
    public void closeHooks() {
        this.hook.lockHook();
    }

    @Override
    public void openHooks() {
        this.hook.openHook();
    }

    private Optional<Double> targetPos;

    @Override
    public void setPosition(double ticks) {
        motor.setPosition(ticks);
        targetPos = Optional.of(ticks);
    }

    @Override
    public void stopMotor() {
        motor.stopMotor();
    }

    @Override
    public void resetEncoder() {
        motor.setEncoderPosition(0);
    }

    @Override
    public boolean isAtTargetPos() {
        return motor.pidAtTarget();
    }

    @Override
    public void setVelocity(double vel) {
        motor.set(vel);
    }

    @Override
    public List<Binding> getMappings() {
        return List.of(
                new Binding(Joysticks.Drive, () -> 5, Button::whenPressed, new InstantCommand(this::openHooks)),
                new Binding(Joysticks.Drive, () -> 3, Button::whenPressed, new InstantCommand(this::closeHooks))
        );
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addDoubleProperty("target pos", () -> targetPos.orElse(0.0), null);
        builder.addDoubleProperty("motor pos", motor::getEncoderTicks, null);
    }
}