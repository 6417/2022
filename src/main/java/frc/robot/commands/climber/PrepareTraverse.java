package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.ParallelCommandGroup;
import frc.robot.commands.telescopeArm.MoveArmToPreparetraversalPosition;
import frc.robot.commands.telescopeArm.MoveArmToTraversalPosition;
import frc.robot.commands.tilter.MoveTilterToTraversalpreparationPoint;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;

public class PrepareTraverse extends ParallelCommandGroup{
   public PrepareTraverse() {
       addCommands(
            new MoveArmToPreparetraversalPosition(),
            new MoveTilterToTraversalpreparationPoint()
       );
   }

    @Override
    public void end(boolean interrupted) {
        ClimberStatemachine.getInstance().fireEvent(new Events.finishedTraversalPreparation());
    }
}
