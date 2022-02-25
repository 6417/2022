package frc.robot.commands.Ballsubsystem;

import ch.fridolins.fridowpi.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.TimerCommand;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.base.TransportBase;

public class ShootCommand extends SequentialCommandGroup {
    private TransportBase transportSubmodule;

    public ShootCommand(TransportBase transportSubmodule) {
        this.transportSubmodule = transportSubmodule;
    }

    @Override
    public void initialize() {
        super.initialize();
        addCommands(new InstantCommand(() -> BallSubsystem.getInstance().setShooterToVelocity()),
                    new TimerCommand(0.5),
                    new InstantCommand(() -> transportSubmodule.run()));
        addRequirements(BallSubsystem.getInstance());
    }
}
