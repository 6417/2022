package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;

public class CheckTraversalWrungContact extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoCheckTraversalWrung();
    }

    @Override
    public void end(boolean interrupted) {
        if (TelescopeArm.getInstance().hasWrungContact()) {
            ClimberStatemachine.getInstance().fireEvent(new Events.traverseCheckSuccessful());
            return;
        }
        
        ClimberStatemachine.getInstance().fireEvent(new Events.finishedCheckingTraversal());
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
