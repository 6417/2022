package frc.robot.commands.Ballsubsystem;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Transport;

public class ReverseflowCommand extends Command{
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        PickUp.getInstance().openExpander();
        Transport.getInstance().reverse();
        PickUp.getInstance().reverseBrush();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Transport.getInstance().stop();
        PickUp.getInstance().closeExpander();
        PickUp.getInstance().stopBrush();
    }
}
