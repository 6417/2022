package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.Binding;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.joystick.joysticks.Logitech;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;

import java.util.List;
import java.util.Optional;

import ch.fridolins.fridowpi.motors.LimitSwitch;
import ch.fridolins.fridowpi.motors.utils.PidValues;
import ch.fridolins.fridowpi.sensors.Navx;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
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
            public static final double handoverPosition = 0;
            public static final double traversalPosition = -222;
            public static final double traversalpreparationPoint = -247;
            public static final double release1Position = 0;
            public static final double release2Position = 0;
        }

        public static final int motorId = 20;
        public static final FridolinsMotor.LimitSwitchPolarity forwardLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kNormallyClosed;
        public static final FridolinsMotor.LimitSwitchPolarity reverseLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kNormallyClosed;

        public static final double zeroingSpeed = 0.0;

        // TODO: use corresponding limit switches
        private static DigitalInput rightHookWrungSwitch = new DigitalInput(1);
        private static DigitalInput leftHookWrungSwitch = new DigitalInput(0);
        private static DigitalInput rightHookLockable = new DigitalInput(3);
        private static DigitalInput leftHookLockable = new DigitalInput(2);
        public static final TilterHook.Params hookParams = new TilterHook.Params(0, true, new TilterHook.Params.Side(rightHookWrungSwitch::get, rightHookLockable::get), new TilterHook.Params.Side(leftHookWrungSwitch::get, leftHookLockable::get));

        public static final PidValues pid = new PidValues(2.0, 0.0, 0.8);

        static {
            pid.setTolerance(0.1);
        }
    }

    private FridolinsMotor motor;
    private TilterHook hook;

    private Tilter() {
        hook = new TilterHook(Constants.hookParams);
        requires(hook);
        addChild("Hooks", hook);
        Initializer.getInstance().addInitialisable(this);
        JoystickHandler.getInstance().bind(this);
    }

    @Override
    public void init() {
        super.init();

        hook.init();

        motor = new FridoCanSparkMax(Constants.motorId, CANSparkMaxLowLevel.MotorType.kBrushless);
        motor.factoryDefault();

        motor.configEncoder(FridolinsMotor.FridoFeedBackDevice.kBuildin, 1);
        motor.setPID(Constants.pid);

        motor.enableForwardLimitSwitch(Constants.forwardLimitSwitchPolarity, true);
        motor.enableReverseLimitSwitch(Constants.reverseLimitSwitchPolarity, true);
    }

    @Override
    public void periodic() {
        if (isInitialized()) {
            if (motor.isForwardLimitSwitchActive()) {
                motor.setEncoderPosition(0);
            }
        }
    }

    @Override
    public void gotoZeroPoint() {
        motor.set(Constants.zeroingSpeed);
    }

    @Override
    public void gotoHandoverPoint() {
        motor.setPosition(Constants.Positions.handoverPosition);
    }

    @Override
    public void gotoTraversalpreparationPoint() {
        motor.setPosition(Constants.Positions.traversalpreparationPoint);
    }

    @Override
    public void gotoTraversalPoint() {
        motor.setPosition(Constants.Positions.traversalPosition);
    }
    @Override
    public void gotoRelease1Position() {
        motor.setPosition(Constants.Positions.release1Position);
    }
    
    @Override
    public void gotoRelease2Position() {
        motor.setPosition(Constants.Positions.release2Position);
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

    private Optional<Double> targetPos = Optional.empty();

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
    public boolean hasWrungContact() {
        return hook.isWrungInHook();
    }

    @Override
    public void setVelocity(double vel) {
        motor.set(vel);
    }

    @Override
    public List<Binding> getMappings() {
        return List.of(
                new Binding(Joysticks.Climb, () -> 6, Button::whenPressed, new InstantCommand(this::resetEncoder)),
                new Binding(Joysticks.Climb, () -> 4, Button::whileHeld, new CommandBase() {
                    @Override
                    public void initialize() {
                        motor.set(0.3);
                    }

                    @Override
                    public void end(boolean interrupted) {
                        motor.stopMotor();
                    }
                }),
        new Binding(Joysticks.Climb, () -> 2, Button::whileHeld, new CommandBase() {
            @Override
            public void initialize() {
                motor.set(-0.3);
            }

            @Override
            public void end(boolean interrupted) {
                motor.stopMotor();
            }
        }),

        new Binding(Joysticks.Climb, () -> 8, Button::toggleWhenPressed, new CommandBase() {
            @Override
            public void initialize() {
                hook.openHook();
            }

            @Override
            public void end(boolean interrupted) {
                hook.lockHook();
            }
        })
        );
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addDoubleProperty("target pos", () -> targetPos.orElse(0.0), null);
        builder.addDoubleProperty("motor pos", motor::getEncoderTicks, null);
    }
}