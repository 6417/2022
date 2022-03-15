package frc.robot.commands.Autonomous;

import ch.fridolins.fridowpi.command.Command;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.autonomous.PathviewerLoader;
import frc.robot.autonomous.RamseteCommandGenerator;
import frc.robot.subsystems.Drive;

import ch.fridolins.fridowpi.command.Command;
        import edu.wpi.first.math.trajectory.Trajectory;
        import edu.wpi.first.wpilibj2.command.CommandScheduler;
        import frc.robot.autonomous.PathviewerLoader;
        import frc.robot.autonomous.RamseteCommandGenerator;
        import frc.robot.subsystems.Drive;

public class driveToPickup extends Command {
    edu.wpi.first.wpilibj2.command.Command autonomousCommand;

    @Override
    public void initialize() {
        Trajectory path = PathviewerLoader.loadTrajectory("paths/output/driveToPickup.wpilib.json");
        autonomousCommand = RamseteCommandGenerator.generateRamseteCommand(path);
        CommandScheduler.getInstance().schedule(autonomousCommand);

        Drive.getInstance().setDirectionToForward();
    }

    @Override
    public boolean isFinished() {
        return !CommandScheduler.getInstance().isScheduled(autonomousCommand);
    }

    @Override
    public void end(boolean interrupted) {
        Drive.getInstance().setDirectionToForward();
    }
}
