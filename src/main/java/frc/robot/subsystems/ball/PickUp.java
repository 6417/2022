package frc.robot.subsystems.ball;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.initializer.Initialisable;
import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice;
import ch.fridolins.fridowpi.motors.FridolinsMotor.IdleMode;
import ch.fridolins.fridowpi.motors.FridolinsMotor.LimitSwitchPolarity;
import ch.fridolins.fridowpi.motors.utils.PidValues;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.ZeroExpander;
import frc.robot.subsystems.ball.base.PickUpBase;

public class PickUp extends PickUpBase {
    public static final class Constants {
        public static final class Brush {
            public static final int id = 33;
            public static final double speed = 1;
        }

        public static final class Expander {
            public static final int id = 32;

            public static final PidValues pidValues = new PidValues(0.17, 0, 0);

            public static final double angle = 8;

            public static final LimitSwitchPolarity first = LimitSwitchPolarity.kNormallyClosed;
        }
    }

    private class Motors implements Initialisable {
        private boolean initialized = true;

        public FridoCanSparkMax brush;
        public FridoCanSparkMax expander;

        public Motors() {
            brush = new FridoCanSparkMax(Constants.Brush.id, MotorType.kBrushless);
            expander = new FridoCanSparkMax(Constants.Expander.id, MotorType.kBrushless);
            Initializer.getInstance().addInitialisable(this);
        }

        @Override
        public void init() {
            brush.factoryDefault();
            expander.factoryDefault();

            brush.setIdleMode(IdleMode.kBrake);
            expander.setIdleMode(IdleMode.kBrake);

            brush.configEncoder(FridoFeedBackDevice.kBuildin, 1);
            expander.configEncoder(FridoFeedBackDevice.kBuildin, 1);

            PickUp.this.registerSubmodule(brush);
            PickUp.this.registerSubmodule(expander);
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }
    }

    private PickUp() {
        Initializer.getInstance().addInitialisable(this);
        JoystickHandler.getInstance().bind(this);
    }

    public static PickUpBase getInstance() {
        if (instance == null) {
            if (enabled)
                instance = new PickUp();
            else
                instance = new PickUpBase();
        }
        return instance;
    }

    private Motors motors = new Motors();
    private static PickUpBase instance = null;
    private static boolean enabled = true;

    @Override
    public void init() {
        super.init();
        motors.init();

        motors.expander.setPID(Constants.Expander.pidValues);

        motors.expander.enableReverseLimitSwitch(Constants.Expander.first, true);

        motors.expander.getPIDController().setOutputRange(-0.2, 0.2);
    }

    @Override
    public void runBrush() {
        motors.brush.set(Constants.Brush.speed);
    }

    @Override
    public void reverseBrush() {
        motors.brush.set(-Constants.Brush.speed);
    }

    @Override
    public void stopBrush() {
        motors.brush.stopMotor();
    }

    @Override
    public void runExpander(double speed) {
        motors.expander.set(speed);
    }

    @Override
    public void openExpander() {
        motors.expander.setPosition(Constants.Expander.angle);
    }

    @Override
    public void closeExpander() {
        motors.expander.setPosition(0);
    }

    @Override
    public boolean isAtTarget() {
        return motors.expander.pidAtTarget();
    }

    @Override
    public boolean isLimitSwitchActive() {
        return motors.expander.isReverseLimitSwitchActive();
    }

    @Override
    public void resetExpander() {
        motors.expander.setEncoderPosition(0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("motorPosition", motors.expander::getEncoderTicks, null);
        super.initSendable(builder);
    }
}
