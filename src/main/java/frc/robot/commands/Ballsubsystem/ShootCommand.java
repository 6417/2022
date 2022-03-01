package frc.robot.commands.Ballsubsystem;

import ch.fridolins.fridowpi.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.TimerCommand;
import frc.robot.commands.Shooter.SetShooterVelocity;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.Thrower;
import frc.robot.subsystems.ball.Transport;

public class ShootCommand extends SequentialCommandGroup {
    @Override
    public void initialize() {
        super.initialize();
        addCommands(
                    new SetShooterVelocity(),
                    new InstantCommand(() -> Transport.getInstance().run()),
                    new TimerCommand(2));
        addRequirements(BallSubsystem.getInstance());
    }

    @Override
    public void end(boolean interrupted) {
        Thrower.getInstance().setPercentage(0);
        Transport.getInstance().stop();
    }
}
