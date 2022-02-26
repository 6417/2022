package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.Tilter;

public class FinishTraverse extends SequentialCommandGroup{
    public FinishTraverse() {
        addCommands(
            new CommandBase() {
                @Override
                public void initialize() {
                    Tilter.getInstance().openHooks();
                }

                @Override
                public boolean isFinished() {
                    return !Tilter.getInstance().hasWrungContact();
                }
            },
            new RetractTelescopearm()
        );
    }
}
