package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;

public class CheckFirstWrungContact extends Command {
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoCheckFristWrung();
    }

    @Override
    public void end(boolean interrupted) {
        if (TelescopeArm.getInstance().hasWrungContact()) {
            ClimberStatemachine.getInstance().fireEvent(new Events.CheckPassed());
            return;
        }

        ClimberStatemachine.getInstance().fireEvent(new Events.CheckFinished());
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
