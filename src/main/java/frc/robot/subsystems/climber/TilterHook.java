package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.pneumatics.FridoDoubleSolenoid;
import ch.fridolins.fridowpi.pneumatics.FridoSolenoid;
import ch.fridolins.fridowpi.pneumatics.IDoubleSolenoid;
import ch.fridolins.fridowpi.pneumatics.ISolenoid;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class TilterHook extends Module {
    public static class Params {
        public final int id;
        public final boolean direction;

        public Params(int id, boolean direction) {
            this.id = id;
            this.direction = direction;
        }
    }

    private final Params params;

    private ISolenoid solenoid;

    public TilterHook(Params params) {
        requires(PneumaticHandler.getInstance());
        solenoid = new FridoSolenoid(params.id);
        this.params = params;
        requires(solenoid);
    }

    @Override
    public void init() {
        super.init();
    }

    public void lockHook() {
        if (isLockable())
            solenoid.set(!params.direction);
    }

    public void openHook() {
        solenoid.set(params.direction);
    }

    public boolean isLockable() {
        // TODO: find out which sensor is used
        return true;
    }
}