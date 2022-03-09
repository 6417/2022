package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.SequentialCommandGroup;
import frc.robot.commands.telescopeArm.CheckTraversalWrungContact;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.commands.tilter.MoveTilterToTraversalPosition;
import frc.robot.statemachines.ClimberStatemachine;

public class BeginTraversing extends SequentialCommandGroup{
    public BeginTraversing() {
        addCommands(
            new MoveTilterToTraversalPosition(),
            new CheckTraversalWrungContact()
        );
    } 

    @Override
    public void end(boolean interrupted) {
    }
}
