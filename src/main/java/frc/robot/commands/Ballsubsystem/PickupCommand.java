package frc.robot.commands.Ballsubsystem;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Transport;
import frc.robot.subsystems.ball.base.PickUpBase;
import frc.robot.subsystems.ball.base.TransportBase;

public class PickupCommand extends Command{  
    PickUpBase pickUpModule;
    TransportBase transportModule;

    public PickupCommand(PickUpBase pickUpModule, TransportBase transportModule) {
        this.pickUpModule = pickUpModule;
        this.transportModule = transportModule;
    }

    @Override
    public void initialize() {
        super.initialize();
        addRequirements(pickUpModule, transportModule);
    }
    
    @Override
    public void execute() {
        pickUpModule.openExpander();
        pickUpModule.runBrush();
        transportModule.run();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        pickUpModule.stopBrush();
        transportModule.stop();
    }
}
