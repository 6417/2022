package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.FirstWrungStatemachine;
import frc.robot.subsystems.climber.TelescopeArm;
import frc.robot.subsystems.climber.Tilter;

public class MoveArmToHandover extends Command{
    boolean alreadyFired;

    public MoveArmToHandover() {
        alreadyFired = false;
    }

    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoHandover();
    }    

    @Override
    public void execute() {
        if (Tilter.getInstance().hasWrungContact() && !alreadyFired) {
            FirstWrungStatemachine.getInstance().fireEvent(new FirstWrungStatemachine.HandoverCheckSuccess());
        }
        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        FirstWrungStatemachine.getInstance().fireEvent(new FirstWrungStatemachine.HandoverFinished());
        super.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
