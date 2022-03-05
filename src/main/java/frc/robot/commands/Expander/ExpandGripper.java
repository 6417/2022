package frc.robot.commands.Expander;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;

public class ExpandGripper extends Command{
    @Override
    public void initialize() {
        PickUp.getInstance().openExpander();
    }

    @Override
    public boolean isFinished() {
        return PickUp.getInstance().isAtTarget();
    }
}
