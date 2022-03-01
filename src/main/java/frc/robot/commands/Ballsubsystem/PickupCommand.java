package frc.robot.commands.Ballsubsystem;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Transport;
import frc.robot.subsystems.ball.base.PickUpBase;
import frc.robot.subsystems.ball.base.TransportBase;

public class PickupCommand extends Command{  
    @Override
    public void execute() {
        PickUp.getInstance().openExpander();
        PickUp.getInstance().runBrush();
        Transport.getInstance().run();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        PickUp.getInstance().stopBrush();
        Transport.getInstance().stop();
        PickUp.getInstance().closeExpander();
    }
}
