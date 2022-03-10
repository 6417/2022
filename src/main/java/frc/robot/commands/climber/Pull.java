package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;

public class Pull extends SequentialCommandGroup{
    public Pull()  {
        addCommands(
            new RetractTelescopearm(),
            new CommandBase() {
                @Override
                public void initialize() {
                    TelescopeArm.getInstance().goDownSlowly();
                }

                @Override
                public boolean isFinished() {
                    return TelescopeArm.getInstance().getBottomLimitSwitchRight() &&
                        TelescopeArm.getInstance().getBottomLimitSwitchLeft();
                }
            }
        );
    }

    @Override
    public void end(boolean interrupted) {
        ClimberStatemachine.getInstance().fireEvent(new Events.PullFinished());
    }

}
