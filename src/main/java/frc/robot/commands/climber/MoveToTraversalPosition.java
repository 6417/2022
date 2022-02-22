package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveToTraversalPosition extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoTraversalWrung();
        requires(TelescopeArm.getInstance());
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
