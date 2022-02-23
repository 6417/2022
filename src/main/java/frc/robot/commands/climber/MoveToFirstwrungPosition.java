package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.ParallelCommandGroup;
import frc.robot.commands.telescopeArm.MoveArmToFirstwrungPosition;
import frc.robot.commands.tilter.MoveTilterToTraversalPosition;
import frc.robot.statemachines.FirstWrungStatemachine;

public class MoveToFirstWrungPosition extends ParallelCommandGroup{
    public MoveToFirstWrungPosition() {
        addCommands(new MoveArmToFirstwrungPosition(), new MoveTilterToTraversalPosition());
    }

    @Override
    public void end(boolean interrupted) {
        FirstWrungStatemachine.getInstance().fireEvent(new FirstWrungStatemachine.MoveUpFinished());
        super.end(interrupted);
    }
}
