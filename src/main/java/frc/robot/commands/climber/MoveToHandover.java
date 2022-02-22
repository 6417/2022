package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveToHandover extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoHandover();
    }    

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
