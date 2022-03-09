package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.ParallelCommandGroup;
import frc.robot.commands.telescopeArm.MoveArmToFirstwrungPosition;
import frc.robot.commands.tilter.MoveTilterToHandoverPosition;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;

public class MoveToFirstWrungPosition extends ParallelCommandGroup{
    public MoveToFirstWrungPosition() {
        addCommands(new MoveArmToFirstwrungPosition(), new MoveTilterToHandoverPosition());
    }

    @Override
    public void end(boolean interrupted) {
        ClimberStatemachine.getInstance().fireEvent(new Events.MoveUpFinished());
        super.end(interrupted);
    }
}
