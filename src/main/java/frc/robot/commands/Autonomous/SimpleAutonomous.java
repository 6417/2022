package frc.robot.commands.Autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.autonomous.PathviewerLoader;
import frc.robot.autonomous.RamseteCommandGenerator;
import frc.robot.autonomous.TrajectoryCreator;
import frc.robot.commands.Ballsubsystem.EndPickup;
import frc.robot.commands.Ballsubsystem.ShootCommand;
import frc.robot.commands.Ballsubsystem.StartpickupCommand;
import frc.robot.commands.Expander.ZeroExpander;

public class SimpleAutonomous extends SequentialCommandGroup {
    public SimpleAutonomous() {
        addCommands(
                new ZeroExpander(),
                new ShootCommand(),
                new ParallelCommandGroup(new StartpickupCommand(), new Straight3Meters()),
                new EndPickup()
        );
    }
}
