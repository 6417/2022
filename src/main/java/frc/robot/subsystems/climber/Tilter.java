package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.subsystems.climber.base.TilterBase;

public class Tilter extends TilterBase {
    private static TilterBase instance = null;
    private static final boolean enabled = true;

    public static final class Constants {
        public static final int motorId = 0;
        public static final FridolinsMotor.LimitSwitchPolarity forwardLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kDisabled;
        public static final FridolinsMotor.LimitSwitchPolarity reverseLimitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kDisabled;

        public static final double zeroingSpeed = 0.0;

        public static final TilterHook.Params leftHookParams = new TilterHook.Params(0, 0, DoubleSolenoid.Value.kOff);
        public static final TilterHook.Params rightHookParams = new TilterHook.Params(0, 0, DoubleSolenoid.Value.kOff);
    }

    private FridolinsMotor motor;
    private TilterHook leftHook;
    private TilterHook rightHook;

    private Tilter() {
        leftHook = new TilterHook(Constants.leftHookParams);
        rightHook = new TilterHook(Constants.rightHookParams);
        requires(leftHook);
        requires(rightHook);
        Initializer.getInstance().addInitialisable(this);
    }

    @Override
    public void init() {
        super.init();

        motor = new FridoCanSparkMax(Constants.motorId, CANSparkMaxLowLevel.MotorType.kBrushed);
        motor.enableForwardLimitSwitch(Constants.forwardLimitSwitchPolarity, true);
        motor.enableReverseLimitSwitch(Constants.reverseLimitSwitchPolarity, true);
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
}