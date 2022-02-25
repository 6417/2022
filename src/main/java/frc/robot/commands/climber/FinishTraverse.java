package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.Tilter;

public class FinishTraverse extends SequentialCommandGroup{
    public FinishTraverse() {
        addCommands(
            new InstantCommand(() -> Tilter.getInstance().openHooks()),
            new RetractTelescopearm()
        );
    }
}
