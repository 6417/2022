package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.Binding;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.joystick.joysticks.LogitechExtreme;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;

import java.util.List;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Joysticks;
import frc.robot.subsystems.climber.base.TilterBase;

public class Tilter extends TilterBase {
    private static TilterBase instance = null;
    private static final boolean enabled = true;

    public static final class Constants {
        public static final int motorId = 0;
        public static final FridolinsMotor.LimitSwitchPolarity forwardLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kDisabled;
        public static final FridolinsMotor.LimitSwitchPolarity reverseLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kDisabled;

        public static final double zeroingSpeed = 0.0;

        public static final TilterHook.Params hookParams = new TilterHook.Params(0, true);
    }

    private FridolinsMotor motor;
    private TilterHook hook;

    private Tilter() {
        hook = new TilterHook(Constants.hookParams);
        requires(hook);
        Initializer.getInstance().addInitialisable(this);
        JoystickHandler.getInstance().bind(this);
    }

    @Override
    public void init() {
        super.init();

        // motor = new FridoCanSparkMax(Constants.motorId, CANSparkMaxLowLevel.MotorType.kBrushed);
        // motor.enableForwardLimitSwitch(Constants.forwardLimitSwitchPolarity, true);
        // motor.enableReverseLimitSwitch(Constants.reverseLimitSwitchPolarity, true);
    }

    public void gotoZeroPoint() {
        motor.set(Constants.zeroingSpeed);
    }

    public boolean frontLimitSwitch() {
        return motor.isForwardLimitSwitchActive();
    }
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

    @Override
    public List<Binding> getMappings() {
        return List.of(
            new Binding(Joysticks.Drive, () -> 5, Button::whenPressed, new InstantCommand(this::openHooks)),
            new Binding(Joysticks.Drive, () -> 3, Button::whenPressed, new InstantCommand(this::closeHooks))
        );
    }
}