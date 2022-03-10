package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import frc.robot.subsystems.climber.TelescopeArm;

public class MoveArmToPosition1Release extends Command {
    @Override
    public void initialize() {
        TelescopeArm.getInstance().gotoRelease1Position();
       
    }
    @Override
    public boolean isFinished() {
        return TelescopeArm.getInstance().isAtTarget();
        
    }
}
