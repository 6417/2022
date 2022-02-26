package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveArmToFirstwrungPosition extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoFirstWrung();
    }

    @Override
    public void end(boolean interrupted) {
        ClimberStatemachine.getInstance().fireEvent(new Events.MoveUpFinished());
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}