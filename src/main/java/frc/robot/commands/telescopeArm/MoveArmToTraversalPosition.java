package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveArmToTraversalPosition extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoTraversalWrung();
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
