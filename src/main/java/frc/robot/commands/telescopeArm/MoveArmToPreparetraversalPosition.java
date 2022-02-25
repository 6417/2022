package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveArmToPreparetraversalPosition extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoTraversalWrungPreparation();
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
