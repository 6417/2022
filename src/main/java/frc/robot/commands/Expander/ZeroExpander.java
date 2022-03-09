package frc.robot.commands.Expander;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;

public class ZeroExpander extends Command{
    @Override
    public void initialize() {
        PickUp.getInstance().runExpander(-0.05);
    }

    @Override
    public boolean isFinished() {
        return PickUp.getInstance().isLimitSwitchActive();
    }

    @Override
    public void end(boolean interrupted) {
        PickUp.getInstance().resetExpander();
        PickUp.getInstance().runExpander(0);
    }
}
