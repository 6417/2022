package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveArmToPosition2Release extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoRelease2Position();
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
