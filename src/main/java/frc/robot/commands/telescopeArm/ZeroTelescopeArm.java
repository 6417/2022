package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import ch.fridolins.fridowpi.utils.LatchedBoolean;
import frc.robot.subsystems.climber.TelescopeArm;

public class ZeroTelescopeArm extends Command {
    public ZeroTelescopeArm() {
        requires(TelescopeArm.getInstance());
    }

    @Override
    public void initialize() {
        TelescopeArm.getInstance().startZero();
    }

    @Override
    public void end(boolean interrupted) {
        TelescopeArm.getInstance().resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().getBottomLimitSwitchLeft() && TelescopeArm.getInstance().getBottomLimitSwitchRight();
    }
}
