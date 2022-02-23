package frc.robot.commands.tilter;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.FirstWrungStatemachine;
import frc.robot.subsystems.climber.Tilter;

public class MoveTilterToTraversalPosition extends Command{
    @Override
    public void initialize() {
        Tilter.getInstance().gotoTraversalPoint();
    }

    @Override
    public void end(boolean interrupted) {
        FirstWrungStatemachine.getInstance().fireEvent(new FirstWrungStatemachine.TilterResetted());
        super.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return Tilter.getInstance().isAtTargetPos();
    }
}