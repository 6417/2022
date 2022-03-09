package frc.robot.commands.Shooter;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.Thrower;

public class SetShooterVelocity extends Command{
    @Override
    public void initialize() {
        BallSubsystem.getInstance().setShooterToVelocity();
    }

    @Override
    public boolean isFinished() {
        return Thrower.getInstance().isAtTarget();
    }
}