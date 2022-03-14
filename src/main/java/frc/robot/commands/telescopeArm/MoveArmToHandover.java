package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;
import frc.robot.subsystems.climber.Tilter;

public class MoveArmToHandover extends Command{
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoHandover();
    }    

    @Override
    public void end(boolean interrupted) {
        if (Tilter.getInstance().hasWrungContact()) {
            ClimberStatemachine.getInstance().fireEvent(new Events.HandoverCheckSuccess());
            return;
        }

        ClimberStatemachine.getInstance().fireEvent(new Events.HandoverFinished());
    }

    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
    }
}
