package frc.robot.commands;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.base.PickUpBase;

public class RetractGripperCommand extends Command{
    PickUpBase pickupModule;

    public RetractGripperCommand(PickUpBase pickupModule) {
        this.pickupModule = pickupModule;
    }

    @Override
    public void initialize() {
        super.initialize();
        addRequirements(pickupModule);
    }

    @Override
    public void execute() {
        super.execute();
        pickupModule.closeExpander();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }
}
