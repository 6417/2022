package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class CheckWrungContact extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoCheckFristWrung();
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
