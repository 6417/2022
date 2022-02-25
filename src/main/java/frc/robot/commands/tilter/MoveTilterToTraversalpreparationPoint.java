package frc.robot.commands.tilter;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.Tilter;

public class MoveTilterToTraversalpreparationPoint extends Command{
    @Override
    public void initialize() {
        Tilter.getInstance().gotoTraversalpreparationPoint();
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return Tilter.getInstance().isAtTargetPos();
    }
}
