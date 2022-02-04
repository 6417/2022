package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.pneumatics.FridoDoubleSolenoid;
import ch.fridolins.fridowpi.pneumatics.IDoubleSolenoid;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class TilterHook extends Module {
    public static class Params {
        public final int lowerId;
        public final int higherId;
        public final DoubleSolenoid.Value openSolenoid;

        public Params(int lowerId, int higherId, DoubleSolenoid.Value openSolenoid) {
            this.lowerId = lowerId;
            this.higherId = higherId;
            this.openSolenoid = openSolenoid;
        }
    }

    private final Params params;

    private IDoubleSolenoid solenoid;

    public TilterHook(Params params) {
        requires(PneumaticHandler.getInstance());
        this.params = params;
    }

    @Override
    public void init() {
        super.init();
        solenoid = new FridoDoubleSolenoid(params.lowerId, params.higherId);
    }

    public void lockHook() {
        if (isLockable())
            solenoid.set(closeSolenoidDirection());
    }

    private DoubleSolenoid.Value closeSolenoidDirection() {
        if (params.openSolenoid == DoubleSolenoid.Value.kForward)
            return DoubleSolenoid.Value.kReverse;
        if (params.openSolenoid == DoubleSolenoid.Value.kReverse)
            return DoubleSolenoid.Value.kForward;
        else
            return DoubleSolenoid.Value.kOff;
    }

    public void openHook() {
        solenoid.set(closeSolenoidDirection());
    }

    public boolean isLockable() {
        // TODO: find out which sensor is used
        return false;
    }
}