package frc.robot.commands.Ballsubsystem;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Transport;

public class EndPickup extends Command{
    @Override
    public void initialize() {
        PickUp.getInstance().stopBrush();
        Transport.getInstance().stop();
        PickUp.getInstance().closeExpander();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
