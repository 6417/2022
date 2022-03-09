package frc.robot.subsystems.ball.base;

import ch.fridolins.fridowpi.module.Module;
import edu.wpi.first.math.geometry.Rotation2d;

public class ThrowerBase extends Module {

    public double getRequiredVelocity() {
        return 0.0;
    }

    public void setVelocity(double velocity) {
    }

    public void setPercentage(double percentage) {

    }

    public double getDistanceToTarget() {
        return 0.0;
    }

    public Rotation2d getAngleToTarget() {
        return new Rotation2d(0.0);
    }

    public boolean isDistanceValid() {
        return false;
    }

    public void blockMotor() {

    }

    public void unlockMotor() {

    }

    public boolean isAtTarget() {
        return false;
    }
}
