package frc.robot.commands.Expander;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.PickUp;

public class RetractGripperCommand extends Command{
    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void execute() {
        super.execute();
        PickUp.getInstance().closeExpander();
    }

    @Override
    public boolean isFinished() {
        return PickUp.getInstance().isAtTarget();
    }
}
