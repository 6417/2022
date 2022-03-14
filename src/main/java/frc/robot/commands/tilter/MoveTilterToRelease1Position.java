package frc.robot.commands.tilter;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.Tilter;

public class MoveTilterToRelease1Position extends Command{
    
    @Override
    public void initialize() {
        Tilter.getInstance().gotoRelease1Position();
    }

    @Override
    public boolean isFinished() {
        return Tilter.getInstance().isAtTargetPos();
    }
}
