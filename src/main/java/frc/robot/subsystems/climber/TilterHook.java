package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.motors.LimitSwitch;
import ch.fridolins.fridowpi.pneumatics.FridoSolenoid;
import ch.fridolins.fridowpi.pneumatics.ISolenoid;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;

public class TilterHook extends Module {
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

    public TilterHook(Params params) {
        requires(PneumaticHandler.getInstance());
        solenoid = new FridoSolenoid(params.id);
        this.params = params;
        right = params.right;
        left = params.left;
        requires(solenoid);
    }

    @Override
    public void init() {
        super.init();
    }


    public void lockHook() {
        if (isLockable())
            solenoid.set(!params.direction);
        else
            DriverStation.reportWarning("hock was not locked", false);

    }

    public void openHook() {
        solenoid.set(params.direction);
    }

    public boolean isPipeInHook() {
        return right.pipeInHookSwitch.get() && left.pipeInHookSwitch.get();
    }

    public boolean isLockable() {
        return right.lockableSwitch.get() && left.lockableSwitch.get();
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