package frc.robot.subsystems.ball;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.initializer.*;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice;
import ch.fridolins.fridowpi.motors.FridolinsMotor.IdleMode;
import ch.fridolins.fridowpi.motors.FridolinsMotor.LimitSwitchPolarity;
import frc.robot.subsystems.ball.base.PickUpBase;

public class PickUp extends PickUpBase {
    public static final class Constants {
        public static final class Brush {
            public static final int id = 0;
            public static final double speed = 1;
        }

        public static final class Expander {
            public static final int id = 0;

            public static final double angle = 0;

            public static final LimitSwitchPolarity forward = LimitSwitchPolarity.kDisabled;
            public static final LimitSwitchPolarity reverse = LimitSwitchPolarity.kDisabled;
        }
    }

    private class Motors implements Initialisable {
        private boolean initialized = true;

        public FridolinsMotor brush;
        public FridolinsMotor expander;

        public Motors() {
            Initializer.getInstance().addInitialisable(this);
        }

        @Override
        public void init() {
            brush = new FridoCanSparkMax(Constants.Brush.id, MotorType.kBrushless);
            expander = new FridoCanSparkMax(Constants.Expander.id, MotorType.kBrushless);

            brush.factoryDefault();
            expander.factoryDefault();

            brush.setIdleMode(IdleMode.kBrake);
            expander.setIdleMode(IdleMode.kBrake);

            brush.configEncoder(FridoFeedBackDevice.kBuildin, 42);
            expander.configEncoder(FridoFeedBackDevice.kBuildin, 42);

            expander.enableReverseLimitSwitch(Constants.Expander.reverse, true);
            expander.enableForwardLimitSwitch(Constants.Expander.forward, true);

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
    public void openExpander() { 
        motors.expander.isReverseLimitSwitchActive();
    }

    @Override
    public void closeExpander() {
        motors.expander.isForwardLimitSwitchActive();
    }

    @Override
    public void resetExpander() {
        motors.expander.isForwardLimitSwitchActive(); //TODO: Look if Forward or Reversed

    }
}
