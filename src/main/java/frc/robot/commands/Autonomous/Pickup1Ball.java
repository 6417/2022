package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Ballsubsystem.EndPickup;
import frc.robot.commands.Ballsubsystem.ShootCommand;
import frc.robot.commands.Ballsubsystem.StartpickupCommand;
import frc.robot.subsystems.Drive;

public class Pickup1Ball extends SequentialCommandGroup {
    public Pickup1Ball() {
        addCommands(
                new ShootCommand(),
                new ParallelCommandGroup(new StartpickupCommand(), new driveToPickup()),
                new EndPickup()
        );
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        Drive.getInstance().setDirectionToForward();
    }
}
