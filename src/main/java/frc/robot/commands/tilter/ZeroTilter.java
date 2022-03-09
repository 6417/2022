package frc.robot.commands.tilter;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.Tilter;

public class ZeroTilter extends Command {
    public ZeroTilter() {
        requires(Tilter.getInstance());
    }

    @Override
    public void initialize() {
        Tilter.getInstance().gotoZeroPoint();
    }

    @Override
    public void end(boolean interrupted) {
        Tilter.getInstance().stopMotor();
        Tilter.getInstance().resetEncoder();
    }

    @Override
    public boolean isFinished() {
        return Tilter.getInstance().frontLimitSwitch();
    }
}
