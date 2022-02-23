package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class ZeroTelescopeArm extends Command {
    public ZeroTelescopeArm() {
       requires(TelescopeArm.getInstance());
    }

    @Override
    public void initialize() {
        super.initialize();
        TelescopeArm.getInstance().startZero();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        TelescopeArm.getInstance().resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().getBottomLimitSwitch();
    }
}
