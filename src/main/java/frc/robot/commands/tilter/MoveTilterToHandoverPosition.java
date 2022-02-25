package frc.robot.commands.tilter;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.Tilter;

public class MoveTilterToHandoverPosition extends Command{
    @Override
    public void initialize() {
        Tilter.getInstance().gotoHandoverPoint();
    }

    @Override
    public void end(boolean interrupted) {
        ClimberStatemachine.getInstance().fireEvent(new Events.TilterResetted());
        super.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return Tilter.getInstance().isAtTargetPos();
    }
}