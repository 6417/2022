package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.FirstWrungStatemachine;
import frc.robot.subsystems.climber.TelescopeArm;

public class CheckWrungContact extends Command{
    boolean alreadyFired;

    public CheckWrungContact() {
        alreadyFired = false;
    }

    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoCheckFristWrung();
    }

    @Override
    public void execute() {
        if (TelescopeArm.getInstance().hasWrungContact() && !alreadyFired) {
            FirstWrungStatemachine.getInstance().fireEvent(new FirstWrungStatemachine.CheckPassed());
            alreadyFired = true;
        }
        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        FirstWrungStatemachine.getInstance().fireEvent(new FirstWrungStatemachine.CheckFinished());
        super.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
