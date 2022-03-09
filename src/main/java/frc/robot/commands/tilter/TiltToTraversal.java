package frc.robot.commands.tilter;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.Tilter;

public class TiltToTraversal extends Command{
    @Override
    public void initialize() {
        Tilter.getInstance().gotoTraversalPoint();
    }

    @Override
    public boolean isFinished() {
        return Tilter.getInstance().isAtTargetPos();
    }


}
