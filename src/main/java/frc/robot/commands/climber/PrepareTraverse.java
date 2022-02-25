package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.ParallelCommandGroup;
import frc.robot.commands.telescopeArm.MoveArmToPreparetraversalPosition;
import frc.robot.commands.telescopeArm.MoveArmToTraversalPosition;
import frc.robot.commands.tilter.MoveTilterToTraversalpreparationPoint;

public class PrepareTraverse extends ParallelCommandGroup{
   public PrepareTraverse() {
       addCommands(
            new MoveArmToPreparetraversalPosition(),
            new MoveTilterToTraversalpreparationPoint()
       );
   } 
}
