package frc.robot.subsystems.climber.base;

import ch.fridolins.fridowpi.module.Module;

public class TilterBase extends Module {
    public void gotoZeroPoint() {
    }

    public boolean frontLimitSwitch() {
        return false;
    }

    public boolean backLimitSwitch() {
        return false;
    }

    public void closeHooks() {
    }

    public void openHooks() {
    }

    public void setPosition(double ticks) {

    }

    public boolean isAtTargetPos() {
        return true;
    }

    public void stopMotor() {

    }
}
