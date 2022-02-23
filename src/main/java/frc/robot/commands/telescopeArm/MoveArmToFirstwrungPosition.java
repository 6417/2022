package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.FirstWrungStatemachine;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveArmToFirstwrungPosition extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoFirstWrung();
    }

    @Override
    public void end(boolean interrupted) {
        FirstWrungStatemachine.getInstance().fireEvent(new FirstWrungStatemachine.MoveUpFinished());
        super.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}