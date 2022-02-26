package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.motors.LimitSwitch;
import ch.fridolins.fridowpi.pneumatics.FridoSolenoid;
import ch.fridolins.fridowpi.pneumatics.ISolenoid;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import ch.fridolins.fridowpi.utils.LatchedBoolean;
import ch.fridolins.fridowpi.utils.LatchedBooleanFalling;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;

public class TilterHook extends Module {
    public enum Hookstate {
        locked, unlocked
    }

    public static class Params {
        public final int id;
        public final boolean direction;

        static final class Side {
            public final LimitSwitch lockableSwitch;
            public final LimitSwitch pipeInHookSwitch;

            public Side(LimitSwitch lockableSwitch, LimitSwitch pipeInHookSwitch) {
                this.lockableSwitch = lockableSwitch;
                this.pipeInHookSwitch = pipeInHookSwitch;
            }
        }

        public final Side right;
        public final Side left;

        public Params(int id, boolean direction, Side right, Side left) {
            this.id = id;
            this.direction = direction;
            this.right = right;
            this.left = left;
        }
    }

    private final Params params;

    private ISolenoid solenoid;

    Params.Side right;
    Params.Side left;
    Hookstate state;

    public TilterHook(Params params) {
        requires(PneumaticHandler.getInstance());
        solenoid = new FridoSolenoid(params.id);
        this.params = params;
        right = params.right;
        left = params.left;
        state = Hookstate.locked;
        requires(solenoid);
    }

    @Override
    public void init() {
        super.init();
    }

    public void lockHook() {
        if (isLockable()) {
            solenoid.set(!params.direction);
            state = Hookstate.locked;
        } else
            DriverStation.reportWarning("hock was not locked", false);

    }

    public void openHook() {
        solenoid.set(params.direction);
        state = Hookstate.unlocked;
    }

    public boolean isWrungInHook() {
        return right.pipeInHookSwitch.get() && left.pipeInHookSwitch.get();
    }

    LatchedBoolean lockableLatchedBoolean = new LatchedBooleanFalling(false);
    public boolean isLockable() {
        return lockableLatchedBoolean.updateAndGet(right.lockableSwitch.get() && left.lockableSwitch.get());
    }

    /**
     * 
     * @return the state of the hook : locked or unlocked
     */
    public Hookstate getState() {
        return state;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addBooleanProperty("right pipe in hook switch", right.pipeInHookSwitch::get, null);
        builder.addBooleanProperty("left pipe in hook switch", left.pipeInHookSwitch::get, null);

        builder.addBooleanProperty("right is lockable switch", right.pipeInHookSwitch::get, null);
        builder.addBooleanProperty("left is lockable switch", left.pipeInHookSwitch::get, null);

        builder.addBooleanProperty("solenoid state", solenoid::get, null);
    }
}