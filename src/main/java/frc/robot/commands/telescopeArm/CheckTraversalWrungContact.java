package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;

public class CheckTraversalWrungContact extends Command{
    boolean alreadyFired;

    public CheckTraversalWrungContact() {
        alreadyFired = false;
    }

    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoCheckTraversalWrung();
    }

    @Override
    public void execute() {
        if (TelescopeArm.getInstance().hasWrungContact() && !alreadyFired) {
            ClimberStatemachine.getInstance().fireEvent(new Events.traverseCheckSuccessful());
            alreadyFired = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        ClimberStatemachine.getInstance().fireEvent(new Events.finishedCheckingTraversal());
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
