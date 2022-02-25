package frc.robot.commands.Ballsubsystem;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Transport;
import frc.robot.subsystems.ball.base.PickUpBase;
import frc.robot.subsystems.ball.base.TransportBase;

public class ReverseflowCommand extends Command{
    TransportBase transportModule;
    PickUpBase pickupModule;

    public ReverseflowCommand(TransportBase transportModule, PickUpBase pickUpModule) {
        this.transportModule = transportModule;
        this.pickupModule = pickUpModule;
    }

    @Override
    public void initialize() {
        super.initialize();
        addRequirements(transportModule, pickupModule);
    }

    @Override
    public void execute() {
        super.execute();
        pickupModule.openExpander();
        transportModule.reverse();
        pickupModule.reverseBrush();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        Transport.getInstance().stop();
        PickUp.getInstance().stopBrush();
    }
}
