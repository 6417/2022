package frc.robot.commands.Ballsubsystem;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.TimerCommand;
import frc.robot.commands.Shooter.SetShooterVelocity;
import frc.robot.commands.Tunnel.PullbackBallsCommand;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.Thrower;
import frc.robot.subsystems.ball.Transport;

public class ShootCommand extends SequentialCommandGroup {
    public ShootCommand() {
        addCommands(
                    new InstantCommand(() -> Thrower.getInstance().setPercentage(0.2)),
                    new WaitCommand(0.2),
                    new PullbackBallsCommand(),
                    new SetShooterVelocity(),
                    new InstantCommand(() -> Transport.getInstance().runShootspeed()),
                    new WaitCommand(2));
        // addRequirements(BallSubsystem.getInstance());
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("ended");
        Thrower.getInstance().setPercentage(0);
        Transport.getInstance().stop();
    }
}
