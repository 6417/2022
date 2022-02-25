package frc.robot.commands;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.Thrower;

public class AccelerateShooter extends Command{

    public AccelerateShooter() {
        requires(Thrower.getInstance());
    }

    @Override
    public void initialize() {
        // Thrower.getInstance().setVelocity(speed);
        BallSubsystem.getInstance().setShooterToVelocity();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return Thrower.getInstance().hasReachedVelocity();
    }
}
