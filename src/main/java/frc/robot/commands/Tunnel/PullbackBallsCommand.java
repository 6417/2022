package frc.robot.commands.Tunnel;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.Thrower;
import frc.robot.subsystems.ball.Transport;

public class PullbackBallsCommand extends Command{
    @Override
    public void initialize() {
        Transport.getInstance().reverse();
        Thrower.getInstance().setPercentage(0.2);
    }

    @Override
    public boolean isFinished() {
        return !BallSubsystem.getInstance().getShooterLightbarrier();
    }

    @Override
    public void end(boolean interrupted) {
        Transport.getInstance().stop();
        Thrower.getInstance().setPercentage(0);
    }
}
