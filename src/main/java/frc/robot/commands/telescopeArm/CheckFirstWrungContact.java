package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;

public class CheckFirstWrungContact extends Command{
    boolean alreadyFired;

    public CheckFirstWrungContact() {
        alreadyFired = false;
    }

    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoCheckFristWrung();
    }

    @Override
    public void execute() {
        if (TelescopeArm.getInstance().hasWrungContact() && !alreadyFired) {
            ClimberStatemachine.getInstance().fireEvent(new Events.CheckPassed());
            alreadyFired = true;
        }
        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        ClimberStatemachine.getInstance().fireEvent(new Events.CheckFinished());
        super.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
