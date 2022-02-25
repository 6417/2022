package frc.robot.commands;

import ch.fridolins.fridowpi.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.base.TransportBase;

public class ShootCommand extends SequentialCommandGroup {
    private TransportBase transportSubmodule;

    public ShootCommand(TransportBase transportSubmodule) {
        this.transportSubmodule = transportSubmodule;
        addCommands(new AccelerateShooter(),
                    new InstantCommand(() -> transportSubmodule.run()));
        addRequirements(BallSubsystem.getInstance());
    }
}
