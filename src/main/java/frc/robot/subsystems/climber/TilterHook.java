package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.motors.LimitSwitch;
import ch.fridolins.fridowpi.pneumatics.FridoSolenoid;
import ch.fridolins.fridowpi.pneumatics.ISolenoid;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import ch.fridolins.fridowpi.utils.LatchedBoolean;
import ch.fridolins.fridowpi.utils.LatchedBooleanFalling;
import edu.wpi.first.util.sendable.SendableBuilder;

public class TilterHook extends Module {
    public enum Hookstate {
        locked, unlocked
    }

    public static class Params {
        public final int id;
        public final boolean direction;

        static final class Side {
            public final LimitSwitch lockableSwitch;
            public final LimitSwitch wrungContactSwitch;

            public Side(LimitSwitch lockableSwitch, LimitSwitch pipeInHookSwitch) {
                this.lockableSwitch = lockableSwitch;
                this.wrungContactSwitch = pipeInHookSwitch;
            }
        }

        public final int solenoid = 0;

        public Params(int id, boolean direction, Side right, Side left) {
            this.id = id;
            this.direction = direction;
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
        state = Hookstate.locked;
        requires(solenoid);
    }

    @Override
    public void init() {
        super.init();
        solenoid.init();
    }

    public void lockHook() {
//        if (isLockable()) {
//            solenoid.set(!params.direction);
//            state = Hookstate.locked;
//        } else
//            DriverStation.reportWarning("hock was not locked", false);

        solenoid.set(!params.direction);
    }

    public void openHook() {
        solenoid.set(params.direction);
        state = Hookstate.unlocked;
    }

    public boolean isWrungInHook() {
        return right.wrungContactSwitch.get() && left.wrungContactSwitch.get();
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
//        builder.addBooleanProperty("lockable right", () -> isInitialized() ? right.lockableSwitch.get() : false, null);
//        builder.addBooleanProperty("lockable left", () -> isInitialized() ? left.lockableSwitch.get(): false, null);
//        builder.addBooleanProperty("wrung right", () -> isInitialized() ? right.wrungContactSwitch.get() : false, null);
//        builder.addBooleanProperty("wrung left", () -> isInitialized() ? left.wrungContactSwitch.get(): false, null);
    }
}