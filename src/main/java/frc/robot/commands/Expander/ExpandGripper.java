package frc.robot.commands.Expander;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;

public class ExpandGripper extends Command{
    @Override
    public void initialize() {
        PickUp.getInstance().openExpander();
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Finished");
    }

    @Override
    public boolean isFinished() {
        System.out.println(PickUp.getInstance().isAtTarget());
        return PickUp.getInstance().isAtTarget();
    }
}
