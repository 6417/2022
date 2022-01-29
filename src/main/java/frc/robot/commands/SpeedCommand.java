package frc.robot.commands;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.Drive;

public class SpeedCommand extends Command {
    public SpeedCommand() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Drive.getInstance().setSpeed(Drive.Constants.Speeds.slow);
    }

    @Override
    public void end(boolean interrupted) {
        Drive.getInstance().setSpeed(Drive.Constants.Speeds.normal);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}